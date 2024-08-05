package de.rholambdapi.o2a.intellij.lang.oberon2.refactoring

import com.intellij.openapi.util.TextRange
import com.intellij.psi.ElementManipulator
import com.intellij.psi.impl.source.tree.Factory
import com.intellij.psi.impl.source.tree.TreeElement
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonProcedureDeclTail
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonTypes

class ProcedureDeclTailManipulator : ElementManipulator<OberonProcedureDeclTail> {
    override fun handleContentChange(
        element: OberonProcedureDeclTail,
        range: TextRange,
        newContent: String?
    ): OberonProcedureDeclTail {
        println("ProcedureDeclTailManipulator::handleContentChange, element: $element, range: $range, newContent: $newContent")
        if (newContent == null) return element
        val endIdentifier = element.endIdentifier.node as TreeElement
        val newNode = Factory.createSingleLeafElement(OberonTypes.IDENT, newContent, null, element.manager)
        endIdentifier.treeParent.replaceChild(endIdentifier, newNode)
        return element
    }

    override fun handleContentChange(element: OberonProcedureDeclTail, newContent: String?): OberonProcedureDeclTail? {
        println("ProcedureDeclTailManipulator::handleContentChange, element: $element, newContent: $newContent")
        return element
    }

    override fun getRangeInElement(element: OberonProcedureDeclTail): TextRange {
        return element.endIdentifier.textRange
    }
}