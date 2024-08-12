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

    internal fun autodocFunctionDescriptionOrNull(functionName: Autodoc.ElementName): String? {
        return autodocFiles.firstNotNullOfOrNull {
            autodocFunctionDescriptionOrNull(
                functionName,
                it.inputStream
            )
        }
    }

    internal fun autodocFunctionDescriptionOrNull(functionName: Autodoc.ElementName, autodocFileInputStream: InputStream): String? {
        val autodoc = getAutodocsByFunctionName(functionName).firstOrNull()
        return autodoc
            ?.contentForSection(functionName, FUNCTION)
            ?.asString
    }

    internal fun getAutodocsByFunctionName(functionName: Autodoc.ElementName): Set<Autodoc> {
        // TODO use index infrastructure

        return autodocFiles.map {
            val autodoc = parseAutodoc(it.inputStream, it.name)
            if (autodoc == null) log.warn("Failed to parse ${it.name} as autodoc")
            autodoc
        }
            .filterNotNull()
            .filter { it.documentsFunction(functionName) }
            .toSet()
    }

    internal fun parseAutodoc(autodocFileInputStream: InputStream, sourceName: String): Autodoc? {
        val buffer = ByteArrayOutputStream()
        autodocFileInputStream.copyTo(buffer)

        val tocLines = ByteArrayInputStream(buffer.toByteArray()).reader(Charsets.ISO_8859_1).useLines { lines ->
            // end of TOC is marked by form feed
            lines
                .takeWhile { !it.startsWith(FF) }
                .toList()
        }

        val tocEntries = when {
            tocLines.isEmpty() || tocLines[0] != "TABLE OF CONTENTS" || tocLines[1].isNotBlank() -> {
                log.warn("No TOC header found in $sourceName")
                null
            }

            else -> tocLines.asSequence()
                .drop(2)
                .map {
                    val result = Regex("^([\\p{Alnum}._-]+)/([\\p{Alnum}._-]+)?\$").matchEntire(it) ?: return@map null
                    val moduleName = result.groupValues[1]
                    val elementName = result.groupValues[2]
                    require(moduleName.isNotBlank()) { "blank module name" }
                    // Apparently, blank element is allowed, see rexxsupport.library doc TODO special handling?

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

        if (tocEntries.isNullOrEmpty()) {
            log.warn("Failed to parse TOC entries of $sourceName")
            return null
        }

        val entryHeaderRegex = Regex("^\\f([\\p{Alnum}._\\-/]{1,39})\\s*\\1\$")

        val autodoc = ByteArrayInputStream(buffer.toByteArray())
            .reader(Charsets.ISO_8859_1)
            .readLines()
            .let { lines ->
                // skip TOC and form feed marker
                var idx = lines.indexOfFirst { it.startsWith(FF) }

                // no marker found, so assume there was no TOC either -> fail
                if (idx == 0) return null

                var tocEntry = tocEntries.firstOrNull { lines[idx].startsWith(it.entryName, 1) }

                // TODO handle names truncated due to overlap
                if (tocEntry == null) {
                    log.warn("Source $sourceName; aborting, no matching TOC entry for header line: ${lines[idx]}")
                    return@let null
                }

                val entries = mutableListOf<Autodoc.Entry>()

                while (idx < lines.size && tocEntry != null) {
                    // skip header line/FF and empty lines
                    ++idx
                    while (idx < lines.size && lines[idx].isBlank()) ++idx

                    val contentLinesOfEntry = buildList {
                        while (idx < lines.size && !lines[idx].startsWith(FF)) add(lines[idx++])
                    }

                    val entrySections = entrySectionsFromLines(contentLinesOfEntry.asSequence())

                    entries.add(Autodoc.Entry(Autodoc.ElementName.from(tocEntry.elementName), entrySections))

                    while (idx < lines.size && lines[idx].isBlank()) ++idx
                    if (idx == lines.size) break

                    tocEntry = tocEntries.firstOrNull { lines[idx].startsWith(it.entryName, 1) }
                    if (tocEntry == null) {
                        log.warn("Source $sourceName; aborting, no matching TOC entry for header line: ${lines[idx]}")
                        return@let null
                    }
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

    fun contentForSection(element: ElementName, section: SectionName): Entry.SectionContent? {
        return entryByName[element]?.sections?.get(section)
    }

    @JvmInline
    value class ModuleName(val name: String) {
        init {
            require(Regex("[\\p{Alnum}_.-]+").matches(name)) { "Illegal module name: $name" }
        }

        companion object {
            fun from(string: String) = ModuleName(string)
        }
    }

    @JvmInline
    value class ElementName(val name: String) {
        init {
            require(Regex("([\\p{Alnum}_.-]+)?").matches(name)) { "Illegal element name: $name" }
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