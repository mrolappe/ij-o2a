package de.rholambdapi.o2a.intellij.lang.oberon2.psi.impl

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.impl.light.LightElement
import de.rholambdapi.o2a.intellij.lang.oberon2.OberonUtil
import de.rholambdapi.o2a.intellij.lang.oberon2.OberonUtil.findAllModules
import de.rholambdapi.o2a.intellij.lang.oberon2.getNamesOfImportedModules
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonImportModuleReference
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonModuleDef

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

    override fun getVariants(): Array<String> {
        val namesOfImportedModules = getNamesOfImportedModules(referencingElement.containingFile, includeAliases = true)
        val namesOfAllVisibleModules = findAllModules(referencingElement.project)
            .mapNotNull { it.moduleName?.text }

        return namesOfAllVisibleModules
            .filterNot { namesOfImportedModules.containsKey(it) || namesOfImportedModules.containsValue(it) }
            .toTypedArray()
    }

}

val OberonModuleDef.moduleName
    get() = moduleHead.moduleName

class OberonPseudoModuleReference(private val referencingElement: OberonImportModuleReference) :
    PsiReferenceBase<OberonImportModuleReference>(referencingElement, referencingElement.ident.textRangeInParent) {
    override fun getVariants(): Array<Any> {
        return arrayOf("oberon pseudo mod", "pling", "pling")
    }

    override fun resolve(): PsiElement {
        return object : LightElement(referencingElement.manager, referencingElement.language) {
            override fun toString(): String {
                return "pseudo module reference ${referencingElement.text}"
            }

            override fun getText(): String {
                return "PsiElement.text von OberonPseudoModuleReference für ${referencingElement.text}"
            }
        }
        // TODO resolve to synthetic PSI element?
    }
}
