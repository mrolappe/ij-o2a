package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.TokenType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.elementType
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonTypes.*
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.impl.procedureName

/**
 * Folds complete procedure definitions
 */
class OberonProcedureFoldingBuilder : FoldingBuilderEx(), DumbAware {
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean) =
        PsiTreeUtil.findChildrenOfType(root, de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonProcedureDecl::class.java)
            .mapNotNull { FoldingDescriptor(it.node, it.textRange) }
            .toTypedArray()

    override fun getPlaceholderText(node: ASTNode): String {
        val element = node.psi

        return if (element is de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonProcedureDecl) {
            val receiverTypeName = element.receiverType?.text
            return if (receiverTypeName != null) "procedure $receiverTypeName.${element.procedureName}" else "procedure ${element.procedureName}"
        } else {
            "???"
        }
    }

    override fun isCollapsedByDefault(node: ASTNode) = true

}

/**
 * Folds the import section of a module
 */
class OberonImportSectionFoldingBuilder : FoldingBuilderEx(), DumbAware {
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> =
        PsiTreeUtil.findChildrenOfType(root, de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonImportList::class.java)
            .mapNotNull { FoldingDescriptor(it.node, it.textRange) }
            .toTypedArray()

    override fun getPlaceholderText(node: ASTNode) = "IMPORT section"

    override fun isCollapsedByDefault(node: ASTNode) = true
}

/**
 * Folds the variable section of a module
 */
class OberonVarSectionFoldingBuilder : FoldingBuilderEx(), DumbAware {
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean) =
        PsiTreeUtil.findChildrenOfType(root, de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonVarSection::class.java)
            .mapNotNull { FoldingDescriptor(it.node, it.textRange) }
            .toTypedArray()

    override fun getPlaceholderText(node: ASTNode) = "VAR section"

    override fun isCollapsedByDefault(node: ASTNode) = true
}

/**
 * Folds the header comment (i.e. comment at beginning of the file) of a module
 */
class OberonHeaderCommentFoldingBuilder : FoldingBuilderEx(), DumbAware {
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
//        println("OberonHeaderCommentFoldingBuilder, root: $root, quick: $quick")

        val headerElements =
            root.children.takeWhile { it.elementType == COMMENT || it.elementType == TokenType.WHITE_SPACE }

        if (headerElements.isEmpty()) {
            return FoldingDescriptor.EMPTY_ARRAY
        }

        val endOffset = when (headerElements.last().elementType) {
            TokenType.WHITE_SPACE -> headerElements.get(headerElements.lastIndex - 1).endOffset
            else -> headerElements.last().endOffset
        }

        val headerTextRange = TextRange(headerElements.first().startOffset, endOffset)

        return arrayOf(FoldingDescriptor(root.firstChild, headerTextRange))
    }

    override fun getPlaceholderText(node: ASTNode) = "(* ... *)"

    override fun isCollapsedByDefault(node: ASTNode): Boolean {
//        println("collapsed by default, node: $node -> ${node.treePrev == null}")
        return node.treePrev == null
    }
}

class OberonConstSectionFoldingBuilder : FoldingBuilderEx(), DumbAware {
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val constSections: Collection<de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonConstSection> = findConstSections(root)
        return constSections
            .map { FoldingDescriptor(it.node, textRangeCoveringElementAndEndingAtNextNewLine(it)) }
            .toTypedArray()
    }

    private fun findConstSections(root: PsiElement): Collection<de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonConstSection> =
        PsiTreeUtil.findChildrenOfType(root, de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonConstSection::class.java)

    override fun getPlaceholderText(node: ASTNode) = "CONST section"

    override fun isCollapsedByDefault(node: ASTNode) = true
}

class OberonTypeSectionFoldingBuilder : FoldingBuilderEx(), DumbAware {
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val typeSections: Collection<de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonTypeSection> = findTypeSections(root)
        return typeSections
            .map { section -> FoldingDescriptor(section.node, textRangeCoveringElementAndEndingAtNextNewLine(section)) }
            .toTypedArray()
    }

    private fun findTypeSections(root: PsiElement): Collection<de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonTypeSection> =
        PsiTreeUtil.findChildrenOfType(root, de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonTypeSection::class.java)

    override fun getPlaceholderText(node: ASTNode) = "TYPE section"

    override fun isCollapsedByDefault(node: ASTNode) = true

}

