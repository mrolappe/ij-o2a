package me.mrolappe.intellij.lang.oberon

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.tree.IElementType
import com.intellij.psi.tree.TokenSet
import me.mrolappe.intellij.lang.oberon.psi.OberonTypes
import me.mrolappe.intellij.lang.oberon.psi.OberonTypes.*

class OberonSyntaxHighlighter : SyntaxHighlighterBase() {
    companion object {
        //    private static final TextAttributesKey[] VALUE_KEYS = new TextAttributesKey[]{VALUE};
        //    private static final TextAttributesKey[] COMMENT_KEYS = new TextAttributesKey[]{COMMENT};
        private val EMPTY_KEYS = arrayOfNulls<TextAttributesKey>(0)

        private val KEYWORD_TOKENS = TokenSet.create(
            ARRAY,
            BEGIN, BY,
            CASE, CONST,
            DIV, DO,
            ELSE, ELSIF, END, EXIT,
            FOR,
            IF, IMPORT, IN, IS,
            LOOP,
            MOD, MODULE,
            NIL,
            OF, OR,
            POINTER, PROCEDURE,
            RECORD, REPEAT, RETURN,
            THEN, TO, TYPE,
            UNTIL,
            VAR,
            WHILE, WITH,
        )


        private val AMIGA_OBERON_KEYWORD_TOKENS = TokenSet.create(
            AND,    // KW in AmigaOberon, predefined identifier in Oberon-A
            BPOINTER,
            CLOSE /* Amiga Oberon */,
            NOT,
            STRUCT,
            UNTRACED
        )


        private val RELOP_TOKENS = TokenSet.create(
            EQUALS,
            GREATER,
            GREATER_EQUAL,
            LESS,
            LESS_EQUAL,
            HASH
        )

        private val ATTRIBUTES: MutableMap<IElementType, TextAttributesKey?> = HashMap()

        init {
            fillMap(ATTRIBUTES, KEYWORD_TOKENS, OberonSyntaxHighlighterColors.KEYWORD)
            fillMap(ATTRIBUTES, AMIGA_OBERON_KEYWORD_TOKENS, OberonSyntaxHighlighterColors.KEYWORD)
            fillMap(ATTRIBUTES, RELOP_TOKENS, OberonSyntaxHighlighterColors.OPERATION_SIGN)

            ATTRIBUTES[IDENT] = OberonSyntaxHighlighterColors.IDENTIFIER
            ATTRIBUTES[STR_LIT] = OberonSyntaxHighlighterColors.STRING
            ATTRIBUTES[COMMENT] = OberonSyntaxHighlighterColors.BLOCK_COMMENT
            ATTRIBUTES[LPAREN] = OberonSyntaxHighlighterColors.PARENTHESES
            ATTRIBUTES[RPAREN] = OberonSyntaxHighlighterColors.PARENTHESES
            ATTRIBUTES[LBRACE] = OberonSyntaxHighlighterColors.BRACES
            ATTRIBUTES[RBRACE] = OberonSyntaxHighlighterColors.BRACES
            ATTRIBUTES[LBRACK] = OberonSyntaxHighlighterColors.BRACKETS
            ATTRIBUTES[RBRACK] = OberonSyntaxHighlighterColors.BRACKETS
            ATTRIBUTES[INT_LIT] = OberonSyntaxHighlighterColors.NUMBER
            ATTRIBUTES[REAL_LIT] = OberonSyntaxHighlighterColors.NUMBER
            ATTRIBUTES[COLON_EQUALS] = OberonSyntaxHighlighterColors.OPERATION_SIGN
            ATTRIBUTES[OberonTypes.PROC_CALL_STMT] = OberonSyntaxHighlighterColors.FUNCTION_CALL
//        fillMap(ATTRIBUTES, ASSIGNMENT_OPERATORS, DartSyntaxHighlighterColors.OPERATION_SIGN);
//        fillMap(ATTRIBUTES, BINARY_OPERATORS, DartSyntaxHighlighterColors.OPERATION_SIGN);
//        fillMap(ATTRIBUTES, UNARY_OPERATORS, DartSyntaxHighlighterColors.OPERATION_SIGN);
//        // '?' from ternary operator; ':' is handled separately in dartColorAnnotator, because there are also ':' in other syntax constructs
//        ATTRIBUTES.put(QUEST, DartSyntaxHighlighterColors.OPERATION_SIGN);
//
//        ATTRIBUTES.put(COMMA, DartSyntaxHighlighterColors.COMMA);
//        ATTRIBUTES.put(DOT, DartSyntaxHighlighterColors.DOT);
//        ATTRIBUTES.put(DOT_DOT, DartSyntaxHighlighterColors.DOT);
//        ATTRIBUTES.put(QUEST_DOT_DOT, DartSyntaxHighlighterColors.DOT);
//        ATTRIBUTES.put(QUEST_DOT, DartSyntaxHighlighterColors.DOT);
//        ATTRIBUTES.put(SEMICOLON, DartSyntaxHighlighterColors.SEMICOLON);
//        ATTRIBUTES.put(COLON, DartSyntaxHighlighterColors.COLON);
//        ATTRIBUTES.put(EXPRESSION_BODY_DEF, DartSyntaxHighlighterColors.FAT_ARROW);
//
//        ATTRIBUTES.put(BAD_CHARACTER, DartSyntaxHighlighterColors.BAD_CHARACTER);
        }
    }

    override fun getHighlightingLexer(): Lexer = OberonLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> = pack(ATTRIBUTES[tokenType])
}