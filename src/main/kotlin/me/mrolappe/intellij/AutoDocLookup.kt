package me.mrolappe.intellij

import com.intellij.openapi.vfs.VirtualFileManager
import com.intellij.openapi.vfs.isFile
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.nio.file.Path

@JvmInline
value class ModuleName(val name: String) {
    init {
        require(name.isNotEmpty())
    }
}

class AutodocEntry(private val moduleName: ModuleName, private val sections: Map<SectionName, SectionContent>) {
    companion object {
        val FUNCTION = SectionName("FUNCTION")
        val INPUTS = SectionName("INPUTS")
        val NAME = SectionName("NAME")
    }

    @JvmInline
    value class SectionName(val name: String) {
        init {
            require(name.isNotEmpty())
        }
    }

    class SectionContent(val content: String)

    fun getNameSectionRawContent(): String? = sections[NAME]?.content

    fun getFunctionSectionRawContent(): String? = sections[FUNCTION]?.content
}

private const val FF = '\u000c'


internal fun autodocFunctionDescriptionOrNull(functionName: String): String? {
//    VirtualFileManager.getInstance().findFileByNioPath(Path.of("dingens"))?.findChild("")?.inputStream.bufferedReader().useLines {  }

    return VirtualFileManager.getInstance()
        .findFileByNioPath(Path.of("/Volumes/maschinenraum/entw/retro/retro-dev/NDK3.2/Autodocs"))
        ?.children
        ?.filter { it.isValid && it.isFile && "doc" == it.extension }
        ?.firstNotNullOfOrNull { autodocFunctionDescriptionOrNull(functionName, it.inputStream) }
}

internal fun autodocFunctionDescriptionOrNull(functionName: String, autodocFilePath: Path): String? {
//    val file = File("/Volumes/maschinenraum/entw/retro/retro-dev/NDK3.2/Autodocs/dos.doc")
    val file = autodocFilePath.toFile()
    return autodocFunctionDescriptionOrNull(functionName, file.inputStream())
}

internal fun autodocFunctionDescriptionOrNull(functionName: String, autodocFileInputStream: InputStream): String? {
    val buffer = ByteArrayOutputStream()
    autodocFileInputStream.copyTo(buffer)

    val tocLines = ByteArrayInputStream(buffer.toByteArray()).reader(Charsets.ISO_8859_1).useLines { lines ->
        // end of TOC is marked by form feed
        lines
            .takeWhile { !it.startsWith(FF) }
            .toList()
    }

    val tocEntryForProc = when {
        tocLines.isEmpty() || tocLines[0] != "TABLE OF CONTENTS" || tocLines[1].isNotEmpty() -> null

        else -> tocLines.asSequence()
            .drop(2)
            .firstOrNull { it.matches(Regex("^.*/$functionName\$")) }
    }

    tocEntryForProc ?: return null

    ByteArrayInputStream(buffer.toByteArray()).reader(Charsets.ISO_8859_1).useLines { lines ->
        val afterToc = lines.dropWhile { !it.startsWith(FF) }    // skip TOC

        var result: String? = null

        val startOfEntry = afterToc.dropWhile { !it.matches(Regex("^\\f$tocEntryForProc\\s*$tocEntryForProc\$")) }

        val entryLines = startOfEntry
            .drop(1)                         // skip header line
            .dropWhile { it.isEmpty() }         // skip empty lines before section start
            .takeWhile { !it.startsWith(FF) }   // until end of entry

        return toSections(entryLines)[AutodocEntry.FUNCTION]?.content
    }
}

fun toSections(lines: Sequence<String>): Map<AutodocEntry.SectionName, AutodocEntry.SectionContent> {
    val sectionTitleRegex = Regex("^ {3,4}[a-zA-Z]+\$")

    val result = mutableMapOf<AutodocEntry.SectionName, AutodocEntry.SectionContent>()

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

            result.put(AutodocEntry.SectionName(sectionTitle), AutodocEntry.SectionContent(sectionLines.toString()))
            sectionLines.clear()
        } else {
            current = if (iterator.hasNext()) iterator.next() else null;
        }
    }

    return result
}

fun main() {
    val description = autodocFunctionDescriptionOrNull("DoPkt")
    println(description)
}