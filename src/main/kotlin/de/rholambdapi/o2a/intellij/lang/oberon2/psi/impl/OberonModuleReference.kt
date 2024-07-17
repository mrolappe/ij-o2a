package de.rholambdapi.o2a.intellij.lang.oberon2.psi.impl

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase

import de.rholambdapi.o2a.intellij.lang.oberon2.OberonUtil

class OberonModuleReference(private val referencingElement: de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonImportModuleReference) :
    PsiReferenceBase<de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonImportModuleReference>(referencingElement, referencingElement.ident.textRangeInParent) {

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
