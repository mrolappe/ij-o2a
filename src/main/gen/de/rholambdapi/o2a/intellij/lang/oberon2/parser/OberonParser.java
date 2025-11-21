// This is a generated file. Not intended for manual editing.
package de.rholambdapi.o2a.intellij.lang.oberon2.parser;

import com.intellij.lang.PsiBuilder;
import com.intellij.lang.PsiBuilder.Marker;
import static de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonTypes.*;
import static de.rholambdapi.o2a.intellij.lang.oberon2.parser.OberonParserUtilKt.*;
import com.intellij.psi.tree.IElementType;
import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.TokenSet;
import com.intellij.lang.PsiParser;
import com.intellij.lang.LightPsiParser;

@SuppressWarnings({"SimplifiableIfStatement", "UnusedAssignment"})
public class OberonParser implements PsiParser, LightPsiParser {

  public ASTNode parse(IElementType t, PsiBuilder b) {
    parseLight(t, b);
    return b.getTreeBuilt();
  }

  public void parseLight(IElementType t, PsiBuilder b) {
    boolean r;
    b = adapt_builder_(t, b, this, EXTENDS_SETS_);
    Marker m = enter_section_(b, 0, _COLLAPSE_, null);
    r = parse_root_(t, b);
    exit_section_(b, 0, m, t, r, true, TRUE_CONDITION);
  }

  protected boolean parse_root_(IElementType t, PsiBuilder b) {
    return parse_root_(t, b, 0);
  }

  static boolean parse_root_(IElementType t, PsiBuilder b, int l) {
    return file(b, l + 1);
  }

  public static final TokenSet[] EXTENDS_SETS_ = new TokenSet[] {
    create_token_set_(PROC_DECL_NAME, PROC_DECL_NAME_NO_EXP_MARK),
  };

