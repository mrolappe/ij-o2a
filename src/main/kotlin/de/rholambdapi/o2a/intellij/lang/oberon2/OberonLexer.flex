package de.rholambdapi.o2a.intellij.lang.oberon2;

import com.intellij.lexer.FlexLexer;
import com.intellij.psi.tree.IElementType;

import static com.intellij.psi.TokenType.BAD_CHARACTER;
import static com.intellij.psi.TokenType.WHITE_SPACE;
import static de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonTypes.*;

%%

%{
  StringBuffer commentBuffer = new StringBuffer();
  int commentNest = 0;

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

//REAL_LIT_1=([0-9]+\.) | ([0-9]+\.[0-9]+([ED][\+\-][0-9]+)?)
CHAR_CONST=[A-Fa-f0-99]+X
STR_LIT=('  ~') | (\" ([^\"] | \\\")* \")
PSEUDO_COMMENT=<\*([^*]|\*[^>])*?\*>
//WHITE_SPACE=[ \t\n\x0B\f\r]+
REAL_LIT=([0-9]+\.([0-9]+D))|([0-9]+\.[0-9]+([ED][\+\-]?[0-9]+)?)
IDENT=[A-Za-z_][A-Za-z_0-9]*
INT_LIT=([A-Fa-f0-9]+[HU]|[0-9]+)
//COMMENT="(*" [^*] ~"*)"
COMMENT_START="(*"
COMMENT_END=\*+ \)

%state IN_COMMENT

%%
<YYINITIAL> {COMMENT_START} {
          ++commentNest;
          yybegin(IN_COMMENT);
          commentBuffer.setLength(0);
//          System.out.println("initial comment start, nest: " + commentNest);
      }

<IN_COMMENT> <<EOF>> {
          throw new Error("Unterminated block comment");
      }

<IN_COMMENT> {COMMENT_START}    {
          ++commentNest;
          commentBuffer.append(yytext());
//          System.out.println("start nested comment, nest: " + commentNest);
      }

<IN_COMMENT> {COMMENT_END}      {
          --commentNest;
//          System.out.println("end nested comment/, nest: " + commentNest);

          if (commentNest == 0) {
              yybegin(YYINITIAL);
//              System.out.println("end of outermost comment; text: " + commentBuffer.toString());
              return COMMENT;
          }
      }

//<IN_COMMENT> ([^\*\(] | \*[^\)] | \([^\*])+ {
<IN_COMMENT> [^*(]+ | \*+ [^)] | \( [^*] {
          CharSequence content = yytext();
          commentBuffer.append(content);
//          System.out.println("comment content: " + content);
      }

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

//  {REAL_LIT_1} / [^.]    { return REAL_LIT; }
  {CHAR_CONST}          { return CHAR_CONST; }
  {STR_LIT}             { return STR_LIT; }
  {PSEUDO_COMMENT}      { return PSEUDO_COMMENT; }
  {WHITE_SPACE}         { return WHITE_SPACE; }
  {REAL_LIT}            { return REAL_LIT; }
  {IDENT}               { return IDENT; }
  {INT_LIT}             { return INT_LIT; }

}

[^] { return BAD_CHARACTER; }
