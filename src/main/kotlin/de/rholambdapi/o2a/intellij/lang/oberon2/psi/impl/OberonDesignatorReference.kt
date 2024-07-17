package de.rholambdapi.o2a.intellij.lang.oberon2.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.parentOfType
import de.rholambdapi.o2a.intellij.lang.oberon2.OberonElementFactory
import de.rholambdapi.o2a.intellij.lang.oberon2.logDebug
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonNamedElement

class OberonDesignatorReference(private val referencingElement: de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonDesignator) :
    PsiReferenceBase<de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonDesignator>(referencingElement, referencingElement.textRangeInParent) {

    override fun resolve(): PsiElement? {
        if (referencingElement.textMatches("ASSERT")) {
            return resolveToSynthetic("ASSERT")
        }
        val resolveResults = mutableListOf<OberonNamedElement>()
        val designatorString = referencingElement.designatorString

        if (!designatorString.contains('.')) {
            resolveByMemberName(referencingElement, designatorString).also { resolveResults.addAll(it) }
        } else {
            val parts = designatorString.split('.')
            // TODO designator might also refer to record member
            val moduleName = referencingElement.parentOfType<de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonModuleDef>()
                ?.let { moduleNameForImportNameOrNull(it, parts.first()) }
            val memberName = designatorString.substringAfter('.')

            if (moduleName != null) {
                resolveByModuleNameAndMemberName(referencingElement, moduleName, memberName)
                    .let { resolveResults.addAll(it) }
            }
        }

        val resolvedElement = resolveResults.firstOrNull()
        logDebug("OberonDesignatorReference::resolve, referencingElement: $designatorString, -> $resolvedElement (#: ${resolveResults.size})")
        // TODO multi resolve
        return resolvedElement
    }

    private fun resolveToSynthetic(text: String): PsiElement? {
        val project = referencingElement.project
        val file = OberonElementFactory.createFile(project, "MODULE dummy; PROCEDURE $text(cond: BOOLEAN; code: INTEGER); BEGIN END $text; END dummy.")
        val element = file.findChildByClass(de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonModuleDef::class.java)
            ?.procedureDeclList?.firstOrNull()
        return element
    }


}

