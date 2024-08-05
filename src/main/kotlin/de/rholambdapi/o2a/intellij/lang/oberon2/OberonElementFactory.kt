package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import com.intellij.psi.util.descendantsOfType
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonFile
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonModuleHead
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonProcedureDecl

object OberonElementFactory {
    fun createFile(project: Project, text: String): OberonFile =
        PsiFileFactory.getInstance(project).createFileFromText("dummy.mod", OberonFileType.INSTANCE, text) as OberonFile

    fun createModuleHead(project: Project, moduleName: String): OberonModuleHead {
        val file = PsiFileFactory.getInstance(project)
            .createFileFromText("dummy.mod", OberonFileType.INSTANCE, "MODULE $moduleName;") as OberonFile
        return file.moduleHead!!
    }

    fun createEmptyNoArgProcedure(project: Project, procName: String): OberonProcedureDecl {
        val file = PsiFileFactory.getInstance(project).createFileFromText(
            "dummy.mod",
            OberonFileType.INSTANCE,
            """
                MODULE dummy;
                    PROCEDURE $procName; 
                    BEGIN END $procName;
                END dummy.
                """.trimIndent()
        ) as OberonFile
        return file.descendantsOfType<OberonProcedureDecl>(childrenFirst = true).first()
    }

}
