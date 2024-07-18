package de.rholambdapi.o2a.intellij.lang.oberon2.template.postfix

import com.intellij.codeInsight.template.postfix.templates.PostfixTemplate
import com.intellij.codeInsight.template.postfix.templates.PostfixTemplateProvider
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiFile

class Oberon2PostfixTemplateProvider: PostfixTemplateProvider {
    override fun getTemplates(): MutableSet<PostfixTemplate> {
        return mutableSetOf(IfPostfixTemplate())
    }

    override fun isTerminalSymbol(currentChar: Char): Boolean {
        return currentChar == '.' || currentChar == '!'
    }

    override fun preExpand(file: PsiFile, editor: Editor) {
    }

    override fun afterExpand(file: PsiFile, editor: Editor) {
    }

    override fun preCheck(copyFile: PsiFile, realEditor: Editor, currentOffset: Int): PsiFile {
        return copyFile
    }
}