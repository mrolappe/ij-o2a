package de.rholambdapi.o2a.intellij.lang.oberon2.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.parentOfType
import de.rholambdapi.o2a.intellij.lang.oberon2.OberonElementFactory
import de.rholambdapi.o2a.intellij.lang.oberon2.logDebug
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonDesignator
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonModuleDef
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonNamedElement

class OberonDesignatorReference(private val referencingElement: OberonDesignator) :
    PsiReferenceBase<OberonDesignator>(referencingElement, referencingElement.textRangeInParent) {

    override fun resolve(): PsiElement? {
        if (referencingElement.textMatches("ASSERT")) {
            return resolveToSynthetic("ASSERT")
        }
        val resolveResults = mutableListOf<PsiNamedElement>()
        val designatorString = referencingElement.designatorString

        if (!designatorString.contains('.')) {
            resolveByMemberName(referencingElement, designatorString).also { resolveResults.addAll(it) }
        } else {
            val parts = designatorString.split('.')
            // TODO designator might also refer to record member
            val moduleName = referencingElement.parentOfType<OberonModuleDef>()
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
        val element = file.findChildByClass(OberonModuleDef::class.java)
            ?.topLevelDecls?.procedureDeclList?.firstOrNull()

        return element
    }


}

