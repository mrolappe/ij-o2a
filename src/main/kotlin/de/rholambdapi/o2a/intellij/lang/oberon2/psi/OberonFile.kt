package de.rholambdapi.o2a.intellij.lang.oberon2.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import de.rholambdapi.o2a.intellij.lang.oberon2.OberonFileType
import de.rholambdapi.o2a.intellij.lang.oberon2.OberonLanguage

class OberonFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, OberonLanguage.INSTANCE) {
    override fun getFileType(): FileType = OberonFileType.INSTANCE

    override fun toString(): String = "Oberon File"
}