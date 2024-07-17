package me.mrolappe.intellij.lang.oberon

import com.intellij.icons.AllIcons
import com.intellij.ide.navigationToolbar.StructureAwareNavBarModelExtension
import com.intellij.lang.Language
import me.mrolappe.intellij.lang.oberon.psi.OberonFile
import me.mrolappe.intellij.lang.oberon.psi.OberonModuleDef
import me.mrolappe.intellij.lang.oberon.psi.OberonModuleInit
import me.mrolappe.intellij.lang.oberon.psi.OberonProcedureDecl
import me.mrolappe.intellij.lang.oberon.psi.impl.procedureName
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
