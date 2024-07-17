package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonTypes.*

// TODO configurable: contract if-statements with one statement in each branch to one line

class OberonBlock(node: ASTNode, wrap: Wrap?, alignment: Alignment?, private val spacingBuilder: SpacingBuilder) :
    AbstractBlock(node, wrap, alignment) {

    override fun getIndent(): Indent? {
        val indent =
            when (node.elementType) {
                VAR_SECTION ->
                    if (node.treeParent?.elementType == PROCEDURE_DECL) Indent.getNormalIndent(true) else Indent.getNoneIndent()

                VAR_DECL, CONST_DECL -> Indent.getNormalIndent(true)

                PROCEDURE_DECL ->
                    if (node.treeParent?.elementType == PROCEDURE_DECL) Indent.getNormalIndent(true) else Indent.getNoneIndent()

                RECORD_TYPE_SPEC -> Indent.getNoneIndent()

                IMPORT_DECL, TYPE_DECL -> Indent.getNormalIndent(true)

                STMT_SEQ ->
                    Indent.getNormalIndent(true)

                else -> Indent.getNoneIndent()
            }

//        println("OberonBlock, getIndent, elemType: ${node.elementType} -> indent $indent, parent: ${node.treeParent}")

        return indent
    }

    override fun getSpacing(child1: Block?, child2: Block) = spacingBuilder.getSpacing(this, child1, child2)

    override fun isLeaf() = myNode.firstChildNode == null

    override fun buildChildren(): MutableList<Block> {
        val blocks = mutableListOf<Block>()
        var child = myNode.firstChildNode

//        println("buildChildren, child: $child, node: ${this.node}")

        while (child != null) {
//            println("loop, child: $child")

            if (child.elementType != TokenType.WHITE_SPACE) {
                val block = OberonBlock(
                    child,
                    Wrap.createWrap(WrapType.NONE, false),
                    Alignment.createAlignment(),
                    spacingBuilder
                )

                blocks.add(block)
            }

            child = child.treeNext
        }

        return blocks
    }
}