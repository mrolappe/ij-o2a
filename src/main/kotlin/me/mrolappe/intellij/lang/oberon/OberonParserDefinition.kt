package me.mrolappe.intellij.lang.oberon

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.ParserDefinition.SpaceRequirements
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import me.mrolappe.intellij.lang.oberon.parser.OberonParser
import me.mrolappe.intellij.lang.oberon.psi.OberonFile
import me.mrolappe.intellij.lang.oberon.psi.OberonTypes

class OberonParserDefinition : ParserDefinition {
    override fun createLexer(project: Project): Lexer = OberonLexerAdapter()

    override fun createParser(project: Project): PsiParser = OberonParser()

    override fun getFileNodeType(): IFileElementType = FILE

    override fun getWhitespaceTokens(): TokenSet = WHITESPACE

    override fun getCommentTokens(): TokenSet = COMMENTS

    override fun getStringLiteralElements(): TokenSet = STRING_LITERALS

    override fun createElement(node: ASTNode): PsiElement = OberonTypes.Factory.createElement(node)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = OberonFile(viewProvider)

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode): SpaceRequirements =
        SpaceRequirements.MAY

    companion object {
        val WHITESPACE = TokenSet.create(TokenType.WHITE_SPACE)
        val COMMENTS = TokenSet.create(OberonTypes.COMMENT, OberonTypes.PSEUDO_COMMENT)
        val STRING_LITERALS = TokenSet.create(OberonTypes.STR_LIT)
        val FILE = IFileElementType(OberonLanguage.INSTANCE)
    }
}