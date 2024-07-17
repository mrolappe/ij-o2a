package me.mrolappe.intellij.lang.oberon.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import me.mrolappe.intellij.lang.oberon.OberonFileType
import me.mrolappe.intellij.lang.oberon.OberonLanguage

class OberonFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, OberonLanguage.INSTANCE) {
    override fun getFileType(): FileType = OberonFileType.INSTANCE

    override fun toString(): String = "Oberon File"
}