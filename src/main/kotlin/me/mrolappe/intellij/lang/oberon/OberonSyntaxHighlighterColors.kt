package me.mrolappe.intellij.lang.oberon

import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey

object OberonSyntaxHighlighterColors {
    val IDENTIFIER = createTextAttributesKey("OBERON_IDENTIFIER", DefaultLanguageHighlighterColors.IDENTIFIER)
    val NUMBER = createTextAttributesKey("OBERON_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
    val KEYWORD = createTextAttributesKey("OBERON_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
    val STRING = createTextAttributesKey("OBERON_STRING", DefaultLanguageHighlighterColors.STRING)
    val BLOCK_COMMENT = createTextAttributesKey("OBERON_BLOCK_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT)
    val BAD_CHARACTER =
        createTextAttributesKey("OBERON_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)
    val OPERATION_SIGN = createTextAttributesKey("DEFAULT_OPERATION_SIGN")
    val BRACES = createTextAttributesKey("OBERON_BRACES", DefaultLanguageHighlighterColors.BRACES)
    val DOT = createTextAttributesKey("DEFAULT_DOT")
    val SEMICOLON = createTextAttributesKey("OBERON_SEMICOLON", DefaultLanguageHighlighterColors.SEMICOLON)
    val COMMA = createTextAttributesKey("DEFAULT_COMMA")
    val PARENTHESES = createTextAttributesKey("OBERON_PARENTHESES", DefaultLanguageHighlighterColors.PARENTHESES)
    val BRACKETS = createTextAttributesKey("OBERON_BRACKETS")
    val CONSTANT = createTextAttributesKey("OBERON_CONSTANT", DefaultLanguageHighlighterColors.CONSTANT)
    val LOCAL_VARIABLE = createTextAttributesKey("OBERON_LOCAL_VARIABLE", IDENTIFIER)
    val GLOBAL_VARIABLE = createTextAttributesKey("OBERON_GLOBAL_VARIABLE", IDENTIFIER)
    val FUNCTION_DECLARATION = createTextAttributesKey("OBERON_FUNCTION_DECLARATION", IDENTIFIER)
    val FUNCTION_CALL = createTextAttributesKey("OBERON_FUNCTION_CALL", DefaultLanguageHighlighterColors.FUNCTION_CALL)
    val PARAMETER = createTextAttributesKey("OBERON_PARAMETER", DefaultLanguageHighlighterColors.PARAMETER)
    val PREDEFINED_SYMBOL = createTextAttributesKey("OBERON_PREDEFINED_SYMBOL", DefaultLanguageHighlighterColors.PREDEFINED_SYMBOL)
    val PSEUDO_COMMENT = createTextAttributesKey("OBERON_PSEUDO_COMMENT", DefaultLanguageHighlighterColors.BLOCK_COMMENT)
}