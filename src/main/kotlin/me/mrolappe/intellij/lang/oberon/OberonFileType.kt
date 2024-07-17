package me.mrolappe.intellij.lang.oberon

import com.intellij.openapi.fileTypes.LanguageFileType
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.Icon

class OberonFileType private constructor() : LanguageFileType(OberonLanguage.INSTANCE) {
    override fun getName(): String = "Oberon"

    override fun getDescription(): String = "Oberon language file"

    override fun getDefaultExtension(): String = "mod"

    override fun getIcon(): Icon = OberonIcons.MODULE

    override fun getCharset(file: VirtualFile, content: ByteArray): String = "ISO-8859-1"

    companion object {
        val INSTANCE = OberonFileType()
    }
}