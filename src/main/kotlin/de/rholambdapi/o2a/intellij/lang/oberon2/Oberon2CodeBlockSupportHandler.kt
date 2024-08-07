package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.codeInsight.highlighting.CodeBlockSupportHandler
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import com.intellij.psi.util.*
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.*

private val codeBlockTypes = arrayOf(
    OberonIfStmt::class, OberonCaseStmt::class, OberonWhileStmt::class, OberonLoopStmt::class,
    OberonProcedureDecl::class, OberonModuleTail::class
)

internal fun nearestSurroundingCodeBlockElement(elementAtCursor: PsiElement) =
    elementAtCursor.parentOfTypes(*codeBlockTypes)

class Oberon2CodeBlockSupportHandler : CodeBlockSupportHandler {
    override fun getCodeBlockMarkerRanges(elementAtCursor: PsiElement): MutableList<TextRange> {
        val markerRanges = mutableListOf<TextRange>()

        when (elementAtCursor.elementType) {
            OberonTypes.END, OberonTypes.ELSE -> {
                when (nearestSurroundingCodeBlockElement(elementAtCursor)) {
                    is OberonCaseStmt -> markerRanges.addAll(markerRangesForSurroundingCase(elementAtCursor))
                    is OberonIfStmt -> markerRanges.addAll(markerRangesForSurroundingIf(elementAtCursor))
                    is OberonLoopStmt -> markerRanges.addAll(markerRangesForSurroundingLoop(elementAtCursor))
                    is OberonModuleTail -> markerRanges.addAll(markerRangesForModuleInit(elementAtCursor))
                    is OberonProcedureDecl -> markerRanges.addAll(markerRangesForSurroundingProcedure(elementAtCursor))
                    is OberonWhileStmt -> markerRanges.addAll(markerRangesForSurroundingWhile(elementAtCursor))
                }
            }

            OberonTypes.CASE, OberonTypes.OF ->
                markerRanges.addAll(markerRangesForSurroundingCase(elementAtCursor))

            OberonTypes.IF, OberonTypes.THEN, OberonTypes.ELSIF ->
                markerRanges.addAll(markerRangesForSurroundingIf(elementAtCursor))

            OberonTypes.LOOP ->
                markerRanges.addAll(markerRangesForSurroundingLoop(elementAtCursor))

            OberonTypes.PROCEDURE, OberonTypes.BEGIN -> {
                markerRanges.addAll(markerRangesForSurroundingProcedure(elementAtCursor))
                markerRanges.addAll(markerRangesForModuleInit(elementAtCursor))
            }

            OberonTypes.WHILE -> markerRanges.addAll(markerRangesForSurroundingWhile(elementAtCursor))
        }

        return markerRanges
    }

    private fun markerRangesForModuleInit(elementAtCursor: PsiElement): Collection<TextRange> {
        val markerRanges = mutableListOf<TextRange>()

        val moduleTail = elementAtCursor.parentOfType<OberonModuleTail>(withSelf = false) ?: return markerRanges
        moduleTail.descendants(childrenFirst = true) { true }
            .firstOrNull { it.elementType == OberonTypes.BEGIN }?.let { markerRanges.add(it.textRange) }
        moduleTail.endIdentifier?.prevLeaf { it.elementType == OberonTypes.END }?.let { markerRanges.add(it.textRange) }

        return markerRanges
    }


    private fun markerRangesForSurroundingLoop(elementAtCursor: PsiElement): List<TextRange> {
        val markerRanges = mutableListOf<TextRange>()

        elementAtCursor.parentOfType<OberonLoopStmt>()
            ?.descendants { it is OberonLoopStmt }
            ?.filter { d -> d.elementType == OberonTypes.LOOP || d.elementType == OberonTypes.END }
            ?.mapTo(markerRanges, PsiElement::getTextRange)

        return markerRanges
    }

    private val tokensToMarkInProcedureBlock = TokenSet.create(OberonTypes.BEGIN, OberonTypes.END)

    private fun markerRangesForSurroundingProcedure(elementAtCursor: PsiElement): MutableList<TextRange> {
        val markerRanges = mutableListOf<TextRange>()

        val procedureDecl = elementAtCursor.parentOfType<OberonProcedureDecl>() ?: return markerRanges
        procedureDecl.descendants(childrenFirst = true) { it == procedureDecl }
            .firstOrNull { it.elementType == OberonTypes.PROCEDURE }
            ?.let { markerRanges.add(it.textRange) }

        val bodyBlock = procedureDecl.procedureDeclBodyBlock ?: return markerRanges
        val procedureDeclBody = bodyBlock.procedureDeclBody ?: return markerRanges

        procedureDeclBody.descendants(childrenFirst = true) { it == procedureDeclBody }
            .firstOrNull() { it.elementType == OberonTypes.BEGIN }
            ?.let { markerRanges.add(it.textRange) }

        bodyBlock.procedureDeclTail?.firstChild
            ?.let { markerRanges.add(it.textRange) }

        return markerRanges
    }

    private val ifBlockTokens =
        TokenSet.create(OberonTypes.IF, OberonTypes.THEN, OberonTypes.ELSIF, OberonTypes.ELSE, OberonTypes.END)

    private fun markerRangesForSurroundingIf(elementAtCursor: PsiElement): MutableList<TextRange> {
        val markerRanges = mutableListOf<TextRange>()

        val ifStmt = elementAtCursor.parentOfType<OberonIfStmt>() ?: return markerRanges
        ifStmt.descendants {
            // descend only into this if statement or its elsif/else branches, not nested ones
            it == ifStmt || (it.parent == ifStmt) && (it is OberonElsifBranch || it is OberonElseBranch)
        }
            .filter { e -> ifBlockTokens.contains(e.elementType) }
            .mapTo(markerRanges, PsiElement::getTextRange)

        return markerRanges
    }

    private val whileBlockTokens =
        TokenSet.create(OberonTypes.WHILE, OberonTypes.DO, OberonTypes.END)

    private fun markerRangesForSurroundingWhile(elementAtCursor: PsiElement): MutableList<TextRange> {
        val markerRanges = mutableListOf<TextRange>()

        val whileStmt = elementAtCursor.parentOfType<OberonWhileStmt>() ?: return markerRanges
        whileStmt.descendants { d -> d is OberonWhileStmt }
            .filter { e -> whileBlockTokens.contains(e.elementType) }
            .mapTo(markerRanges, PsiElement::getTextRange)

        return markerRanges
    }

    private val caseBlockTokens = TokenSet.create(OberonTypes.CASE, OberonTypes.OF, OberonTypes.ELSE, OberonTypes.END)

    private fun markerRangesForSurroundingCase(elementAtCursor: PsiElement): MutableList<TextRange> {
        val markerRanges = mutableListOf<TextRange>()
        val caseStmt = elementAtCursor.parentOfType<OberonCaseStmt>() ?: return markerRanges

        caseStmt.descendants { d -> d is OberonCaseStmt }
            .filter { caseBlockTokens.contains(it.elementType) }
            .mapTo(markerRanges, PsiElement::getTextRange)

        return markerRanges
    }

    override fun getCodeBlockRange(elementAtCursor: PsiElement): TextRange {
        return nearestSurroundingCodeBlockElement(elementAtCursor)?.textRange ?: TextRange.EMPTY_RANGE
    }
}
