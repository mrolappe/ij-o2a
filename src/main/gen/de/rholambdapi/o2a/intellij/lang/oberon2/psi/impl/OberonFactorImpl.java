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

public class OberonFactorImpl extends ASTWrapperPsiElement implements OberonFactor {

  public OberonFactorImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull OberonVisitor visitor) {
    visitor.visitFactor(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof OberonVisitor) accept((OberonVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public OberonActualParams getActualParams() {
    return findChildByClass(OberonActualParams.class);
  }

  @Override
  @Nullable
  public OberonDesignator getDesignator() {
    return findChildByClass(OberonDesignator.class);
  }

  @Override
  @Nullable
  public OberonExpr getExpr() {
    return findChildByClass(OberonExpr.class);
  }

  @Override
  @Nullable
  public OberonFactor getFactor() {
    return findChildByClass(OberonFactor.class);
  }

  @Override
  @Nullable
  public OberonSetLit getSetLit() {
    return findChildByClass(OberonSetLit.class);
  }

  @Override
  @Nullable
  public OberonStrLitMulti getStrLitMulti() {
    return findChildByClass(OberonStrLitMulti.class);
  }

  @Override
  @Nullable
  public PsiElement getCharConst() {
    return findChildByType(CHAR_CONST);
  }

  @Override
  @Nullable
  public PsiElement getIntLit() {
    return findChildByType(INT_LIT);
  }

  @Override
  @Nullable
  public PsiElement getRealLit() {
    return findChildByType(REAL_LIT);
  }

}
