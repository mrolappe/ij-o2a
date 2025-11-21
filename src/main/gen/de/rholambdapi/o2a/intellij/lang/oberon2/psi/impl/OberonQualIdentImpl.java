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
import com.intellij.psi.PsiReference;

public class OberonQualIdentImpl extends ASTWrapperPsiElement implements OberonQualIdent {

  public OberonQualIdentImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull OberonVisitor visitor) {
    visitor.visitQualIdent(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof OberonVisitor) accept((OberonVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public OberonQualIdentQualified getQualIdentQualified() {
    return findChildByClass(OberonQualIdentQualified.class);
  }

  @Override
  @Nullable
  public OberonQualIdentSimple getQualIdentSimple() {
    return findChildByClass(OberonQualIdentSimple.class);
  }

  @Override
  public @NotNull PsiReference @NotNull [] getReferences() {
    return OberonPsiImplUtil.getReferences(this);
  }

}
