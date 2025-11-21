// This is a generated file. Not intended for manual editing.
package de.rholambdapi.o2a.intellij.lang.oberon2.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.*;

public class OberonStmtImpl extends ASTWrapperPsiElement implements OberonStmt {

  public OberonStmtImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull OberonVisitor visitor) {
    visitor.visitStmt(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof OberonVisitor) accept((OberonVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public OberonAssignStmt getAssignStmt() {
    return findChildByClass(OberonAssignStmt.class);
  }

  @Override
  @Nullable
  public OberonCaseStmt getCaseStmt() {
    return findChildByClass(OberonCaseStmt.class);
  }

  @Override
  @Nullable
  public OberonExpr getExpr() {
    return findChildByClass(OberonExpr.class);
  }

  @Override
  @Nullable
  public OberonForStmt getForStmt() {
    return findChildByClass(OberonForStmt.class);
  }

  @Override
  @Nullable
  public OberonIfStmt getIfStmt() {
    return findChildByClass(OberonIfStmt.class);
  }

  @Override
  @Nullable
  public OberonLoopStmt getLoopStmt() {
    return findChildByClass(OberonLoopStmt.class);
  }

  @Override
  @Nullable
  public OberonProcCallStmt getProcCallStmt() {
    return findChildByClass(OberonProcCallStmt.class);
  }

  @Override
  @Nullable
  public OberonRepeatStmt getRepeatStmt() {
    return findChildByClass(OberonRepeatStmt.class);
  }

  @Override
  @Nullable
  public OberonWhileStmt getWhileStmt() {
    return findChildByClass(OberonWhileStmt.class);
  }

  @Override
  @Nullable
  public OberonWithStmt getWithStmt() {
    return findChildByClass(OberonWithStmt.class);
  }

}
