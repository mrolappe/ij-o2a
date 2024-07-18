package de.rholambdapi.o2a.intellij.lang.oberon2.template.postfix

import com.intellij.codeInsight.template.postfix.templates.PostfixTemplate
import com.intellij.openapi.editor.Document
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement

class IfPostfixTemplate: PostfixTemplate("if", "if", "dodaif", null) {
    override fun isApplicable(context: PsiElement, copyDocument: Document, newOffset: Int): Boolean {
        return true
    }

    override fun expand(context: PsiElement, editor: Editor) {
        editor.document.insertString(editor.caretModel.offset, "${context.toString()}")
    }
}