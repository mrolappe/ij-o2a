// This is a generated file. Not intended for manual editing.
package de.rholambdapi.o2a.intellij.lang.oberon2.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonTypes.*;
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.*;

public class OberonProcDeclNameImpl extends OberonNamedElementImpl implements OberonProcDeclName {

  public OberonProcDeclNameImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull OberonVisitor visitor) {
    visitor.visitProcDeclName(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof OberonVisitor) accept((OberonVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public PsiElement getProcedureName() {
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

}
