package me.mrolappe.intellij.lang.oberon;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonTypes.*;

%%

%{
  public _OberonLexer() {
    this((java.io.Reader)null);
  }
%}

%public
%class _OberonLexer
%implements FlexLexer
%function advance
%type IElementType
%unicode

EOL=\R
WHITE_SPACE=\s+

COMMENT=\(\*([^*]|\*[^)])*?\*\)
CHAR_CONST=[A-Fa-f0-99]+X
STR_LIT=\".*?\"
PSEUDO_COMMENT=<\*([^*]|\*[^>])*?\*>
WHITE_SPACE=[ \t\n\x0B\f\r]+
REAL_LIT=([0-9]+\.([0-9]+D))|([0-9]+\.[0-9]+([ED][\+\-]?[0-9]+)?)
IDENT=[A-Za-z_][A-Za-z_0-9]*
INT_LIT=([A-Fa-f0-9]+[HU]|[0-9]+)

%%
<YYINITIAL> {
  {WHITE_SPACE}         { return WHITE_SPACE; }

  "&"                   { return AMP; }
  "^"                   { return CARET; }
  ":"                   { return COLON; }
  ":="                  { return COLON_EQUALS; }
  ","                   { return COMMA; }
  "."                   { return DOT; }
  ".."                  { return DOT_DOT; }
  "="                   { return EQUALS; }
  ">"                   { return GREATER; }
  ">="                  { return GREATER_EQUAL; }
  "#"                   { return HASH; }
  "{"                   { return LBRACE; }
  "["                   { return LBRACK; }
  "("                   { return LPAREN; }
  "<"                   { return LESS; }
  "<="                  { return LESS_EQUAL; }
  "-"                   { return MINUS; }
  "|"                   { return PIPE; }
  "+"                   { return PLUS; }
  "}"                   { return RBRACE; }
  "]"                   { return RBRACK; }
  ")"                   { return RPAREN; }
  ";"                   { return SEMI; }
  "/"                   { return SLASH; }
  "*"                   { return STAR; }
  "~"                   { return TILDE; }
  "ARRAY"               { return ARRAY; }
  "BEGIN"               { return BEGIN; }
  "BY"                  { return BY; }
  "CASE"                { return CASE; }
  "CONST"               { return CONST; }
  "DIV"                 { return DIV; }
  "DO"                  { return DO; }
  "ELSE"                { return ELSE; }
  "ELSIF"               { return ELSIF; }
  "END"                 { return END; }
  "EXIT"                { return EXIT; }
  "FOR"                 { return FOR; }
  "IF"                  { return IF; }
  "IMPORT"              { return IMPORT; }
  "IN"                  { return IN; }
  "IS"                  { return IS; }
  "LOOP"                { return LOOP; }
  "MOD"                 { return MOD; }
  "MODULE"              { return MODULE; }
  "NIL"                 { return NIL; }
  "OF"                  { return OF; }
  "OR"                  { return OR; }
  "POINTER"             { return POINTER; }
  "PROCEDURE"           { return PROCEDURE; }
  "RECORD"              { return RECORD; }
  "REPEAT"              { return REPEAT; }
  "RETURN"              { return RETURN; }
  "THEN"                { return THEN; }
  "TO"                  { return TO; }
  "TYPE"                { return TYPE; }
  "UNTIL"               { return UNTIL; }
  "UNTRACED"            { return UNTRACED; }
  "VAR"                 { return VAR; }
  "WHILE"               { return WHILE; }
  "WITH"                { return WITH; }
  "AND"                 { return AND; }
  "BPOINTER"            { return BPOINTER; }
  "CLOSE"               { return CLOSE; }
  "NOT"                 { return NOT; }
  "STRUCT"              { return STRUCT; }

  {COMMENT}             { return COMMENT; }
  {CHAR_CONST}          { return CHAR_CONST; }
  {STR_LIT}             { return STR_LIT; }
  {PSEUDO_COMMENT}      { return PSEUDO_COMMENT; }
  {WHITE_SPACE}         { return WHITE_SPACE; }
  {REAL_LIT}            { return REAL_LIT; }
  {IDENT}               { return IDENT; }
  {INT_LIT}             { return INT_LIT; }

}

[^] { return BAD_CHARACTER; }
