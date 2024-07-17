package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonFile

object OberonElementFactory {
    fun createFile(project: Project, text: String): OberonFile =
        PsiFileFactory.getInstance(project).createFileFromText("dummy.mod", OberonFileType.INSTANCE, text) as OberonFile
}
