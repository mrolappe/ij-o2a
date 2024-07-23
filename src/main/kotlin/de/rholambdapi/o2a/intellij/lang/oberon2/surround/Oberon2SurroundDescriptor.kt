package de.rholambdapi.o2a.intellij.lang.oberon2.surround

import com.intellij.lang.surroundWith.SurroundDescriptor
import com.intellij.lang.surroundWith.Surrounder
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import org.slf4j.LoggerFactory

class Oberon2SurroundDescriptor: SurroundDescriptor {
    private val log = LoggerFactory.getLogger(Oberon2SurroundDescriptor::class.java)

    override fun getElementsToSurround(file: PsiFile?, startOffset: Int, endOffset: Int): Array<PsiElement> {
        println("getElementsToSurround, file: $file, startOffset: $startOffset, endOffset: $endOffset")
        return file?.findElementAt(startOffset)?.let { arrayOf(it) } ?: emptyArray()
    }

    override fun getSurrounders(): Array<Surrounder> {
        println("getSurrounders")
        return arrayOf(TmpSurrounder())
    }

    class TmpSurrounder : Surrounder {
        override fun getTemplateDescription(): String {
            println("getTemplateDescription")
            return "TMP"
        }

        override fun isApplicable(elements: Array<out PsiElement>): Boolean {
            println("isApplicable")
            return true
        }

        override fun surroundElements(project: Project, editor: Editor, elements: Array<out PsiElement>): TextRange? {
            println("surroundElements, project: $project, editor: $editor, # elements: ${elements.size}, elements: ${elements.map { it.toString() }}")
            return null
        }

    }

    override fun isExclusive(): Boolean {
        return false
    }
}