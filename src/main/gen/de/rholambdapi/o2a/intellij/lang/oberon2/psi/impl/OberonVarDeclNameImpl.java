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

public class OberonVarDeclNameImpl extends ASTWrapperPsiElement implements OberonVarDeclName {

  public OberonVarDeclNameImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull OberonVisitor visitor) {
    visitor.visitVarDeclName(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof OberonVisitor) accept((OberonVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public OberonIntConstExpr getIntConstExpr() {
    return findChildByClass(OberonIntConstExpr.class);
  }

  @Override
  @Nullable
  public PsiElement getStrLit() {
    return findChildByType(STR_LIT);
  }

  @Override
  @NotNull
  public PsiElement getVarName() {
    return findNotNullChildByType(IDENT);
  }

  @Override
  @Nullable
  public PsiElement getReadWriteExportMark() {
    return findChildByType(STAR);
  }

  @Override
  @Nullable
  public PsiElement getReadOnlyExportMark() {
    return findChildByType(MINUS);
  }

  @Override
  public @NotNull String getName() {
    return OberonPsiImplUtil.getName(this);
  }

  @Override
  public @NotNull PsiElement setName(@NotNull String newName) {
    return OberonPsiImplUtil.setName(this, newName);
  }

  @Override
  public @NotNull PsiElement getNameIdentifier() {
    return OberonPsiImplUtil.getNameIdentifier(this);
  }

}
