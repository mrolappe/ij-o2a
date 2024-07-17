package me.mrolappe.intellij.lang.oberon.psi.impl

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import me.mrolappe.intellij.lang.oberon.OberonUtil
import me.mrolappe.intellij.lang.oberon.psi.OberonImportModuleReference

class OberonModuleReference(private val referencingElement: OberonImportModuleReference) :
    PsiReferenceBase<OberonImportModuleReference>(referencingElement, referencingElement.ident.textRangeInParent) {

    companion object {
        val LOG = thisLogger()
    }

    override fun resolve(): PsiElement? {
        val moduleNameToResolve = referencingElement.ident.text
        val resolvedModules = OberonUtil.findModulesByName(referencingElement.project, moduleNameToResolve)
//        val resolvedModules = emptyArray<OberonModuleDef>()
        LOG.debug("TODO OberonModuleReference::resolve $moduleNameToResolve -> $resolvedModules")
        return if (resolvedModules.isNotEmpty()) resolvedModules.first() else null
    }
}
