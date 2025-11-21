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

public class OberonSimpleExprImpl extends ASTWrapperPsiElement implements OberonSimpleExpr {

  public OberonSimpleExprImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull OberonVisitor visitor) {
    visitor.visitSimpleExpr(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof OberonVisitor) accept((OberonVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<OberonAddOp> getAddOpList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, OberonAddOp.class);
  }

  @Override
  @NotNull
  public List<OberonTerm> getTermList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, OberonTerm.class);
  }

}
