package me.mrolappe.intellij.lang.oberon

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.ResolveResult

class OberonReference(element: PsiElement, textRange: TextRange) : PsiReferenceBase<PsiElement>(element, textRange),
    PsiPolyVariantReference {
    private val procName: String

    init {
        procName = element.text.substring(textRange.startOffset, textRange.endOffset)
        println("OberonReference.init, procName: $procName")
    }

    override fun resolve(): PsiElement? {
        val resolveResults = multiResolve(incompleteCode = false)

        return if (resolveResults.size == 1) resolveResults.first().element else null
    }

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> {
        val project = myElement.project
//        val procs = findProcs(project, procName)

//        return procs.map { PsiElementResolveResult(it) }.toTypedArray()
        TODO()
    }

    override fun getVariants(): Array<Any> {
        val project = myElement.project
        val procs = OberonUtil.findProcedures(project)

        return procs.map {
            LookupElementBuilder.create(it).withIcon(OberonIcons.MODULE).withTypeText(it.containingFile.name)
        }.toTypedArray()
    }
}