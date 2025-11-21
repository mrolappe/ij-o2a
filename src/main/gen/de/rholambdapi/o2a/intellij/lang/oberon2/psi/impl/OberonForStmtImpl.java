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

public class OberonForStmtImpl extends ASTWrapperPsiElement implements OberonForStmt {

  public OberonForStmtImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull OberonVisitor visitor) {
    visitor.visitForStmt(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof OberonVisitor) accept((OberonVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public OberonConstExpr getConstExpr() {
    return findChildByClass(OberonConstExpr.class);
  }

  @Override
  @NotNull
  public List<OberonExpr> getExprList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, OberonExpr.class);
  }

  @Override
  @Nullable
  public OberonStmtSeq getStmtSeq() {
    return findChildByClass(OberonStmtSeq.class);
  }

  @Override
  @NotNull
  public PsiElement getIdent() {
    return findNotNullChildByType(IDENT);
  }

}
