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
import com.intellij.navigation.ItemPresentation;

public class OberonModuleDefImpl extends Oberon2ElementMixin implements OberonModuleDef {

  public OberonModuleDefImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull OberonVisitor visitor) {
    visitor.visitModuleDef(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof OberonVisitor) accept((OberonVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public OberonModuleHead getModuleHead() {
    return findNotNullChildByClass(OberonModuleHead.class);
  }

  @Override
  @NotNull
  public OberonModuleTail getModuleTail() {
    return findNotNullChildByClass(OberonModuleTail.class);
  }

  @Override
  @NotNull
  public OberonTopLevelDecls getTopLevelDecls() {
    return findNotNullChildByClass(OberonTopLevelDecls.class);
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
  public @Nullable PsiElement getNameIdentifier() {
    return OberonPsiImplUtil.getNameIdentifier(this);
  }

  @Override
  public @NotNull ItemPresentation getPresentation() {
    return OberonPsiImplUtil.getPresentation(this);
  }

}
