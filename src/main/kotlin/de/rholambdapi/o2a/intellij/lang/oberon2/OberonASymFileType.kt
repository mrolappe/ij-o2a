package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.openapi.fileTypes.FileType
import javax.swing.Icon


class OberonASymFileType internal constructor() : FileType {
    companion object {
        val INSTANCE = OberonASymFileType()
    }

    override fun getName(): String {
        return "OBERON-A_SYM"
    }

    override fun getDescription(): String {
        return "Oberon-A symbol file"
    }

    override fun getDefaultExtension(): String {
        return "sym"
    }

    override fun getIcon(): Icon? {
        return null
    }

    override fun isBinary(): Boolean {
        return true
    }
}