  /* ********************************************************** */
  // '(' [ expr_list ] ')'
  public static boolean actual_params(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "actual_params")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && actual_params_1(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, ACTUAL_PARAMS, r);
    return r;
  }

  // [ expr_list ]
  private static boolean actual_params_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "actual_params_1")) return false;
    expr_list(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '+' | '-' | 'OR'
  public static boolean add_op(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "add_op")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, ADD_OP, "<add op>");
    r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    if (!r) r = consumeToken(b, OR);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'PROCEDURE' ident_def '{' STR_LIT '}' [ formal_params ] ';'
  public static boolean amiga_oberon_external_proc_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "amiga_oberon_external_proc_decl")) return false;
    if (!nextTokenIs(b, PROCEDURE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PROCEDURE);
    r = r && ident_def(b, l + 1);
    r = r && consumeTokens(b, 0, LBRACE, STR_LIT, RBRACE);
    r = r && amiga_oberon_external_proc_decl_5(b, l + 1);
    r = r && consumeToken(b, SEMI);
    exit_section_(b, m, AMIGA_OBERON_EXTERNAL_PROC_DECL, r);
    return r;
  }

  // [ formal_params ]
  private static boolean amiga_oberon_external_proc_decl_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "amiga_oberon_external_proc_decl_5")) return false;
    formal_params(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // '{' INT_LIT '}'
  public static boolean amiga_oberon_param_reg_spec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "amiga_oberon_param_reg_spec")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LBRACE, INT_LIT, RBRACE);
    exit_section_(b, m, AMIGA_OBERON_PARAM_REG_SPEC, r);
    return r;
  }

  /* ********************************************************** */
  // '{' IDENT ',' [ '-' ] INT_LIT '}'
  public static boolean amiga_oberon_shared_lib_lv_data(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "amiga_oberon_shared_lib_lv_data")) return false;
    if (!nextTokenIs(b, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LBRACE, IDENT, COMMA);
    r = r && amiga_oberon_shared_lib_lv_data_3(b, l + 1);
    r = r && consumeTokens(b, 0, INT_LIT, RBRACE);
    exit_section_(b, m, AMIGA_OBERON_SHARED_LIB_LV_DATA, r);
    return r;
  }

  // [ '-' ]
  private static boolean amiga_oberon_shared_lib_lv_data_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "amiga_oberon_shared_lib_lv_data_3")) return false;
    consumeToken(b, MINUS);
    return true;
  }

  /* ********************************************************** */
  // 'PROCEDURE' ident_def
  //     amiga_oberon_shared_lib_lv_data
  //     [ formal_params ] ';'
  public static boolean amiga_oberon_shared_lib_proc_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "amiga_oberon_shared_lib_proc_decl")) return false;
    if (!nextTokenIs(b, PROCEDURE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PROCEDURE);
    r = r && ident_def(b, l + 1);
    r = r && amiga_oberon_shared_lib_lv_data(b, l + 1);
    r = r && amiga_oberon_shared_lib_proc_decl_3(b, l + 1);
    r = r && consumeToken(b, SEMI);
    exit_section_(b, m, AMIGA_OBERON_SHARED_LIB_PROC_DECL, r);
    return r;
  }

  // [ formal_params ]
  private static boolean amiga_oberon_shared_lib_proc_decl_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "amiga_oberon_shared_lib_proc_decl_3")) return false;
    formal_params(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'ARRAY' [ const_expr ( ',' const_expr )* ] 'OF' type_spec
  public static boolean array_type_spec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_type_spec")) return false;
    if (!nextTokenIs(b, ARRAY)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ARRAY);
    r = r && array_type_spec_1(b, l + 1);
    r = r && consumeToken(b, OF);
    r = r && type_spec(b, l + 1);
    exit_section_(b, m, ARRAY_TYPE_SPEC, r);
    return r;
  }

  // [ const_expr ( ',' const_expr )* ]
  private static boolean array_type_spec_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_type_spec_1")) return false;
    array_type_spec_1_0(b, l + 1);
    return true;
  }

  // const_expr ( ',' const_expr )*
  private static boolean array_type_spec_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_type_spec_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = const_expr(b, l + 1);
    r = r && array_type_spec_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' const_expr )*
  private static boolean array_type_spec_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_type_spec_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!array_type_spec_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "array_type_spec_1_0_1", c)) break;
    }
    return true;
  }

  // ',' const_expr
  private static boolean array_type_spec_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "array_type_spec_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && const_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // designator ':=' expr
  public static boolean assign_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "assign_stmt")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ASSIGN_STMT, null);
    r = designator(b, l + 1);
    r = r && consumeToken(b, COLON_EQUALS);
    p = r; // pin = 2
    r = r && expr(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // [ case_label_list  ':' stmt_seq? ]
  public static boolean case_arm(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_arm")) return false;
    Marker m = enter_section_(b, l, _NONE_, CASE_ARM, "<case arm>");
    case_arm_0(b, l + 1);
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  // case_label_list  ':' stmt_seq?
  private static boolean case_arm_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_arm_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = case_label_list(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && case_arm_0_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // stmt_seq?
  private static boolean case_arm_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_arm_0_2")) return false;
    stmt_seq(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // case_labels ( ',' case_labels )*
  public static boolean case_label_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_label_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CASE_LABEL_LIST, "<case label list>");
    r = case_labels(b, l + 1);
    r = r && case_label_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( ',' case_labels )*
  private static boolean case_label_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_label_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!case_label_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "case_label_list_1", c)) break;
    }
    return true;
  }

  // ',' case_labels
  private static boolean case_label_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_label_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && case_labels(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // const_expr [ '..' const_expr ]
  public static boolean case_labels(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_labels")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CASE_LABELS, "<case labels>");
    r = const_expr(b, l + 1);
    r = r && case_labels_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ '..' const_expr ]
  private static boolean case_labels_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_labels_1")) return false;
    case_labels_1_0(b, l + 1);
    return true;
  }

  // '..' const_expr
  private static boolean case_labels_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_labels_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOT_DOT);
    r = r && const_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'CASE' expr 'OF'
  //         case_arm
  //         ( '|' case_arm )*
  //         [ 'ELSE' [ stmt_seq ] ]
  //     'END'
  public static boolean case_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_stmt")) return false;
    if (!nextTokenIs(b, CASE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CASE_STMT, null);
    r = consumeToken(b, CASE);
    p = r; // pin = 1
    r = r && report_error_(b, expr(b, l + 1));
    r = p && report_error_(b, consumeToken(b, OF)) && r;
    r = p && report_error_(b, case_arm(b, l + 1)) && r;
    r = p && report_error_(b, case_stmt_4(b, l + 1)) && r;
    r = p && report_error_(b, case_stmt_5(b, l + 1)) && r;
    r = p && consumeToken(b, END) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ( '|' case_arm )*
  private static boolean case_stmt_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_stmt_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!case_stmt_4_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "case_stmt_4", c)) break;
    }
    return true;
  }

  // '|' case_arm
  private static boolean case_stmt_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_stmt_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PIPE);
    r = r && case_arm(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ 'ELSE' [ stmt_seq ] ]
  private static boolean case_stmt_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_stmt_5")) return false;
    case_stmt_5_0(b, l + 1);
    return true;
  }

  // 'ELSE' [ stmt_seq ]
  private static boolean case_stmt_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_stmt_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ELSE);
    r = r && case_stmt_5_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ stmt_seq ]
  private static boolean case_stmt_5_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "case_stmt_5_0_1")) return false;
    stmt_seq(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // const_decl_name '=' const_expr ';'
  public static boolean const_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "const_decl")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = const_decl_name(b, l + 1);
    r = r && consumeToken(b, EQUALS);
    r = r && const_expr(b, l + 1);
    r = r && consumeToken(b, SEMI);
    exit_section_(b, m, CONST_DECL, r);
    return r;
  }

  /* ********************************************************** */
  // IDENT
  //     [ export_mark ]
  public static boolean const_decl_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "const_decl_name")) return false;
    if (!nextTokenIs(b, "<constant declaration>", IDENT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONST_DECL_NAME, "<constant declaration>");
    r = consumeToken(b, IDENT);
    p = r; // pin = 1
    r = r && const_decl_name_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ export_mark ]
  private static boolean const_decl_name_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "const_decl_name_1")) return false;
    export_mark(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // expr
  public static boolean const_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "const_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, CONST_EXPR, "<const expr>");
    r = expr(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'CONST' ( const_decl  )*
  public static boolean const_section(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "const_section")) return false;
    if (!nextTokenIs(b, CONST)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, CONST_SECTION, null);
    r = consumeToken(b, CONST);
    p = r; // pin = 1
    r = r && const_section_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ( const_decl  )*
  private static boolean const_section_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "const_section_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!const_section_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "const_section_1", c)) break;
    }
    return true;
  }

  // ( const_decl  )
  private static boolean const_section_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "const_section_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = const_decl(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // const_section
  //         | type_section
  //         | var_section
  //         | forward_decl ';'
  //         // Oberon-A shared library procedure
  //         | oberon_a_lib_proc_decl
  //         // Oberon-A external procedure
  //         | oberon_a_external_proc_decl
  //         | amiga_oberon_shared_lib_proc_decl
  //         | amiga_oberon_external_proc_decl
  //         | procedure_decl
  static boolean decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "decl")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = const_section(b, l + 1);
    if (!r) r = type_section(b, l + 1);
    if (!r) r = var_section(b, l + 1);
    if (!r) r = decl_3(b, l + 1);
    if (!r) r = oberon_a_lib_proc_decl(b, l + 1);
    if (!r) r = oberon_a_external_proc_decl(b, l + 1);
    if (!r) r = amiga_oberon_shared_lib_proc_decl(b, l + 1);
    if (!r) r = amiga_oberon_external_proc_decl(b, l + 1);
    if (!r) r = procedure_decl(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // forward_decl ';'
  private static boolean decl_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "decl_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = forward_decl(b, l + 1);
    r = r && consumeToken(b, SEMI);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // qual_ident (
  //         '.' IDENT
  //         | '[' expr_list ']'
  //         | '(' qual_ident ')'    // type guard
  //         | '^'
  //     )*
  public static boolean designator(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "designator")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, DESIGNATOR, null);
    r = qual_ident(b, l + 1);
    p = r; // pin = 1
    r = r && designator_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // (
  //         '.' IDENT
  //         | '[' expr_list ']'
  //         | '(' qual_ident ')'    // type guard
  //         | '^'
  //     )*
  private static boolean designator_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "designator_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!designator_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "designator_1", c)) break;
    }
    return true;
  }

  // '.' IDENT
  //         | '[' expr_list ']'
  //         | '(' qual_ident ')'    // type guard
  //         | '^'
  private static boolean designator_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "designator_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseTokens(b, 0, DOT, IDENT);
    if (!r) r = designator_1_0_1(b, l + 1);
    if (!r) r = designator_1_0_2(b, l + 1);
    if (!r) r = consumeToken(b, CARET);
    exit_section_(b, m, null, r);
    return r;
  }

  // '[' expr_list ']'
  private static boolean designator_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "designator_1_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACK);
    r = r && expr_list(b, l + 1);
    r = r && consumeToken(b, RBRACK);
    exit_section_(b, m, null, r);
    return r;
  }

  // '(' qual_ident ')'
  private static boolean designator_1_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "designator_1_0_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && qual_ident(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'ELSE' stmt_seq
  public static boolean else_branch(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "else_branch")) return false;
    if (!nextTokenIs(b, ELSE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ELSE_BRANCH, null);
    r = consumeToken(b, ELSE);
    p = r; // pin = 1
    r = r && stmt_seq(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // 'ELSIF' expr 'THEN' stmt_seq
  public static boolean elsif_branch(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "elsif_branch")) return false;
    if (!nextTokenIs(b, ELSIF)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, ELSIF_BRANCH, null);
    r = consumeToken(b, ELSIF);
    p = r; // pin = 1
    r = r && report_error_(b, expr(b, l + 1));
    r = p && report_error_(b, consumeToken(b, THEN)) && r;
    r = p && stmt_seq(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // '*' | '-'
  static boolean export_mark(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "export_mark")) return false;
    if (!nextTokenIs(b, "", MINUS, STAR)) return false;
    boolean r;
    r = consumeToken(b, STAR);
    if (!r) r = consumeToken(b, MINUS);
    return r;
  }

  /* ********************************************************** */
  // simple_expr [ rel_op simple_expr ]
  public static boolean expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXPR, "<expr>");
    r = simple_expr(b, l + 1);
    r = r && expr_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ rel_op simple_expr ]
  private static boolean expr_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_1")) return false;
    expr_1_0(b, l + 1);
    return true;
  }

  // rel_op simple_expr
  private static boolean expr_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = rel_op(b, l + 1);
    r = r && simple_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // expr ( ',' expr )*
  public static boolean expr_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, EXPR_LIST, "<expr list>");
    r = expr(b, l + 1);
    r = r && expr_list_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( ',' expr )*
  private static boolean expr_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!expr_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "expr_list_1", c)) break;
    }
    return true;
  }

  // ',' expr
  private static boolean expr_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "expr_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // INT_LIT
  //     | REAL_LIT
  //     | CHAR_CONST
  //     | str_lit_multi
  //     | NIL
  //     | set_lit
  //     | designator [ actual_params ] [ <<amiga_oberon_mode>> '^' ]
  //     | paren_expr
  //     | ( '~' | <<amiga_oberon_mode>> 'NOT' ) factor
  public static boolean factor(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "factor")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _COLLAPSE_, FACTOR, "<factor>");
    r = consumeToken(b, INT_LIT);
    if (!r) r = consumeToken(b, REAL_LIT);
    if (!r) r = consumeToken(b, CHAR_CONST);
    if (!r) r = str_lit_multi(b, l + 1);
    if (!r) r = consumeToken(b, NIL);
    if (!r) r = set_lit(b, l + 1);
    if (!r) r = factor_6(b, l + 1);
    if (!r) r = paren_expr(b, l + 1);
    if (!r) r = factor_8(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // designator [ actual_params ] [ <<amiga_oberon_mode>> '^' ]
  private static boolean factor_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "factor_6")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = designator(b, l + 1);
    r = r && factor_6_1(b, l + 1);
    r = r && factor_6_2(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ actual_params ]
  private static boolean factor_6_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "factor_6_1")) return false;
    actual_params(b, l + 1);
    return true;
  }

  // [ <<amiga_oberon_mode>> '^' ]
  private static boolean factor_6_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "factor_6_2")) return false;
    factor_6_2_0(b, l + 1);
    return true;
  }

  // <<amiga_oberon_mode>> '^'
  private static boolean factor_6_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "factor_6_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = amiga_oberon_mode(b, l + 1);
    r = r && consumeToken(b, CARET);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( '~' | <<amiga_oberon_mode>> 'NOT' ) factor
  private static boolean factor_8(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "factor_8")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = factor_8_0(b, l + 1);
    r = r && factor(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // '~' | <<amiga_oberon_mode>> 'NOT'
  private static boolean factor_8_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "factor_8_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, TILDE);
    if (!r) r = factor_8_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // <<amiga_oberon_mode>> 'NOT'
  private static boolean factor_8_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "factor_8_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = amiga_oberon_mode(b, l + 1);
    r = r && consumeToken(b, NOT);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // ident_list ':' type_spec
  public static boolean field_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "field_list")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ident_list(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && type_spec(b, l + 1);
    exit_section_(b, m, FIELD_LIST, r);
    return r;
  }

  /* ********************************************************** */
  // [ module_def ]
  static boolean file(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "file")) return false;
    module_def(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'FOR' IDENT ':=' expr 'TO' expr [ 'BY' const_expr ] 'DO'
  //         [stmt_seq]
  //     'END'
  public static boolean for_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_stmt")) return false;
    if (!nextTokenIs(b, FOR)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, FOR, IDENT, COLON_EQUALS);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, TO);
    r = r && expr(b, l + 1);
    r = r && for_stmt_6(b, l + 1);
    r = r && consumeToken(b, DO);
    r = r && for_stmt_8(b, l + 1);
    r = r && consumeToken(b, END);
    exit_section_(b, m, FOR_STMT, r);
    return r;
  }

  // [ 'BY' const_expr ]
  private static boolean for_stmt_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_stmt_6")) return false;
    for_stmt_6_0(b, l + 1);
    return true;
  }

  // 'BY' const_expr
  private static boolean for_stmt_6_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_stmt_6_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BY);
    r = r && const_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [stmt_seq]
  private static boolean for_stmt_8(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "for_stmt_8")) return false;
    stmt_seq(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // IDENT
  public static boolean formal_param_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "formal_param_name")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENT);
    exit_section_(b, m, FORMAL_PARAM_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // [ 'VAR' ] formal_param_name [ amiga_oberon_param_reg_spec ] [ '..' /* AmigaOberon */ ]
  //         ( ',' formal_param_name [ amiga_oberon_param_reg_spec ] [ '..' /* AmigaOberon */ ] )* ':' formal_type
  public static boolean formal_param_section(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "formal_param_section")) return false;
    if (!nextTokenIs(b, "<formal param section>", IDENT, VAR)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FORMAL_PARAM_SECTION, "<formal param section>");
    r = formal_param_section_0(b, l + 1);
    r = r && formal_param_name(b, l + 1);
    r = r && formal_param_section_2(b, l + 1);
    r = r && formal_param_section_3(b, l + 1);
    r = r && formal_param_section_4(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && formal_type(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ 'VAR' ]
  private static boolean formal_param_section_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "formal_param_section_0")) return false;
    consumeToken(b, VAR);
    return true;
  }

  // [ amiga_oberon_param_reg_spec ]
  private static boolean formal_param_section_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "formal_param_section_2")) return false;
    amiga_oberon_param_reg_spec(b, l + 1);
    return true;
  }

  // [ '..' /* AmigaOberon */ ]
  private static boolean formal_param_section_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "formal_param_section_3")) return false;
    consumeToken(b, DOT_DOT);
    return true;
  }

  // ( ',' formal_param_name [ amiga_oberon_param_reg_spec ] [ '..' /* AmigaOberon */ ] )*
  private static boolean formal_param_section_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "formal_param_section_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!formal_param_section_4_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "formal_param_section_4", c)) break;
    }
    return true;
  }

  // ',' formal_param_name [ amiga_oberon_param_reg_spec ] [ '..' /* AmigaOberon */ ]
  private static boolean formal_param_section_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "formal_param_section_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && formal_param_name(b, l + 1);
    r = r && formal_param_section_4_0_2(b, l + 1);
    r = r && formal_param_section_4_0_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ amiga_oberon_param_reg_spec ]
  private static boolean formal_param_section_4_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "formal_param_section_4_0_2")) return false;
    amiga_oberon_param_reg_spec(b, l + 1);
    return true;
  }

  // [ '..' /* AmigaOberon */ ]
  private static boolean formal_param_section_4_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "formal_param_section_4_0_3")) return false;
    consumeToken(b, DOT_DOT);
    return true;
  }

  /* ********************************************************** */
  // '(' [ formal_param_section ( ';' formal_param_section )* ] ')' [ ':' qual_ident ]
  public static boolean formal_params(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "formal_params")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && formal_params_1(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    r = r && formal_params_3(b, l + 1);
    exit_section_(b, m, FORMAL_PARAMS, r);
    return r;
  }

  // [ formal_param_section ( ';' formal_param_section )* ]
  private static boolean formal_params_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "formal_params_1")) return false;
    formal_params_1_0(b, l + 1);
    return true;
  }

  // formal_param_section ( ';' formal_param_section )*
  private static boolean formal_params_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "formal_params_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = formal_param_section(b, l + 1);
    r = r && formal_params_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ';' formal_param_section )*
  private static boolean formal_params_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "formal_params_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!formal_params_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "formal_params_1_0_1", c)) break;
    }
    return true;
  }

  // ';' formal_param_section
  private static boolean formal_params_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "formal_params_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SEMI);
    r = r && formal_param_section(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ ':' qual_ident ]
  private static boolean formal_params_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "formal_params_3")) return false;
    formal_params_3_0(b, l + 1);
    return true;
  }

  // ':' qual_ident
  private static boolean formal_params_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "formal_params_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && qual_ident(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // qual_ident
  //     | 'ARRAY' [ const_expr ( ',' const_expr )* ] 'OF' formal_type
  //     | 'RECORD' 'END'
  //     | 'POINTER' 'TO' formal_type
  //     | 'PROCEDURE' [ formal_params ]
  public static boolean formal_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "formal_type")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, FORMAL_TYPE, "<formal type>");
    r = qual_ident(b, l + 1);
    if (!r) r = formal_type_1(b, l + 1);
    if (!r) r = parseTokens(b, 0, RECORD, END);
    if (!r) r = formal_type_3(b, l + 1);
    if (!r) r = formal_type_4(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // 'ARRAY' [ const_expr ( ',' const_expr )* ] 'OF' formal_type
  private static boolean formal_type_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "formal_type_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ARRAY);
    r = r && formal_type_1_1(b, l + 1);
    r = r && consumeToken(b, OF);
    r = r && formal_type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ const_expr ( ',' const_expr )* ]
  private static boolean formal_type_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "formal_type_1_1")) return false;
    formal_type_1_1_0(b, l + 1);
    return true;
  }

  // const_expr ( ',' const_expr )*
  private static boolean formal_type_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "formal_type_1_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = const_expr(b, l + 1);
    r = r && formal_type_1_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' const_expr )*
  private static boolean formal_type_1_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "formal_type_1_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!formal_type_1_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "formal_type_1_1_0_1", c)) break;
    }
    return true;
  }

  // ',' const_expr
  private static boolean formal_type_1_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "formal_type_1_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && const_expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'POINTER' 'TO' formal_type
  private static boolean formal_type_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "formal_type_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, POINTER, TO);
    r = r && formal_type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'PROCEDURE' [ formal_params ]
  private static boolean formal_type_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "formal_type_4")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PROCEDURE);
    r = r && formal_type_4_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ formal_params ]
  private static boolean formal_type_4_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "formal_type_4_1")) return false;
    formal_params(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'PROCEDURE' '^' [ receiver ] ident_def [ formal_params ]
  public static boolean forward_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "forward_decl")) return false;
    if (!nextTokenIs(b, PROCEDURE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, FORWARD_DECL, null);
    r = consumeTokens(b, 2, PROCEDURE, CARET);
    p = r; // pin = 2
    r = r && report_error_(b, forward_decl_2(b, l + 1));
    r = p && report_error_(b, ident_def(b, l + 1)) && r;
    r = p && forward_decl_4(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ receiver ]
  private static boolean forward_decl_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "forward_decl_2")) return false;
    receiver(b, l + 1);
    return true;
  }

  // [ formal_params ]
  private static boolean forward_decl_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "forward_decl_4")) return false;
    formal_params(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // qual_ident ':' qual_ident
  public static boolean guard(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "guard")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = qual_ident(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && qual_ident(b, l + 1);
    exit_section_(b, m, GUARD, r);
    return r;
  }

  /* ********************************************************** */
  // IDENT
  //     [ export_mark ]
  public static boolean ident_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ident_def")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENT);
    r = r && ident_def_1(b, l + 1);
    exit_section_(b, m, IDENT_DEF, r);
    return r;
  }

  // [ export_mark ]
  private static boolean ident_def_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ident_def_1")) return false;
    export_mark(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // ident_def ( ',' ident_def )*
  public static boolean ident_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ident_list")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = ident_def(b, l + 1);
    r = r && ident_list_1(b, l + 1);
    exit_section_(b, m, IDENT_LIST, r);
    return r;
  }

  // ( ',' ident_def )*
  private static boolean ident_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ident_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!ident_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "ident_list_1", c)) break;
    }
    return true;
  }

  // ',' ident_def
  private static boolean ident_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "ident_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && ident_def(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'IF' expr 'THEN'
  //         stmt_seq?
  //     elsif_branch*
  //     else_branch?
  //     'END'
  public static boolean if_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_stmt")) return false;
    if (!nextTokenIs(b, IF)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IF);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, THEN);
    r = r && if_stmt_3(b, l + 1);
    r = r && if_stmt_4(b, l + 1);
    r = r && if_stmt_5(b, l + 1);
    r = r && consumeToken(b, END);
    exit_section_(b, m, IF_STMT, r);
    return r;
  }

  // stmt_seq?
  private static boolean if_stmt_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_stmt_3")) return false;
    stmt_seq(b, l + 1);
    return true;
  }

  // elsif_branch*
  private static boolean if_stmt_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_stmt_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!elsif_branch(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "if_stmt_4", c)) break;
    }
    return true;
  }

  // else_branch?
  private static boolean if_stmt_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "if_stmt_5")) return false;
    else_branch(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // IDENT [ <<amiga_oberon_mode>> '*' ] ':='
  //     | IDENT [ <<amiga_oberon_mode>> '*' ] ':'
  public static boolean import_alias(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_alias")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = import_alias_0(b, l + 1);
    if (!r) r = import_alias_1(b, l + 1);
    exit_section_(b, m, IMPORT_ALIAS, r);
    return r;
  }

  // IDENT [ <<amiga_oberon_mode>> '*' ] ':='
  private static boolean import_alias_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_alias_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENT);
    r = r && import_alias_0_1(b, l + 1);
    r = r && consumeToken(b, COLON_EQUALS);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ <<amiga_oberon_mode>> '*' ]
  private static boolean import_alias_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_alias_0_1")) return false;
    import_alias_0_1_0(b, l + 1);
    return true;
  }

  // <<amiga_oberon_mode>> '*'
  private static boolean import_alias_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_alias_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = amiga_oberon_mode(b, l + 1);
    r = r && consumeToken(b, STAR);
    exit_section_(b, m, null, r);
    return r;
  }

  // IDENT [ <<amiga_oberon_mode>> '*' ] ':'
  private static boolean import_alias_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_alias_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENT);
    r = r && import_alias_1_1(b, l + 1);
    r = r && consumeToken(b, COLON);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ <<amiga_oberon_mode>> '*' ]
  private static boolean import_alias_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_alias_1_1")) return false;
    import_alias_1_1_0(b, l + 1);
    return true;
  }

  // <<amiga_oberon_mode>> '*'
  private static boolean import_alias_1_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_alias_1_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = amiga_oberon_mode(b, l + 1);
    r = r && consumeToken(b, STAR);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // [ import_alias ] import_module_reference
  public static boolean import_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_decl")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = import_decl_0(b, l + 1);
    r = r && import_module_reference(b, l + 1);
    exit_section_(b, m, IMPORT_DECL, r);
    return r;
  }

  // [ import_alias ]
  private static boolean import_decl_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_decl_0")) return false;
    import_alias(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // import_decl ( ',' import_decl )*
  public static boolean import_decls(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_decls")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IMPORT_DECLS, "<import decls>");
    r = import_decl(b, l + 1);
    p = r; // pin = 1
    r = r && import_decls_1(b, l + 1);
    exit_section_(b, l, m, r, p, OberonParser::recover_import_list);
    return r || p;
  }

  // ( ',' import_decl )*
  private static boolean import_decls_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_decls_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!import_decls_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "import_decls_1", c)) break;
    }
    return true;
  }

  // ',' import_decl
  private static boolean import_decls_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_decls_1_0")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_);
    r = consumeToken(b, COMMA);
    p = r; // pin = 1
    r = r && import_decl(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // 'IMPORT' import_decls ';'
  public static boolean import_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_list")) return false;
    if (!nextTokenIs(b, IMPORT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, IMPORT_LIST, null);
    r = consumeToken(b, IMPORT);
    p = r; // pin = 1
    r = r && report_error_(b, import_decls(b, l + 1));
    r = p && consumeToken(b, SEMI) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // IDENT '*'
  //     | IDENT
  public static boolean import_module_reference(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "import_module_reference")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = parseTokens(b, 0, IDENT, STAR);
    if (!r) r = consumeToken(b, IDENT);
    exit_section_(b, m, IMPORT_MODULE_REFERENCE, r);
    return r;
  }

  /* ********************************************************** */
  // expr
  public static boolean int_const_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "int_const_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, INT_CONST_EXPR, "<int const expr>");
    r = expr(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'LOOP'
  //         [stmt_seq]
  //     'END'
  public static boolean loop_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "loop_stmt")) return false;
    if (!nextTokenIs(b, LOOP)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, LOOP_STMT, null);
    r = consumeToken(b, LOOP);
    p = r; // pin = 1
    r = r && report_error_(b, loop_stmt_1(b, l + 1));
    r = p && consumeToken(b, END) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [stmt_seq]
  private static boolean loop_stmt_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "loop_stmt_1")) return false;
    stmt_seq(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // module_head
  //     top_level_decls
  //     module_tail
  public static boolean module_def(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_def")) return false;
    if (!nextTokenIs(b, "<module definition>", MODULE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MODULE_DEF, "<module definition>");
    r = module_head(b, l + 1);
    r = r && top_level_decls(b, l + 1);
    r = r && module_tail(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // MODULE
  //     // Oberon-A notation: specifies default system flag for module
  //         [ '[' INT_LIT ']' ]
  //         /*module_def_name*/ IDENT
  //         [ '[' STR_LIT ( ',' STR_LIT )* ']' ]    // names of external object files
  //     ';'
  //     [ import_list ]
  public static boolean module_head(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_head")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MODULE_HEAD, "<module head>");
    r = consumeToken(b, MODULE);
    r = r && module_head_1(b, l + 1);
    r = r && consumeToken(b, IDENT);
    p = r; // pin = 3
    r = r && report_error_(b, module_head_3(b, l + 1));
    r = p && report_error_(b, consumeToken(b, SEMI)) && r;
    r = p && module_head_5(b, l + 1) && r;
    exit_section_(b, l, m, r, p, OberonParser::recover_module_head);
    return r || p;
  }

  // [ '[' INT_LIT ']' ]
  private static boolean module_head_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_head_1")) return false;
    parseTokens(b, 0, LBRACK, INT_LIT, RBRACK);
    return true;
  }

  // [ '[' STR_LIT ( ',' STR_LIT )* ']' ]
  private static boolean module_head_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_head_3")) return false;
    module_head_3_0(b, l + 1);
    return true;
  }

  // '[' STR_LIT ( ',' STR_LIT )* ']'
  private static boolean module_head_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_head_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LBRACK, STR_LIT);
    r = r && module_head_3_0_2(b, l + 1);
    r = r && consumeToken(b, RBRACK);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' STR_LIT )*
  private static boolean module_head_3_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_head_3_0_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!module_head_3_0_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "module_head_3_0_2", c)) break;
    }
    return true;
  }

  // ',' STR_LIT
  private static boolean module_head_3_0_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_head_3_0_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, COMMA, STR_LIT);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ import_list ]
  private static boolean module_head_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_head_5")) return false;
    import_list(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'BEGIN'
  //     [ stmt_seq ]
  public static boolean module_init(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_init")) return false;
    if (!nextTokenIs(b, "<module initializer>", BEGIN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MODULE_INIT, "<module initializer>");
    r = consumeToken(b, BEGIN);
    p = r; // pin = 1
    r = r && module_init_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ stmt_seq ]
  private static boolean module_init_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_init_1")) return false;
    stmt_seq(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // [ module_init ]
  //     // &<<amiga_oberon_mode>>
  //     [ 'CLOSE'           // AmigaOberon
  //         stmt_seq ]
  //     'END' IDENT '.'
  public static boolean module_tail(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_tail")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, MODULE_TAIL, "<module tail>");
    r = module_tail_0(b, l + 1);
    p = r; // pin = 1
    r = r && report_error_(b, module_tail_1(b, l + 1));
    r = p && report_error_(b, consumeTokens(b, -1, END, IDENT, DOT)) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ module_init ]
  private static boolean module_tail_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_tail_0")) return false;
    module_init(b, l + 1);
    return true;
  }

  // [ 'CLOSE'           // AmigaOberon
  //         stmt_seq ]
  private static boolean module_tail_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_tail_1")) return false;
    module_tail_1_0(b, l + 1);
    return true;
  }

  // 'CLOSE'           // AmigaOberon
  //         stmt_seq
  private static boolean module_tail_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "module_tail_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, CLOSE);
    r = r && stmt_seq(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '*' | '/' | 'DIV' | 'MOD' | '&'  | <<amiga_oberon_mode>> 'AND'
  public static boolean mul_op(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mul_op")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, MUL_OP, "<mul op>");
    r = consumeToken(b, STAR);
    if (!r) r = consumeToken(b, SLASH);
    if (!r) r = consumeToken(b, DIV);
    if (!r) r = consumeToken(b, MOD);
    if (!r) r = consumeToken(b, AMP);
    if (!r) r = mul_op_5(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // <<amiga_oberon_mode>> 'AND'
  private static boolean mul_op_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "mul_op_5")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = amiga_oberon_mode(b, l + 1);
    r = r && consumeToken(b, AND);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'PROCEDURE'
  //         ['*']                   // mark as assignable procedure
  //         [ '[' INT_LIT ']' ]     // system flag
  //         proc_decl_name
  //         '[' STR_LIT ']'      // (linker) name of external procedure
  //         '(' [ oberon_a_formal_parameters ] ')' [ ':' formal_type ] ';'
  public static boolean oberon_a_external_proc_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_external_proc_decl")) return false;
    if (!nextTokenIs(b, PROCEDURE)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PROCEDURE);
    r = r && oberon_a_external_proc_decl_1(b, l + 1);
    r = r && oberon_a_external_proc_decl_2(b, l + 1);
    r = r && proc_decl_name(b, l + 1);
    r = r && consumeTokens(b, 0, LBRACK, STR_LIT, RBRACK, LPAREN);
    r = r && oberon_a_external_proc_decl_8(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    r = r && oberon_a_external_proc_decl_10(b, l + 1);
    r = r && consumeToken(b, SEMI);
    exit_section_(b, m, OBERON_A_EXTERNAL_PROC_DECL, r);
    return r;
  }

  // ['*']
  private static boolean oberon_a_external_proc_decl_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_external_proc_decl_1")) return false;
    consumeToken(b, STAR);
    return true;
  }

  // [ '[' INT_LIT ']' ]
  private static boolean oberon_a_external_proc_decl_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_external_proc_decl_2")) return false;
    parseTokens(b, 0, LBRACK, INT_LIT, RBRACK);
    return true;
  }

  // [ oberon_a_formal_parameters ]
  private static boolean oberon_a_external_proc_decl_8(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_external_proc_decl_8")) return false;
    oberon_a_formal_parameters(b, l + 1);
    return true;
  }

  // [ ':' formal_type ]
  private static boolean oberon_a_external_proc_decl_10(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_external_proc_decl_10")) return false;
    oberon_a_external_proc_decl_10_0(b, l + 1);
    return true;
  }

  // ':' formal_type
  private static boolean oberon_a_external_proc_decl_10_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_external_proc_decl_10_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && formal_type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // [ 'VAR' ] IDENT oberon_a_reg_spec ( ',' IDENT oberon_a_reg_spec )* ':' formal_type
  //     | [ 'VAR' ] IDENT ':' formal_type
  public static boolean oberon_a_formal_parameter(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_formal_parameter")) return false;
    if (!nextTokenIs(b, "<oberon a formal parameter>", IDENT, VAR)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, OBERON_A_FORMAL_PARAMETER, "<oberon a formal parameter>");
    r = oberon_a_formal_parameter_0(b, l + 1);
    if (!r) r = oberon_a_formal_parameter_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ 'VAR' ] IDENT oberon_a_reg_spec ( ',' IDENT oberon_a_reg_spec )* ':' formal_type
  private static boolean oberon_a_formal_parameter_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_formal_parameter_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = oberon_a_formal_parameter_0_0(b, l + 1);
    r = r && consumeToken(b, IDENT);
    r = r && oberon_a_reg_spec(b, l + 1);
    r = r && oberon_a_formal_parameter_0_3(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && formal_type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ 'VAR' ]
  private static boolean oberon_a_formal_parameter_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_formal_parameter_0_0")) return false;
    consumeToken(b, VAR);
    return true;
  }

  // ( ',' IDENT oberon_a_reg_spec )*
  private static boolean oberon_a_formal_parameter_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_formal_parameter_0_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!oberon_a_formal_parameter_0_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "oberon_a_formal_parameter_0_3", c)) break;
    }
    return true;
  }

  // ',' IDENT oberon_a_reg_spec
  private static boolean oberon_a_formal_parameter_0_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_formal_parameter_0_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, COMMA, IDENT);
    r = r && oberon_a_reg_spec(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ 'VAR' ] IDENT ':' formal_type
  private static boolean oberon_a_formal_parameter_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_formal_parameter_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = oberon_a_formal_parameter_1_0(b, l + 1);
    r = r && consumeTokens(b, 0, IDENT, COLON);
    r = r && formal_type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ 'VAR' ]
  private static boolean oberon_a_formal_parameter_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_formal_parameter_1_0")) return false;
    consumeToken(b, VAR);
    return true;
  }

  /* ********************************************************** */
  // '(' [ oberon_a_formal_parameter ( ';' oberon_a_formal_parameter )* ] ')'
  public static boolean oberon_a_formal_parameters(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_formal_parameters")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && oberon_a_formal_parameters_1(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, OBERON_A_FORMAL_PARAMETERS, r);
    return r;
  }

  // [ oberon_a_formal_parameter ( ';' oberon_a_formal_parameter )* ]
  private static boolean oberon_a_formal_parameters_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_formal_parameters_1")) return false;
    oberon_a_formal_parameters_1_0(b, l + 1);
    return true;
  }

  // oberon_a_formal_parameter ( ';' oberon_a_formal_parameter )*
  private static boolean oberon_a_formal_parameters_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_formal_parameters_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = oberon_a_formal_parameter(b, l + 1);
    r = r && oberon_a_formal_parameters_1_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ';' oberon_a_formal_parameter )*
  private static boolean oberon_a_formal_parameters_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_formal_parameters_1_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!oberon_a_formal_parameters_1_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "oberon_a_formal_parameters_1_0_1", c)) break;
    }
    return true;
  }

  // ';' oberon_a_formal_parameter
  private static boolean oberon_a_formal_parameters_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_formal_parameters_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SEMI);
    r = r && oberon_a_formal_parameter(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'PROCEDURE'
  //     ident_def
  //     oberon_a_shared_lib_lv_data
  //     [ oberon_a_formal_parameters ]
  //     [ ':' formal_type ] ';'
  public static boolean oberon_a_lib_proc_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_lib_proc_decl")) return false;
    if (!nextTokenIs(b, PROCEDURE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, OBERON_A_LIB_PROC_DECL, null);
    r = consumeToken(b, PROCEDURE);
    r = r && ident_def(b, l + 1);
    r = r && oberon_a_shared_lib_lv_data(b, l + 1);
    p = r; // pin = oberon_a_shared_lib_lv_data
    r = r && report_error_(b, oberon_a_lib_proc_decl_3(b, l + 1));
    r = p && report_error_(b, oberon_a_lib_proc_decl_4(b, l + 1)) && r;
    r = p && consumeToken(b, SEMI) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ oberon_a_formal_parameters ]
  private static boolean oberon_a_lib_proc_decl_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_lib_proc_decl_3")) return false;
    oberon_a_formal_parameters(b, l + 1);
    return true;
  }

  // [ ':' formal_type ]
  private static boolean oberon_a_lib_proc_decl_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_lib_proc_decl_4")) return false;
    oberon_a_lib_proc_decl_4_0(b, l + 1);
    return true;
  }

  // ':' formal_type
  private static boolean oberon_a_lib_proc_decl_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_lib_proc_decl_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COLON);
    r = r && formal_type(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // '[' INT_LIT ']'     // register (0-15)
  //     [ ".." ]
  static boolean oberon_a_reg_spec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_reg_spec")) return false;
    if (!nextTokenIs(b, LBRACK)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LBRACK, INT_LIT, RBRACK);
    r = r && oberon_a_reg_spec_3(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ ".." ]
  private static boolean oberon_a_reg_spec_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_reg_spec_3")) return false;
    consumeToken(b, DOT_DOT);
    return true;
  }

  /* ********************************************************** */
  // '[' IDENT ',' [ '-' ] INT_LIT ']'
  public static boolean oberon_a_shared_lib_lv_data(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_shared_lib_lv_data")) return false;
    if (!nextTokenIs(b, LBRACK)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, LBRACK, IDENT, COMMA);
    r = r && oberon_a_shared_lib_lv_data_3(b, l + 1);
    r = r && consumeTokens(b, 0, INT_LIT, RBRACK);
    exit_section_(b, m, OBERON_A_SHARED_LIB_LV_DATA, r);
    return r;
  }

  // [ '-' ]
  private static boolean oberon_a_shared_lib_lv_data_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "oberon_a_shared_lib_lv_data_3")) return false;
    consumeToken(b, MINUS);
    return true;
  }

  /* ********************************************************** */
  // '(' expr ')'
  static boolean paren_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "paren_expr")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && expr(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // [ 'UNTRACED' ] 'POINTER' [ reg_spec ] 'TO' type_spec
  //     | 'BPOINTER' [ reg_spec ] 'TO' type_spec
  public static boolean pointer_type_spec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pointer_type_spec")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, POINTER_TYPE_SPEC, "<pointer type spec>");
    r = pointer_type_spec_0(b, l + 1);
    if (!r) r = pointer_type_spec_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ 'UNTRACED' ] 'POINTER' [ reg_spec ] 'TO' type_spec
  private static boolean pointer_type_spec_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pointer_type_spec_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = pointer_type_spec_0_0(b, l + 1);
    r = r && consumeToken(b, POINTER);
    r = r && pointer_type_spec_0_2(b, l + 1);
    r = r && consumeToken(b, TO);
    r = r && type_spec(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ 'UNTRACED' ]
  private static boolean pointer_type_spec_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pointer_type_spec_0_0")) return false;
    consumeToken(b, UNTRACED);
    return true;
  }

  // [ reg_spec ]
  private static boolean pointer_type_spec_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pointer_type_spec_0_2")) return false;
    reg_spec(b, l + 1);
    return true;
  }

  // 'BPOINTER' [ reg_spec ] 'TO' type_spec
  private static boolean pointer_type_spec_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pointer_type_spec_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, BPOINTER);
    r = r && pointer_type_spec_1_1(b, l + 1);
    r = r && consumeToken(b, TO);
    r = r && type_spec(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ reg_spec ]
  private static boolean pointer_type_spec_1_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "pointer_type_spec_1_1")) return false;
    reg_spec(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // designator [ actual_params ]
  public static boolean proc_call_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "proc_call_stmt")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = designator(b, l + 1);
    r = r && proc_call_stmt_1(b, l + 1);
    exit_section_(b, m, PROC_CALL_STMT, r);
    return r;
  }

  // [ actual_params ]
  private static boolean proc_call_stmt_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "proc_call_stmt_1")) return false;
    actual_params(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // IDENT
  //     [ export_mark ]
  public static boolean proc_decl_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "proc_decl_name")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PROC_DECL_NAME, null);
    r = consumeToken(b, IDENT);
    p = r; // pin = 1
    r = r && proc_decl_name_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ export_mark ]
  private static boolean proc_decl_name_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "proc_decl_name_1")) return false;
    export_mark(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // IDENT
  public static boolean proc_decl_name_no_exp_mark(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "proc_decl_name_no_exp_mark")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENT);
    exit_section_(b, m, PROC_DECL_NAME_NO_EXP_MARK, r);
    return r;
  }

  /* ********************************************************** */
  // 'PROCEDURE'
  //     [ '*' /* Amiga Oberon */ ]
  //     [ '[' INT_LIT ']']          // Oberon-A: For overriding default system flag set for module
  //     [ receiver ]
  //     proc_decl_name
  //     [ formal_params ] ';'
  //     decl*
  //     procedure_decl_body_block
  public static boolean procedure_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "procedure_decl")) return false;
    if (!nextTokenIs(b, PROCEDURE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PROCEDURE_DECL, null);
    r = consumeToken(b, PROCEDURE);
    r = r && procedure_decl_1(b, l + 1);
    r = r && procedure_decl_2(b, l + 1);
    r = r && procedure_decl_3(b, l + 1);
    r = r && proc_decl_name(b, l + 1);
    p = r; // pin = 5
    r = r && report_error_(b, procedure_decl_5(b, l + 1));
    r = p && report_error_(b, consumeToken(b, SEMI)) && r;
    r = p && report_error_(b, procedure_decl_7(b, l + 1)) && r;
    r = p && procedure_decl_body_block(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ '*' /* Amiga Oberon */ ]
  private static boolean procedure_decl_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "procedure_decl_1")) return false;
    consumeToken(b, STAR);
    return true;
  }

  // [ '[' INT_LIT ']']
  private static boolean procedure_decl_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "procedure_decl_2")) return false;
    parseTokens(b, 0, LBRACK, INT_LIT, RBRACK);
    return true;
  }

  // [ receiver ]
  private static boolean procedure_decl_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "procedure_decl_3")) return false;
    receiver(b, l + 1);
    return true;
  }

  // [ formal_params ]
  private static boolean procedure_decl_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "procedure_decl_5")) return false;
    formal_params(b, l + 1);
    return true;
  }

  // decl*
  private static boolean procedure_decl_7(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "procedure_decl_7")) return false;
    while (true) {
      int c = current_position_(b);
      if (!decl(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "procedure_decl_7", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // 'BEGIN'
  //         [ stmt_seq ]
  public static boolean procedure_decl_body(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "procedure_decl_body")) return false;
    if (!nextTokenIs(b, BEGIN)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PROCEDURE_DECL_BODY, null);
    r = consumeToken(b, BEGIN);
    p = r; // pin = 1
    r = r && procedure_decl_body_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ stmt_seq ]
  private static boolean procedure_decl_body_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "procedure_decl_body_1")) return false;
    stmt_seq(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // [ procedure_decl_body ]
  //     procedure_decl_tail
  public static boolean procedure_decl_body_block(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "procedure_decl_body_block")) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, PROCEDURE_DECL_BODY_BLOCK, "<procedure body>");
    r = procedure_decl_body_block_0(b, l + 1);
    p = r; // pin = 1
    r = r && procedure_decl_tail(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ procedure_decl_body ]
  private static boolean procedure_decl_body_block_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "procedure_decl_body_block_0")) return false;
    procedure_decl_body(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'END' IDENT ';'
  public static boolean procedure_decl_tail(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "procedure_decl_tail")) return false;
    if (!nextTokenIs(b, END)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, END, IDENT, SEMI);
    exit_section_(b, m, PROCEDURE_DECL_TAIL, r);
    return r;
  }

  /* ********************************************************** */
  // qual_ident_qualified
  //     | qual_ident_simple
  public static boolean qual_ident(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "qual_ident")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = qual_ident_qualified(b, l + 1);
    if (!r) r = qual_ident_simple(b, l + 1);
    exit_section_(b, m, QUAL_IDENT, r);
    return r;
  }

  /* ********************************************************** */
  // IDENT '.' IDENT
  public static boolean qual_ident_qualified(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "qual_ident_qualified")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, QUAL_IDENT_QUALIFIED, null);
    r = consumeTokens(b, 2, IDENT, DOT, IDENT);
    p = r; // pin = 2
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  /* ********************************************************** */
  // IDENT
  public static boolean qual_ident_simple(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "qual_ident_simple")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENT);
    exit_section_(b, m, QUAL_IDENT_SIMPLE, r);
    return r;
  }

  /* ********************************************************** */
  // '(' [ 'VAR' ] receiver_name ':' receiver_type ')'
  static boolean receiver(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "receiver")) return false;
    if (!nextTokenIs(b, LPAREN)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && receiver_1(b, l + 1);
    r = r && receiver_name(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && receiver_type(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ 'VAR' ]
  private static boolean receiver_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "receiver_1")) return false;
    consumeToken(b, VAR);
    return true;
  }

  /* ********************************************************** */
  // IDENT
  public static boolean receiver_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "receiver_name")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENT);
    exit_section_(b, m, RECEIVER_NAME, r);
    return r;
  }

  /* ********************************************************** */
  // IDENT
  public static boolean receiver_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "receiver_type")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENT);
    exit_section_(b, m, RECEIVER_TYPE, r);
    return r;
  }

  /* ********************************************************** */
  // qual_ident
  public static boolean record_base_type(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "record_base_type")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = qual_ident(b, l + 1);
    exit_section_(b, m, RECORD_BASE_TYPE, r);
    return r;
  }

  /* ********************************************************** */
  // 'RECORD' [ '(' record_base_type ')' ]
  //         field_list? ( ';' field_list? )*
  //     'END'
  //     // Oberon-A system flag notation
  //     | 'RECORD' '[' INT_LIT ']' [ '(' record_base_type ')' ]
  //         field_list? ( ';' field_list? )*
  //     'END'
  public static boolean record_type_spec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "record_type_spec")) return false;
    if (!nextTokenIs(b, RECORD)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = record_type_spec_0(b, l + 1);
    if (!r) r = record_type_spec_1(b, l + 1);
    exit_section_(b, m, RECORD_TYPE_SPEC, r);
    return r;
  }

  // 'RECORD' [ '(' record_base_type ')' ]
  //         field_list? ( ';' field_list? )*
  //     'END'
  private static boolean record_type_spec_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "record_type_spec_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, RECORD);
    r = r && record_type_spec_0_1(b, l + 1);
    r = r && record_type_spec_0_2(b, l + 1);
    r = r && record_type_spec_0_3(b, l + 1);
    r = r && consumeToken(b, END);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ '(' record_base_type ')' ]
  private static boolean record_type_spec_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "record_type_spec_0_1")) return false;
    record_type_spec_0_1_0(b, l + 1);
    return true;
  }

  // '(' record_base_type ')'
  private static boolean record_type_spec_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "record_type_spec_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && record_base_type(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // field_list?
  private static boolean record_type_spec_0_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "record_type_spec_0_2")) return false;
    field_list(b, l + 1);
    return true;
  }

  // ( ';' field_list? )*
  private static boolean record_type_spec_0_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "record_type_spec_0_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!record_type_spec_0_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "record_type_spec_0_3", c)) break;
    }
    return true;
  }

  // ';' field_list?
  private static boolean record_type_spec_0_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "record_type_spec_0_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SEMI);
    r = r && record_type_spec_0_3_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // field_list?
  private static boolean record_type_spec_0_3_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "record_type_spec_0_3_0_1")) return false;
    field_list(b, l + 1);
    return true;
  }

  // 'RECORD' '[' INT_LIT ']' [ '(' record_base_type ')' ]
  //         field_list? ( ';' field_list? )*
  //     'END'
  private static boolean record_type_spec_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "record_type_spec_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeTokens(b, 0, RECORD, LBRACK, INT_LIT, RBRACK);
    r = r && record_type_spec_1_4(b, l + 1);
    r = r && record_type_spec_1_5(b, l + 1);
    r = r && record_type_spec_1_6(b, l + 1);
    r = r && consumeToken(b, END);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ '(' record_base_type ')' ]
  private static boolean record_type_spec_1_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "record_type_spec_1_4")) return false;
    record_type_spec_1_4_0(b, l + 1);
    return true;
  }

  // '(' record_base_type ')'
  private static boolean record_type_spec_1_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "record_type_spec_1_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && record_base_type(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // field_list?
  private static boolean record_type_spec_1_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "record_type_spec_1_5")) return false;
    field_list(b, l + 1);
    return true;
  }

  // ( ';' field_list? )*
  private static boolean record_type_spec_1_6(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "record_type_spec_1_6")) return false;
    while (true) {
      int c = current_position_(b);
      if (!record_type_spec_1_6_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "record_type_spec_1_6", c)) break;
    }
    return true;
  }

  // ';' field_list?
  private static boolean record_type_spec_1_6_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "record_type_spec_1_6_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SEMI);
    r = r && record_type_spec_1_6_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // field_list?
  private static boolean record_type_spec_1_6_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "record_type_spec_1_6_0_1")) return false;
    field_list(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // !(BEGIN | CONST | TYPE | VAR | PROCEDURE)
  static boolean recover_decl_seq(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_decl_seq")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_decl_seq_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // BEGIN | CONST | TYPE | VAR | PROCEDURE
  private static boolean recover_decl_seq_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_decl_seq_0")) return false;
    boolean r;
    r = consumeToken(b, BEGIN);
    if (!r) r = consumeToken(b, CONST);
    if (!r) r = consumeToken(b, TYPE);
    if (!r) r = consumeToken(b, VAR);
    if (!r) r = consumeToken(b, PROCEDURE);
    return r;
  }

  /* ********************************************************** */
  // !(SEMI | BEGIN | CONST | PROCEDURE | TYPE | VAR)
  static boolean recover_import_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_import_list")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !recover_import_list_0(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // SEMI | BEGIN | CONST | PROCEDURE | TYPE | VAR
  private static boolean recover_import_list_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_import_list_0")) return false;
    boolean r;
    r = consumeToken(b, SEMI);
    if (!r) r = consumeToken(b, BEGIN);
    if (!r) r = consumeToken(b, CONST);
    if (!r) r = consumeToken(b, PROCEDURE);
    if (!r) r = consumeToken(b, TYPE);
    if (!r) r = consumeToken(b, VAR);
    return r;
  }

  /* ********************************************************** */
  // !(IMPORT) recover_top_level
  static boolean recover_module_head(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_module_head")) return false;
    if (!nextTokenIs(b, RECOVER_TOP_LEVEL)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = recover_module_head_0(b, l + 1);
    r = r && consumeToken(b, RECOVER_TOP_LEVEL);
    exit_section_(b, m, null, r);
    return r;
  }

  // !(IMPORT)
  private static boolean recover_module_head_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "recover_module_head_0")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NOT_);
    r = !consumeToken(b, IMPORT);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // '[' const_expr ']'
  public static boolean reg_spec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "reg_spec")) return false;
    if (!nextTokenIs(b, LBRACK)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACK);
    r = r && const_expr(b, l + 1);
    r = r && consumeToken(b, RBRACK);
    exit_section_(b, m, REG_SPEC, r);
    return r;
  }

  /* ********************************************************** */
  // '=' | '#' | '<' | '<=' | '>' | '>=' | 'IN' | 'IS'
  public static boolean rel_op(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "rel_op")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, REL_OP, "<rel op>");
    r = consumeToken(b, EQUALS);
    if (!r) r = consumeToken(b, HASH);
    if (!r) r = consumeToken(b, LESS);
    if (!r) r = consumeToken(b, LESS_EQUAL);
    if (!r) r = consumeToken(b, GREATER);
    if (!r) r = consumeToken(b, GREATER_EQUAL);
    if (!r) r = consumeToken(b, IN);
    if (!r) r = consumeToken(b, IS);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  /* ********************************************************** */
  // 'REPEAT'
  //         [ stmt_seq ]
  //     'UNTIL' expr
  public static boolean repeat_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "repeat_stmt")) return false;
    if (!nextTokenIs(b, REPEAT)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, REPEAT_STMT, null);
    r = consumeToken(b, REPEAT);
    p = r; // pin = 1
    r = r && report_error_(b, repeat_stmt_1(b, l + 1));
    r = p && report_error_(b, consumeToken(b, UNTIL)) && r;
    r = p && expr(b, l + 1) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [ stmt_seq ]
  private static boolean repeat_stmt_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "repeat_stmt_1")) return false;
    stmt_seq(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // expr [ '..' expr ]
  public static boolean set_elem(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "set_elem")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SET_ELEM, "<set elem>");
    r = expr(b, l + 1);
    r = r && set_elem_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ '..' expr ]
  private static boolean set_elem_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "set_elem_1")) return false;
    set_elem_1_0(b, l + 1);
    return true;
  }

  // '..' expr
  private static boolean set_elem_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "set_elem_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, DOT_DOT);
    r = r && expr(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // [ IDENT ] // Amiga Oberon -- SET | SHORTSET | LONGSET
  //     '{' [ set_elem ( ',' set_elem)* ] '}'
  public static boolean set_lit(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "set_lit")) return false;
    if (!nextTokenIs(b, "<set lit>", IDENT, LBRACE)) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SET_LIT, "<set lit>");
    r = set_lit_0(b, l + 1);
    r = r && consumeToken(b, LBRACE);
    r = r && set_lit_2(b, l + 1);
    r = r && consumeToken(b, RBRACE);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ IDENT ]
  private static boolean set_lit_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "set_lit_0")) return false;
    consumeToken(b, IDENT);
    return true;
  }

  // [ set_elem ( ',' set_elem)* ]
  private static boolean set_lit_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "set_lit_2")) return false;
    set_lit_2_0(b, l + 1);
    return true;
  }

  // set_elem ( ',' set_elem)*
  private static boolean set_lit_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "set_lit_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = set_elem(b, l + 1);
    r = r && set_lit_2_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( ',' set_elem)*
  private static boolean set_lit_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "set_lit_2_0_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!set_lit_2_0_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "set_lit_2_0_1", c)) break;
    }
    return true;
  }

  // ',' set_elem
  private static boolean set_lit_2_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "set_lit_2_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && set_elem(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // [ '+' | '-' ] term ( add_op term )*
  public static boolean simple_expr(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_expr")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, SIMPLE_EXPR, "<simple expr>");
    r = simple_expr_0(b, l + 1);
    r = r && term(b, l + 1);
    r = r && simple_expr_2(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // [ '+' | '-' ]
  private static boolean simple_expr_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_expr_0")) return false;
    simple_expr_0_0(b, l + 1);
    return true;
  }

  // '+' | '-'
  private static boolean simple_expr_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_expr_0_0")) return false;
    boolean r;
    r = consumeToken(b, PLUS);
    if (!r) r = consumeToken(b, MINUS);
    return r;
  }

  // ( add_op term )*
  private static boolean simple_expr_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_expr_2")) return false;
    while (true) {
      int c = current_position_(b);
      if (!simple_expr_2_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "simple_expr_2", c)) break;
    }
    return true;
  }

  // add_op term
  private static boolean simple_expr_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "simple_expr_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = add_op(b, l + 1);
    r = r && term(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // assign_stmt
  //         | proc_call_stmt
  //         | if_stmt
  //         | case_stmt
  //         | while_stmt
  //         | repeat_stmt
  //         | for_stmt      // oberon-2
  //         | loop_stmt
  //         | with_stmt
  //         | 'EXIT'
  //         | 'RETURN' [ expr ]
  public static boolean stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STMT, "<stmt>");
    r = assign_stmt(b, l + 1);
    if (!r) r = proc_call_stmt(b, l + 1);
    if (!r) r = if_stmt(b, l + 1);
    if (!r) r = case_stmt(b, l + 1);
    if (!r) r = while_stmt(b, l + 1);
    if (!r) r = repeat_stmt(b, l + 1);
    if (!r) r = for_stmt(b, l + 1);
    if (!r) r = loop_stmt(b, l + 1);
    if (!r) r = with_stmt(b, l + 1);
    if (!r) r = consumeToken(b, EXIT);
    if (!r) r = stmt_10(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // 'RETURN' [ expr ]
  private static boolean stmt_10(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_10")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, RETURN);
    r = r && stmt_10_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ expr ]
  private static boolean stmt_10_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_10_1")) return false;
    expr(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // (';' | (stmt ';') )+ stmt?
  //     | stmt
  public static boolean stmt_seq(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_seq")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, STMT_SEQ, "<stmt seq>");
    r = stmt_seq_0(b, l + 1);
    if (!r) r = stmt(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // (';' | (stmt ';') )+ stmt?
  private static boolean stmt_seq_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_seq_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = stmt_seq_0_0(b, l + 1);
    r = r && stmt_seq_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // (';' | (stmt ';') )+
  private static boolean stmt_seq_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_seq_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = stmt_seq_0_0_0(b, l + 1);
    while (r) {
      int c = current_position_(b);
      if (!stmt_seq_0_0_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "stmt_seq_0_0", c)) break;
    }
    exit_section_(b, m, null, r);
    return r;
  }

  // ';' | (stmt ';')
  private static boolean stmt_seq_0_0_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_seq_0_0_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SEMI);
    if (!r) r = stmt_seq_0_0_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // stmt ';'
  private static boolean stmt_seq_0_0_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_seq_0_0_0_1")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = stmt(b, l + 1);
    r = r && consumeToken(b, SEMI);
    exit_section_(b, m, null, r);
    return r;
  }

  // stmt?
  private static boolean stmt_seq_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "stmt_seq_0_1")) return false;
    stmt(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // STR_LIT /* TBD multiple only in oberon-a? */ ( STR_LIT )*
  public static boolean str_lit_multi(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "str_lit_multi")) return false;
    if (!nextTokenIs(b, STR_LIT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, STR_LIT);
    r = r && str_lit_multi_1(b, l + 1);
    exit_section_(b, m, STR_LIT_MULTI, r);
    return r;
  }

  // ( STR_LIT )*
  private static boolean str_lit_multi_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "str_lit_multi_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!consumeToken(b, STR_LIT)) break;
      if (!empty_element_parsed_guard_(b, "str_lit_multi_1", c)) break;
    }
    return true;
  }

  /* ********************************************************** */
  // 'STRUCT'
  //         // extend from struct specified by identifier after ':'; access must be qualified by identifier given before ':'
  //         [ '(' [ IDENT ( '*' | '-' )? ':' ] qual_ident ')' ]
  //         field_list? ( ';' field_list? )*
  //     'END'
  public static boolean struct_type_spec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_type_spec")) return false;
    if (!nextTokenIs(b, STRUCT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, STRUCT);
    r = r && struct_type_spec_1(b, l + 1);
    r = r && struct_type_spec_2(b, l + 1);
    r = r && struct_type_spec_3(b, l + 1);
    r = r && consumeToken(b, END);
    exit_section_(b, m, STRUCT_TYPE_SPEC, r);
    return r;
  }

  // [ '(' [ IDENT ( '*' | '-' )? ':' ] qual_ident ')' ]
  private static boolean struct_type_spec_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_type_spec_1")) return false;
    struct_type_spec_1_0(b, l + 1);
    return true;
  }

  // '(' [ IDENT ( '*' | '-' )? ':' ] qual_ident ')'
  private static boolean struct_type_spec_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_type_spec_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LPAREN);
    r = r && struct_type_spec_1_0_1(b, l + 1);
    r = r && qual_ident(b, l + 1);
    r = r && consumeToken(b, RPAREN);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ IDENT ( '*' | '-' )? ':' ]
  private static boolean struct_type_spec_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_type_spec_1_0_1")) return false;
    struct_type_spec_1_0_1_0(b, l + 1);
    return true;
  }

  // IDENT ( '*' | '-' )? ':'
  private static boolean struct_type_spec_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_type_spec_1_0_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENT);
    r = r && struct_type_spec_1_0_1_0_1(b, l + 1);
    r = r && consumeToken(b, COLON);
    exit_section_(b, m, null, r);
    return r;
  }

  // ( '*' | '-' )?
  private static boolean struct_type_spec_1_0_1_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_type_spec_1_0_1_0_1")) return false;
    struct_type_spec_1_0_1_0_1_0(b, l + 1);
    return true;
  }

  // '*' | '-'
  private static boolean struct_type_spec_1_0_1_0_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_type_spec_1_0_1_0_1_0")) return false;
    boolean r;
    r = consumeToken(b, STAR);
    if (!r) r = consumeToken(b, MINUS);
    return r;
  }

  // field_list?
  private static boolean struct_type_spec_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_type_spec_2")) return false;
    field_list(b, l + 1);
    return true;
  }

  // ( ';' field_list? )*
  private static boolean struct_type_spec_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_type_spec_3")) return false;
    while (true) {
      int c = current_position_(b);
      if (!struct_type_spec_3_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "struct_type_spec_3", c)) break;
    }
    return true;
  }

  // ';' field_list?
  private static boolean struct_type_spec_3_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_type_spec_3_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, SEMI);
    r = r && struct_type_spec_3_0_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // field_list?
  private static boolean struct_type_spec_3_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "struct_type_spec_3_0_1")) return false;
    field_list(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // factor ( mul_op factor )*
  public static boolean term(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "term")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TERM, "<term>");
    r = factor(b, l + 1);
    r = r && term_1(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // ( mul_op factor )*
  private static boolean term_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "term_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!term_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "term_1", c)) break;
    }
    return true;
  }

  // mul_op factor
  private static boolean term_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "term_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = mul_op(b, l + 1);
    r = r && factor(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // decl*
  public static boolean top_level_decls(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "top_level_decls")) return false;
    Marker m = enter_section_(b, l, _NONE_, TOP_LEVEL_DECLS, "<top level decls>");
    while (true) {
      int c = current_position_(b);
      if (!decl(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "top_level_decls", c)) break;
    }
    exit_section_(b, l, m, true, false, null);
    return true;
  }

  /* ********************************************************** */
  // type_decl_name
  //     '=' type_spec
  public static boolean type_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_decl")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = type_decl_name(b, l + 1);
    r = r && consumeToken(b, EQUALS);
    r = r && type_spec(b, l + 1);
    exit_section_(b, m, TYPE_DECL, r);
    return r;
  }

  /* ********************************************************** */
  // IDENT
  //     [ export_mark ]
  public static boolean type_decl_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_decl_name")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENT);
    r = r && type_decl_name_1(b, l + 1);
    exit_section_(b, m, TYPE_DECL_NAME, r);
    return r;
  }

  // [ export_mark ]
  private static boolean type_decl_name_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_decl_name_1")) return false;
    export_mark(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'TYPE' ( type_decl ';' )*
  public static boolean type_section(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_section")) return false;
    if (!nextTokenIs(b, TYPE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, TYPE_SECTION, null);
    r = consumeToken(b, TYPE);
    p = r; // pin = 1
    r = r && type_section_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ( type_decl ';' )*
  private static boolean type_section_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_section_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!type_section_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "type_section_1", c)) break;
    }
    return true;
  }

  // type_decl ';'
  private static boolean type_section_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_section_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = type_decl(b, l + 1);
    r = r && consumeToken(b, SEMI);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // array_type_spec
  //     | record_type_spec
  //     | <<amiga_oberon_mode>> struct_type_spec      // Amiga Oberon
  //     | <<amiga_oberon_mode>> ( 'SET' | 'SHORTSET' | 'LONGSET' )    // Amiga Oberon
  //     | pointer_type_spec
  //     | 'PROCEDURE' [ formal_params ]
  //     | qual_ident
  public static boolean type_spec(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_spec")) return false;
    boolean r;
    Marker m = enter_section_(b, l, _NONE_, TYPE_SPEC, "<type spec>");
    r = array_type_spec(b, l + 1);
    if (!r) r = record_type_spec(b, l + 1);
    if (!r) r = type_spec_2(b, l + 1);
    if (!r) r = type_spec_3(b, l + 1);
    if (!r) r = pointer_type_spec(b, l + 1);
    if (!r) r = type_spec_5(b, l + 1);
    if (!r) r = qual_ident(b, l + 1);
    exit_section_(b, l, m, r, false, null);
    return r;
  }

  // <<amiga_oberon_mode>> struct_type_spec
  private static boolean type_spec_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_spec_2")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = amiga_oberon_mode(b, l + 1);
    r = r && struct_type_spec(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // <<amiga_oberon_mode>> ( 'SET' | 'SHORTSET' | 'LONGSET' )
  private static boolean type_spec_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_spec_3")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = amiga_oberon_mode(b, l + 1);
    r = r && type_spec_3_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // 'SET' | 'SHORTSET' | 'LONGSET'
  private static boolean type_spec_3_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_spec_3_1")) return false;
    boolean r;
    r = consumeToken(b, "SET");
    if (!r) r = consumeToken(b, "SHORTSET");
    if (!r) r = consumeToken(b, "LONGSET");
    return r;
  }

  // 'PROCEDURE' [ formal_params ]
  private static boolean type_spec_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_spec_5")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PROCEDURE);
    r = r && type_spec_5_1(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ formal_params ]
  private static boolean type_spec_5_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "type_spec_5_1")) return false;
    formal_params(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // var_decl_name_list
  //     ':' type_spec ';'
  public static boolean var_decl(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "var_decl")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = var_decl_name_list(b, l + 1);
    r = r && consumeToken(b, COLON);
    r = r && type_spec(b, l + 1);
    r = r && consumeToken(b, SEMI);
    exit_section_(b, m, VAR_DECL, r);
    return r;
  }

  /* ********************************************************** */
  // IDENT
  //     [ export_mark ]
  //     [ '[' ( int_const_expr /* Amiga Oberon abs var */ | STR_LIT /* Amiga Oberon ext proc ref */) ']'  ]
  public static boolean var_decl_name(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "var_decl_name")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, IDENT);
    r = r && var_decl_name_1(b, l + 1);
    r = r && var_decl_name_2(b, l + 1);
    exit_section_(b, m, VAR_DECL_NAME, r);
    return r;
  }

  // [ export_mark ]
  private static boolean var_decl_name_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "var_decl_name_1")) return false;
    export_mark(b, l + 1);
    return true;
  }

  // [ '[' ( int_const_expr /* Amiga Oberon abs var */ | STR_LIT /* Amiga Oberon ext proc ref */) ']'  ]
  private static boolean var_decl_name_2(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "var_decl_name_2")) return false;
    var_decl_name_2_0(b, l + 1);
    return true;
  }

  // '[' ( int_const_expr /* Amiga Oberon abs var */ | STR_LIT /* Amiga Oberon ext proc ref */) ']'
  private static boolean var_decl_name_2_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "var_decl_name_2_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, LBRACK);
    r = r && var_decl_name_2_0_1(b, l + 1);
    r = r && consumeToken(b, RBRACK);
    exit_section_(b, m, null, r);
    return r;
  }

  // int_const_expr /* Amiga Oberon abs var */ | STR_LIT
  private static boolean var_decl_name_2_0_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "var_decl_name_2_0_1")) return false;
    boolean r;
    r = int_const_expr(b, l + 1);
    if (!r) r = consumeToken(b, STR_LIT);
    return r;
  }

  /* ********************************************************** */
  // var_decl_name ( ',' var_decl_name )*
  public static boolean var_decl_name_list(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "var_decl_name_list")) return false;
    if (!nextTokenIs(b, IDENT)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = var_decl_name(b, l + 1);
    r = r && var_decl_name_list_1(b, l + 1);
    exit_section_(b, m, VAR_DECL_NAME_LIST, r);
    return r;
  }

  // ( ',' var_decl_name )*
  private static boolean var_decl_name_list_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "var_decl_name_list_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!var_decl_name_list_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "var_decl_name_list_1", c)) break;
    }
    return true;
  }

  // ',' var_decl_name
  private static boolean var_decl_name_list_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "var_decl_name_list_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, COMMA);
    r = r && var_decl_name(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'VAR' ( var_decl )*
  public static boolean var_section(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "var_section")) return false;
    if (!nextTokenIs(b, VAR)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, VAR_SECTION, null);
    r = consumeToken(b, VAR);
    p = r; // pin = 1
    r = r && var_section_1(b, l + 1);
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // ( var_decl )*
  private static boolean var_section_1(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "var_section_1")) return false;
    while (true) {
      int c = current_position_(b);
      if (!var_section_1_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "var_section_1", c)) break;
    }
    return true;
  }

  // ( var_decl )
  private static boolean var_section_1_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "var_section_1_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = var_decl(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  /* ********************************************************** */
  // 'WHILE' expr 'DO'
  //         [stmt_seq]
  //     'END'
  public static boolean while_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_stmt")) return false;
    if (!nextTokenIs(b, WHILE)) return false;
    boolean r, p;
    Marker m = enter_section_(b, l, _NONE_, WHILE_STMT, null);
    r = consumeToken(b, WHILE);
    p = r; // pin = 1
    r = r && report_error_(b, expr(b, l + 1));
    r = p && report_error_(b, consumeToken(b, DO)) && r;
    r = p && report_error_(b, while_stmt_3(b, l + 1)) && r;
    r = p && consumeToken(b, END) && r;
    exit_section_(b, l, m, r, p, null);
    return r || p;
  }

  // [stmt_seq]
  private static boolean while_stmt_3(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "while_stmt_3")) return false;
    stmt_seq(b, l + 1);
    return true;
  }

  /* ********************************************************** */
  // 'WITH' guard 'DO' stmt_seq
  //         ( '|' guard 'DO' stmt_seq )*
  //         [ 'ELSE' stmt_seq ]
  //     'END'
  public static boolean with_stmt(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "with_stmt")) return false;
    if (!nextTokenIs(b, WITH)) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, WITH);
    r = r && guard(b, l + 1);
    r = r && consumeToken(b, DO);
    r = r && stmt_seq(b, l + 1);
    r = r && with_stmt_4(b, l + 1);
    r = r && with_stmt_5(b, l + 1);
    r = r && consumeToken(b, END);
    exit_section_(b, m, WITH_STMT, r);
    return r;
  }

  // ( '|' guard 'DO' stmt_seq )*
  private static boolean with_stmt_4(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "with_stmt_4")) return false;
    while (true) {
      int c = current_position_(b);
      if (!with_stmt_4_0(b, l + 1)) break;
      if (!empty_element_parsed_guard_(b, "with_stmt_4", c)) break;
    }
    return true;
  }

  // '|' guard 'DO' stmt_seq
  private static boolean with_stmt_4_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "with_stmt_4_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, PIPE);
    r = r && guard(b, l + 1);
    r = r && consumeToken(b, DO);
    r = r && stmt_seq(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

  // [ 'ELSE' stmt_seq ]
  private static boolean with_stmt_5(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "with_stmt_5")) return false;
    with_stmt_5_0(b, l + 1);
    return true;
  }

  // 'ELSE' stmt_seq
  private static boolean with_stmt_5_0(PsiBuilder b, int l) {
    if (!recursion_guard_(b, l, "with_stmt_5_0")) return false;
    boolean r;
    Marker m = enter_section_(b);
    r = consumeToken(b, ELSE);
    r = r && stmt_seq(b, l + 1);
    exit_section_(b, m, null, r);
    return r;
  }

}
