package de.rholambdapi.o2a.intellij

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.isFile
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

// TODO use indexing infrastructure

internal fun autodocFunctionDescriptionOrNull(functionName: String, autodocFilePath: Path): String? {
    return AutodocLookup(autodocFilePath).autodocFunctionDescriptionOrNull(functionName)
}

class AutodocLookup(private val basePath: Path) {
    private val log = thisLogger<AutodocLookup>()
    private val autodocFiles: List<VirtualFile>

    init {
        require(basePath.exists())
        require(basePath.isDirectory())

        autodocFiles = VirtualFileManager.getInstance()
            .findFileByNioPath(basePath)
            ?.children
            ?.filterNotNull()
            ?.filter { it.isValid && it.isFile && "doc" == it.extension }
            ?: emptyList()

        log.debug("Found %d autodoc files:", autodocFiles.size)
        log.debug(autodocFiles.joinToString("\n"))

        // TODO notify user if no files found
    }

    internal fun autodocFunctionDescriptionOrNull(functionName: String): String? {
        return autodocFiles.firstNotNullOfOrNull {
            autodocFunctionDescriptionOrNull(
                functionName,
                it.inputStream
            )
        }
    }

    internal fun autodocFunctionDescriptionOrNull(functionName: String, autodocFileInputStream: InputStream): String? {
        val autodoc = getAutodocsByFunctionName(functionName).firstOrNull()
        return autodoc
            ?.sectionContentFor(functionName.asElementName, FUNCTION)
            ?.asString
    }

    internal fun getAutodocsByFunctionName(functionName: String): Set<Autodoc> {
        // TODO use index infrastructure

        return autodocFiles.map { parseAutodoc(it.inputStream) }
            .filterNotNull()
            .filter { it.documentsFunction(Autodoc.ElementName.from(functionName)) }
            .toSet()
    }

    internal fun parseAutodoc(autodocFileInputStream: InputStream): Autodoc? {
        val buffer = ByteArrayOutputStream()
        autodocFileInputStream.copyTo(buffer)

        val tocLines = ByteArrayInputStream(buffer.toByteArray()).reader(Charsets.ISO_8859_1).useLines { lines ->
            // end of TOC is marked by form feed
            lines
                .takeWhile { !it.startsWith(FF) }
                .toList()
        }

        val tocEntries = when {
            tocLines.isEmpty() || tocLines[0] != "TABLE OF CONTENTS" || tocLines[1].isNotEmpty() -> null

            else -> tocLines.asSequence()
                .drop(2)
                .map {
                    val result = Regex("^([\\p{Alpha}._]+)/([\\p{Alpha}_]+)\$").matchEntire(it) ?: return@map null
                    val moduleName = result.groupValues[1]
                    val elementName = result.groupValues[2]
                    require(moduleName.isNotBlank()) { "blank module name" }
                    require(elementName.isNotBlank()) { "blank element name" }
                    TocEntry(moduleName, elementName)
                }
                .let { entries ->
                    if (entries.any { it == null }) {
                        null
                    } else {
                        entries.filterNotNull().toList() /* call is only for non-null entry type */
                    }
                }
        }

        if (tocEntries.isNullOrEmpty()) return null

        val entryHeaderRegex = Regex("^\\f(([\\p{Alpha}.]+)/([\\p{Alpha}_-]+))\\s*\\1\$")

        val autodoc = ByteArrayInputStream(buffer.toByteArray())
            .reader(Charsets.ISO_8859_1)
            .readLines()
            .let { lines ->
                // skip TOC and form feed marker
                var idx = lines.indexOfFirst { it.startsWith(FF) }

                // no marker found, so assume there was no TOC either -> fail
                if (idx == 0) return null

                // TODO handle names truncated due to overlap
                var matchResult = entryHeaderRegex.matchEntire(lines[idx])

                val entries = mutableListOf<Autodoc.Entry>()

                while (idx < lines.size && matchResult != null) {
                    // skip header line/FF and empty lines
                    ++idx
                    while (idx < lines.size && lines[idx].isEmpty()) ++idx

                    val contentLinesOfEntry = buildList {
                        while (idx < lines.size && !lines[idx].startsWith(FF)) add(lines[idx++])
                    }

                    val entrySections = entrySectionsFromLines(contentLinesOfEntry.asSequence())
                    val elementName = matchResult.groupValues[3]
                    entries.add(Autodoc.Entry(Autodoc.ElementName.from(elementName), entrySections))

                    matchResult = if (idx < lines.size) entryHeaderRegex.matchEntire(lines[idx]) else null

                }

                Autodoc(Autodoc.ModuleName.from(tocEntries.first().moduleName), entries)
            }

        return autodoc
    }
}

data class TocEntry(val moduleName: String, val elementName: String) {
    val entryName
        get() = "$moduleName/$elementName"
}

@JvmInline
value class SectionName(val name: String) {
    init {
        require(name.isNotEmpty())
    }
}

private val String.asElementName: Autodoc.ElementName
    get() = Autodoc.ElementName(this)

val FUNCTION = SectionName("FUNCTION")
val INPUTS = SectionName("INPUTS")
val NAME = SectionName("NAME")

data class Autodoc(private val moduleName: ModuleName, private val entries: List<Entry>) {
    private val entryByName by lazy { entries.associateBy { it.name } }

    fun documentsFunction(name: ElementName): Boolean {
        return entryByName.containsKey(name)
    }

    fun sectionContentFor(element: ElementName, section: SectionName): Entry.SectionContent? {
        return entryByName[element]?.sections?.get(section)
    }

    @JvmInline
    value class ModuleName(val name: String) {
        init {
            require(Regex("[\\p{Alpha}_.]+").matches(name)) { "Illegal module name: $name" }
        }

        companion object {
            fun from(string: String) = ModuleName(string)
        }
    }

    @JvmInline
    value class ElementName(val name: String) {
        init {
            require(Regex("[\\p{Alpha}_]+").matches(name)) { "Illegal element name: $name" }
        }

        companion object {
            fun from(nameString: String) = ElementName(nameString)
        }
    }

    data class Entry(val name: ElementName, val sections: Map<SectionName, SectionContent>) {
        data class SectionContent(private val content: String) {
            val asString: String
                get() = content
        }

    }
}

private const val FF = '\u000c'


fun entrySectionsFromLines(lines: Sequence<String>): Map<SectionName, Autodoc.Entry.SectionContent> {
    val sectionTitleRegex = Regex("^ {3,4}[a-zA-Z]+\$")

    val result = mutableMapOf<SectionName, Autodoc.Entry.SectionContent>()

    val iterator = lines.iterator()
    var current = if (iterator.hasNext()) iterator.next() else null

    while (current != null) {
        val sectionLines = StringBuilder()

        if (current.matches(sectionTitleRegex)) {
            val sectionTitle = current.trim()

            current = if (iterator.hasNext()) iterator.next() else null

            while (current?.matches(sectionTitleRegex) == false) {
                sectionLines.append(current)
                current = if (iterator.hasNext()) iterator.next() else null
            }

            result.put(
                SectionName(sectionTitle),
                Autodoc.Entry.SectionContent(sectionLines.toString())
            )
            sectionLines.clear()
        } else {
            current = if (iterator.hasNext()) iterator.next() else null
        }
    }

    return result
}