package me.mrolappe.intellij.lang.oberon.psi.impl

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase
import me.mrolappe.intellij.lang.oberon.psi.OberonOberonASharedLibLvData

class OberonSharedLibBaseVarReference(private val referencingElement: PsiElement, private val baseVarName: String, baseVarNameTextRangeInParent: TextRange) :
    PsiReferenceBase<PsiElement>(referencingElement, baseVarNameTextRangeInParent /*referencingElement.baseVarName.textRangeInParent*/) {

    val LOG = thisLogger()

    override fun resolve(): PsiElement? {
        val module = moduleContaining(referencingElement)
//        val baseVarName = referencingElement.baseVarName.text
        val resolvedElement = module?.let {
            topLevelVarDeclNames(it).firstOrNull { varDeclName -> varDeclName.variableNameMatches(baseVarName) }
        }

        LOG.debug("OberonSharedLibBaseVarReference::resolve, module: $module, base var name: $baseVarName, referencingElement: $referencingElement -> $resolvedElement")
        return resolvedElement
    }
}
