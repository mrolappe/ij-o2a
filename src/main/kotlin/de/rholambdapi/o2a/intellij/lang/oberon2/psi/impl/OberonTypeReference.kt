package de.rholambdapi.o2a.intellij.lang.oberon2.psi.impl

import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReferenceBase

class OberonTypeReference(referencingElement: PsiElement, textRange: TextRange)
    : PsiReferenceBase<PsiElement>(referencingElement, textRange) {

    override fun resolve(): PsiElement? {
        val resolvedElement = moduleContaining(element)
            ?.let { module -> resolveTypeByNameInModule(element.text, module) }
        println("OberonTypeReference::resolve; resolvedElement: $resolvedElement")
        return resolvedElement
    }
}