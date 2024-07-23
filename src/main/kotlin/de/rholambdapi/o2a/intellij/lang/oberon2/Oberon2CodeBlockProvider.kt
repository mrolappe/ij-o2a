package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.codeInsight.editorActions.CodeBlockProvider
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiFile

class Oberon2CodeBlockProvider : CodeBlockProvider {
    override fun getCodeBlockRange(editor: Editor?, psiFile: PsiFile?): TextRange? {
        val caretOffset = editor?.caretModel?.offset ?: return null
        val elementAtCaret = psiFile?.findElementAt(caretOffset) ?: return null
        val codeBlock = nearestSurroundingCodeBlockElement(elementAtCaret) ?: return null
        val blockRange = codeBlock.textRange

        return blockRange
    }
}
