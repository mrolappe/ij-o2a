package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonTypes
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

        println("buildChildren, child: $child, node: ${this.node}")

        while (child != null) {
//            println("loop, child: $child")

            if (child.elementType != TokenType.WHITE_SPACE) {
                val block = when {
                    child.elementType == OberonTypes.PROCEDURE_DECL -> ProcedureBlock(
                        child,
                        Wrap.createWrap(WrapType.NONE, false),
                        Alignment.createAlignment(),
                        spacingBuilder
                    )

                    child.elementType == IMPORT_LIST -> {
                        ImportListBlock(
                            child,
                            Wrap.createWrap(WrapType.NONE, false),
                            Alignment.createAlignment(),
                            spacingBuilder
                        )
                    }

                    child.elementType == VAR_SECTION -> {
                        VarSectionBlock(
                            child,
                            Wrap.createWrap(WrapType.NONE, false),
                            Alignment.createAlignment(),
                            spacingBuilder
                        )
                    }

                    child.elementType == TYPE_SECTION -> {
                        TypeSectionBlock(
                            child,
                            Wrap.createWrap(WrapType.NONE, false),
                            Alignment.createAlignment(),
                            spacingBuilder
                        )
                    }

                    child.elementType == CONST_SECTION -> {
                        ConstSectionBlock(
                            child,
                            Wrap.createWrap(WrapType.NONE, false),
                            Alignment.createAlignment(),
                            spacingBuilder
                        )
                    }

                    else -> OberonBlock(
                        child,
                        Wrap.createWrap(WrapType.NONE, false),
                        Alignment.createAlignment(),
                        spacingBuilder
                    )
                }

                blocks.add(block)
            }

            child = child.treeNext
        }

        return blocks
    }
}

class ImportListBlock(node: ASTNode, wrap: Wrap?, alignment: Alignment?, private val spacingBuilder: SpacingBuilder) :
    AbstractBlock(node, wrap, alignment) {
    override fun getSpacing(child1: Block?, child2: Block) = spacingBuilder.getSpacing(this, child1, child2)

    override fun isLeaf() = false

    override fun buildChildren(): List<Block> {
        val child = myNode.firstChildNode
        println("ImportListBlock::buildChildren, child: $child, node: ${this.node}")
        return emptyList()
    }
}

class ConstSectionBlock(node: ASTNode, wrap: Wrap?, alignment: Alignment?, private val spacingBuilder: SpacingBuilder) :
    AbstractBlock(node, wrap, alignment) {
    override fun getSpacing(child1: Block?, child2: Block) = spacingBuilder.getSpacing(this, child1, child2)

    override fun isLeaf() = false

    override fun buildChildren(): List<Block> {
        val child = myNode.firstChildNode
        println("ConstSectionBlock::buildChildren, child: $child, node: ${this.node}")
        return emptyList()
    }
}

class VarSectionBlock(node: ASTNode, wrap: Wrap?, alignment: Alignment?, private val spacingBuilder: SpacingBuilder) :
    AbstractBlock(node, wrap, alignment) {
    override fun getSpacing(child1: Block?, child2: Block) = spacingBuilder.getSpacing(this, child1, child2)

    override fun isLeaf() = false

    override fun buildChildren(): List<Block> {
        val child = myNode.firstChildNode
        println("VarSectionBlock::buildChildren, child: $child, node: ${this.node}")
        return emptyList()
    }
}

class TypeSectionBlock(node: ASTNode, wrap: Wrap?, alignment: Alignment?, private val spacingBuilder: SpacingBuilder) :
    AbstractBlock(node, wrap, alignment) {
    override fun getSpacing(child1: Block?, child2: Block) = spacingBuilder.getSpacing(this, child1, child2)

    override fun isLeaf() = false

    override fun buildChildren(): List<Block> {
        val child = myNode.firstChildNode
        println("TypeSectionBlock::buildChildren, child: $child, node: ${this.node}")
        return emptyList()
    }
}

class ProcedureBlock(node: ASTNode, wrap: Wrap?, alignment: Alignment?, private val spacingBuilder: SpacingBuilder) :
    AbstractBlock(node, wrap, alignment) {
    override fun getSpacing(child1: Block?, child2: Block) = spacingBuilder.getSpacing(this, child1, child2)

    override fun isLeaf() = false

    override fun buildChildren(): MutableList<Block> {
        val blocks = mutableListOf<Block>()
        var child = myNode.firstChildNode

        println("ProcedureBlock::buildChildren, child: $child, node: ${this.node}")

        while (child != null) {
//            println("loop, child: $child")

            if (child.elementType != TokenType.WHITE_SPACE) {
                when {
                    child.elementType == OberonTypes.PROCEDURE_DECL -> {
                        val block = ProcedureBlock(
                            child,
                            Wrap.createWrap(WrapType.NONE, false),
                            Alignment.createAlignment(),
                            spacingBuilder
                        )
                        blocks.add(block)
                    }

                }

            }

            child = child.treeNext
        }

        return blocks
    }

}
