package me.mrolappe.intellij.lang.oberon.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.parentOfType
import me.mrolappe.intellij.lang.oberon.OberonElementFactory
import me.mrolappe.intellij.lang.oberon.logDebug
import me.mrolappe.intellij.lang.oberon.psi.*

class OberonDesignatorReference(private val referencingElement: OberonDesignator) :
    PsiReferenceBase<OberonDesignator>(referencingElement, referencingElement.textRangeInParent) {

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
            ?.procedureDeclList?.firstOrNull()
        return element
    }


}

