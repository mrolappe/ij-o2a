package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.icons.AllIcons
import com.intellij.ide.navigationToolbar.StructureAwareNavBarModelExtension
import com.intellij.lang.Language
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonFile
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonModuleDef
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonModuleInit
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonProcedureDecl
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.impl.procedureName
import javax.swing.Icon

class OberonStructureAwareNavBar : StructureAwareNavBarModelExtension() {
    override val language: Language
        get() = OberonLanguage.INSTANCE

    override fun getPresentableText(obj: Any): String? {
        return when (obj) {
            is OberonFile -> obj.name
            is OberonModuleDef -> obj.name
            is OberonProcedureDecl -> obj.procedureName
            is OberonModuleInit -> "<module init>"
            else -> null
        }
    }

    override fun getIcon(obj: Any?): Icon? {
        return when(obj) {
            is OberonModuleDef -> AllIcons.Nodes.Module
            is OberonProcedureDecl -> AllIcons.Nodes.Function
            is OberonModuleInit -> AllIcons.Nodes.Function
            else -> super.getIcon(obj)
        }
    }
}
