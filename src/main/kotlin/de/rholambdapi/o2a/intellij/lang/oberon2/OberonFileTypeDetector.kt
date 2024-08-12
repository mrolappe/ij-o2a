package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.FileTypeRegistry.FileTypeDetector
import com.intellij.openapi.util.io.ByteSequence
import com.intellij.openapi.vfs.VirtualFile

private val moduleRegex = Regex("MODULE[ ]+\\p{Alpha}\\w*;")

class OberonFileTypeDetector: FileTypeDetector {
    private val log = thisLogger()

    override fun detect(file: VirtualFile, firstBytes: ByteSequence, firstCharsIfText: CharSequence?): FileType? {
        log.debug("OberonFileTypeDetector, detect; file: $file, extension: ${file.extension}")

        if (file.extension == null || firstCharsIfText == null) {
            return null
        }

        val fileType = if (file.extension.equals(".mod") && firstCharsIfText.contains(moduleRegex)) {
            OberonFileType.INSTANCE
        } else {
            null
        }

        log.debug("OberonFileTypeDetector, detect; file: $file -> fileType: $fileType")
        return fileType
    }
}