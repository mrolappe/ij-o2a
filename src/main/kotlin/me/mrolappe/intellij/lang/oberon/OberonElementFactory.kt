package me.mrolappe.intellij.lang.oberon

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import me.mrolappe.intellij.lang.oberon.psi.OberonFile

object OberonElementFactory {
    fun createFile(project: Project, text: String): OberonFile =
        PsiFileFactory.getInstance(project).createFileFromText("dummy.mod", OberonFileType.INSTANCE, text) as OberonFile
}