private fun textRangeCoveringElementAndEndingAtNextNewLine(element: PsiElement): TextRange {
    val trailingNewLine = findNextSiblingWithLineBreak(element)

    return if (trailingNewLine != null) {
        val endOffset = trailingNewLine.startOffset + trailingNewLine.text.indexOfAny("\n\r".toCharArray())
        TextRange(element.startOffset, endOffset)
    } else {
        element.textRange
    }
}

private fun findNextSiblingWithLineBreak(element: PsiElement): PsiElement? {
    var current = PsiTreeUtil.getNextSiblingOfType(element, PsiWhiteSpace::class.java)

    while (current != null) {
        if (current.textContains('\n') || current.textContains('\r')) {
            return current
        } else {
            current = PsiTreeUtil.getNextSiblingOfType(current, PsiWhiteSpace::class.java)
        }
    }

    return null
}

class OberonIfFoldingBuilder : FoldingBuilderEx(), DumbAware {
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        return PsiTreeUtil.findChildrenOfType(root, de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonIfStmt::class.java)
            .map { toFoldRegion(it)}
            .toTypedArray()
    }

    private fun toFoldRegion(stmt: de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonIfStmt): FoldingDescriptor {
        return when {
            // TODO include trailing semicolon and maybe even rest of last line
            stmt.parent is de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonStmt && stmt.parent.nextSibling.elementType == SEMI -> FoldingDescriptor(stmt, TextRange(stmt.startOffset, stmt.parent.nextSibling.endOffset))
            else -> FoldingDescriptor(stmt, stmt.textRange)
        }
    }

    override fun getPlaceholderText(node: ASTNode): String {
        return when (node.elementType) {
            IF_STMT -> "IF ${node.findChildByType(EXPR)?.text} ..."
            else -> "???"
        }
    }

    override fun isCollapsedByDefault(node: ASTNode) = false
}

class OberonCaseFoldingBuilder : FoldingBuilderEx(), DumbAware {
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        return PsiTreeUtil.findChildrenOfType(root, de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonCaseStmt::class.java)
            .flatMap { toFoldingDescriptors(it) }
            .toTypedArray()
    }

    private fun toFoldingDescriptors(caseStmt: de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonCaseStmt): List<FoldingDescriptor> {
        val caseArmRegions = caseStmt.caseArmList
            .filter {

                !it.textRange.isEmpty
            }
            .map { FoldingDescriptor(it, it.textRange) }

        return caseArmRegions + FoldingDescriptor(caseStmt, caseStmt.textRange)
    }

    override fun getPlaceholderText(node: ASTNode): String {
        return when (node.elementType) {
            CASE_STMT -> "CASE ${node.findChildByType(EXPR)?.text} OF ..."
            CASE_ARM -> "${node.findChildByType(CASE_LABEL_LIST)?.text}: ..."
            else -> "???"
        }
    }

    override fun isCollapsedByDefault(node: ASTNode) = false

}

class OberonLoopFoldingBuilder : FoldingBuilderEx(), DumbAware {
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        return PsiTreeUtil.findChildrenOfType(root, de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonLoopStmt::class.java)
            .map { FoldingDescriptor(it, it.textRange) }
            .toTypedArray()
    }

    override fun getPlaceholderText(node: ASTNode) = "LOOP ..."

    override fun isCollapsedByDefault(node: ASTNode) = false
}

class OberonKeywordFoldingBuilder : FoldingBuilderEx(), DumbAware {
    @OptIn(ExperimentalStdlibApi::class)
    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> {
        val procedureDecls = PsiTreeUtil.findChildrenOfType(root, de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonProcedureDecl::class.java)
        val procNodes = procedureDecls.mapNotNull { it.firstChild }

        val keywordNodes = emptyList<PsiElement>()
// TODO            procedureDecls.flatMap { it.collectDescendantsOfType<PsiElement> { it.node.elementType == OberonTypes.BEGIN || it.node.elementType == OberonTypes.END } }

        return buildList<PsiElement> {
            addAll(procNodes)
            addAll(keywordNodes)
        }
            .map { FoldingDescriptor(it.node, it.textRange) }
            .toTypedArray()
    }

    override fun getPlaceholderText(node: ASTNode): String = when (node.text) {
        "PROCEDURE" -> "proc"
        "BEGIN" -> "{"
        "END" -> "}"
        else -> node.text
    }

    override fun isCollapsedByDefault(node: ASTNode): Boolean = true
}