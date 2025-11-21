package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.formatting.*
import com.intellij.lang.ASTNode
import com.intellij.psi.TokenType
import com.intellij.psi.formatter.common.AbstractBlock
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonTypes

// TODO configurable: contract if-statements with one statement in each branch to one line

class OberonBlock(node: ASTNode, wrap: Wrap?, alignment: Alignment?, private val spacingBuilder: SpacingBuilder) :
    AbstractBlock(node, wrap, alignment) {

    override fun getIndent(): Indent? {
        val indent =
            when (node.elementType) {
                OberonTypes.VAR_SECTION ->
                    if (node.treeParent?.elementType == OberonTypes.PROCEDURE_DECL) Indent.getNormalIndent(true) else Indent.getNoneIndent()

                OberonTypes.VAR_DECL, OberonTypes.CONST_DECL -> Indent.getNormalIndent(true)

                OberonTypes.PROCEDURE_DECL ->
                    if (node.treeParent?.elementType == OberonTypes.PROCEDURE_DECL) Indent.getNormalIndent(true) else Indent.getNoneIndent()

                OberonTypes.RECORD_TYPE_SPEC -> Indent.getNoneIndent()

                OberonTypes.IMPORT_DECL, OberonTypes.TYPE_DECL -> Indent.getNormalIndent(true)

                OberonTypes.STMT_SEQ ->
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
                        spacingBuilder,
                        isTopLevel = true
                    )

                    child.elementType == OberonTypes.IMPORT_LIST -> {
                        ImportListBlock(
                            child,
                            Wrap.createWrap(WrapType.NONE, false),
                            Alignment.createAlignment(),
                            spacingBuilder
                        )
                    }

                    child.elementType == OberonTypes.VAR_SECTION -> {
                        VarSectionBlock(
                            child,
                            Wrap.createWrap(WrapType.NONE, false),
                            Alignment.createAlignment(),
                            spacingBuilder,
                            isTopLevel = true
                        )
                    }

                    child.elementType == OberonTypes.TYPE_SECTION -> {
                        TypeSectionBlock(
                            child,
                            Wrap.createWrap(WrapType.NONE, false),
                            Alignment.createAlignment(),
                            spacingBuilder,
                            isTopLevel = true
                        )
                    }

                    child.elementType == OberonTypes.CONST_SECTION -> {
                        ConstSectionBlock(
                            child,
                            Wrap.createWrap(WrapType.NONE, false),
                            Alignment.createAlignment(),
                            spacingBuilder,
                            isTopLevel = true
                        )
                    }

                    child.elementType == OberonTypes.MODULE_TAIL -> {
                        ModuleTailBlock(
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
    override fun getSpacing(child1: Block?, child2: Block): Spacing? {

        println("ImportListBlock::getSpacing, child1: $child1, child2: $child2")

        val tmpSpacingBuilder = spacingBuilder
            .between(OberonTypes.IMPORT, OberonTypes.IMPORT_DECL) .spaces(1)
            .between(OberonTypes.IDENT, OberonTypes.COMMA).none()
            .between(OberonTypes.COMMA, OberonTypes.IDENT).spaces(1)
            .between(OberonTypes.IMPORT_DECL, OberonTypes.COMMA).none()
            .between(OberonTypes.COMMA, OberonTypes.IMPORT_DECL).spaces(1)
            .around(OberonTypes.COLON_EQUALS).spaces(1)

        val spacing = tmpSpacingBuilder.getSpacing(this, child1, child2)
        return spacing
    }

    override fun isLeaf() = false
    override fun getIndent(): Indent = Indent.getAbsoluteNoneIndent()

    override fun buildChildren(): List<Block> {
        val children = mutableListOf<Block>()

        myNode.getChildren(null)
            .forEach { child ->
                println("ImportListBlock::buildChildren, child: $child")
                when (child.elementType) {
                    TokenType.WHITE_SPACE -> {} /* ignore */

                    OberonTypes.IMPORT_DECLS -> {
                        child.getChildren(null)
                            .mapIndexedNotNullTo(children) { idx, it ->
                                when (it.elementType) {
                                    TokenType.WHITE_SPACE -> null

                                    OberonTypes.IMPORT_DECL -> GenericBlock(
                                        it,
                                        Wrap.createWrap(if (idx == 0) WrapType.ALWAYS else WrapType.NORMAL, true),
                                        alignment,
                                        spacingBuilder
                                    )

                                    else -> GenericBlock(
                                        it,
                                        Wrap.createWrap(WrapType.NONE, false),
                                        alignment,
                                        spacingBuilder
                                    )
                                }
                            }
                    }

                    else -> children.add(GenericBlock(child, wrap, alignment, spacingBuilder))
                }
            }

        return children
    }
}

class ConstSectionBlock(
    node: ASTNode, wrap: Wrap?, alignment: Alignment?, private val spacingBuilder: SpacingBuilder,
    private val isTopLevel: Boolean
) : AbstractBlock(node, wrap, alignment) {

    override fun getSpacing(child1: Block?, child2: Block) = spacingBuilder.getSpacing(this, child1, child2)
    override fun isLeaf() = false
    override fun getIndent(): Indent? {
        return if (isTopLevel) Indent.getNoneIndent() else Indent.getNormalIndent()
    }

    override fun buildChildren(): List<Block> {
        val child = myNode.firstChildNode
        println("ConstSectionBlock::buildChildren, child: $child, node: ${this.node}")
        return emptyList()
    }
}

class VarSectionBlock(
    node: ASTNode, wrap: Wrap?, alignment: Alignment?, private val spacingBuilder: SpacingBuilder,
    private val isTopLevel: Boolean
) : AbstractBlock(node, wrap, alignment) {

    override fun getSpacing(child1: Block?, child2: Block) = spacingBuilder.getSpacing(this, child1, child2)
    override fun isLeaf() = false
    override fun getIndent(): Indent = if (isTopLevel) Indent.getNoneIndent() else Indent.getNormalIndent()

    override fun buildChildren(): List<Block> {
        val varDeclWrap = Wrap.createWrap(WrapType.ALWAYS, true)
        val varDeclAlignment = Alignment.createAlignment()

        return myNode.getChildren(null)
            .mapNotNull { child ->
                println("VarSectionBlock::buildChildren, child: $child, node: ${this.node}")

                when (child.elementType) {
                    TokenType.WHITE_SPACE -> null /* ignore */
                    OberonTypes.VAR_DECL -> {
                        GenericBlock(
                            child,
                            varDeclWrap,
                            null,
                            spacingBuilder,
                            hasChildren = true,
                            indent = Indent.getNormalIndent()
                        )
                    }

                    else -> GenericBlock(child, wrap, alignment, spacingBuilder)
                }
            }
            .toList()
    }
}

class TypeSectionBlock(
    node: ASTNode, wrap: Wrap?, alignment: Alignment?, private val spacingBuilder: SpacingBuilder,
    private val isTopLevel: Boolean
) : AbstractBlock(node, wrap, alignment) {

    override fun getSpacing(child1: Block?, child2: Block) = spacingBuilder.getSpacing(this, child1, child2)
    override fun isLeaf() = false
    override fun getIndent(): Indent = if (isTopLevel) Indent.getNoneIndent() else Indent.getNormalIndent()

    override fun buildChildren(): List<Block> {
        val child = myNode.firstChildNode
        println("TypeSectionBlock::buildChildren, child: $child, node: ${this.node}")
        return emptyList()
    }
}

class ProcedureBlock(
    node: ASTNode, wrap: Wrap?, alignment: Alignment?, private val spacingBuilder: SpacingBuilder,
    private val isTopLevel: Boolean
) : AbstractBlock(node, wrap, alignment) {

    override fun getSpacing(child1: Block?, child2: Block) = spacingBuilder.getSpacing(this, child1, child2)
    override fun isLeaf() = false
    override fun getIndent(): Indent = if (isTopLevel) Indent.getNoneIndent() else Indent.getNormalIndent()

    override fun buildChildren(): MutableList<Block> {
        val blocks = mutableListOf<Block>()
        var child = myNode.firstChildNode

        println("ProcedureBlock::buildChildren, child: $child, node: ${this.node}")

        /*
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
        */

        return blocks
    }

}

class ModuleTailBlock(node: ASTNode, wrap: Wrap?, alignment: Alignment?, private val spacingBuilder: SpacingBuilder) :
    AbstractBlock(node, wrap, alignment) {
    override fun getSpacing(child1: Block?, child2: Block) = spacingBuilder.getSpacing(this, child1, child2)

    override fun isLeaf() = false
    override fun getIndent(): Indent = Indent.getAbsoluteNoneIndent()

    override fun buildChildren(): List<Block> {
        return myNode.getChildren(null)
            .mapNotNull { child ->
                println("ModuleTailBlock::buildChildren, child: $child")
                when (child.elementType) {
                    TokenType.WHITE_SPACE -> null /* ignore */
                    else -> GenericBlock(child, wrap, alignment, spacingBuilder)
                }
            }
            .toList()
    }
}

class GenericBlock(
    node: ASTNode, wrap: Wrap?, alignment: Alignment?, private val spacingBuilder: SpacingBuilder,
    private val hasChildren: Boolean = true, private val indent: Indent? = null
) : AbstractBlock(node, wrap, alignment) {
    override fun getSpacing(child1: Block?, child2: Block): Spacing? {
        println("GenericBlock::getSpacing, child1: $child1, child2: $child2")
        val mySpacingBuilder = spacingBuilder
            .betweenInside(OberonTypes.IDENT, OberonTypes.COLON_EQUALS, OberonTypes.IMPORT_ALIAS).spaces(1)
            .betweenInside(OberonTypes.IMPORT_ALIAS, OberonTypes.IMPORT_MODULE_REFERENCE, OberonTypes.IMPORT_DECL).spaces(1)

        return mySpacingBuilder.getSpacing(this, child1, child2)
    }
    override fun isLeaf() = false
    override fun getIndent(): Indent? = indent

    override fun buildChildren(): List<Block> {
        if (!hasChildren) return emptyList()

        return myNode.getChildren(null)
            .mapNotNull { child ->
                when (child.elementType) {
                    OberonTypes.STMT_SEQ -> StmtSequenceBlock(child, wrap, alignment, spacingBuilder)
                    TokenType.WHITE_SPACE -> null /* ignore */
                    else -> GenericBlock(child, Wrap.createWrap(WrapType.NONE, false), alignment, spacingBuilder)
                }
            }
            .toList()
    }
}


class StmtBlock(node: ASTNode, wrap: Wrap?, alignment: Alignment?, private val spacingBuilder: SpacingBuilder) :
    AbstractBlock(node, wrap, alignment) {
    override fun getSpacing(child1: Block?, child2: Block) = spacingBuilder.getSpacing(this, child1, child2)

    override fun isLeaf() = false

    override fun buildChildren(): List<Block> {
        return myNode.getChildren(null)
            .mapNotNull { child ->
                when (child.elementType) {
                    OberonTypes.STMT -> StmtBlock(child, wrap, alignment, spacingBuilder)
                    TokenType.WHITE_SPACE -> null /* ignore */
                    else -> GenericBlock(child, wrap, alignment, spacingBuilder)
                }
            }
            .toList()
    }

}

class StmtSequenceBlock(node: ASTNode, wrap: Wrap?, alignment: Alignment?, private val spacingBuilder: SpacingBuilder) :
    AbstractBlock(node, wrap, alignment) {
    override fun getSpacing(child1: Block?, child2: Block) = spacingBuilder.getSpacing(this, child1, child2)

    override fun isLeaf() = false

    override fun buildChildren(): List<Block> {
        return myNode.getChildren(null)
            .mapNotNull { child ->
                when (child.elementType) {
                    OberonTypes.STMT -> StmtBlock(child, wrap, alignment, spacingBuilder)
                    TokenType.WHITE_SPACE -> null /* ignore */
                    else -> GenericBlock(child, wrap, alignment, spacingBuilder)
                }
            }
            .toList()
    }

    override fun getChildAttributes(newChildIndex: Int): ChildAttributes {
        val childAttributes = super.getChildAttributes(newChildIndex)
        println("StmtSequenceBlock::getChildAttributes, newChildIndex: $newChildIndex -> $childAttributes")
        return childAttributes
    }

    override fun getChildIndent(): Indent? {
        return Indent.getNormalIndent()
    }
}
