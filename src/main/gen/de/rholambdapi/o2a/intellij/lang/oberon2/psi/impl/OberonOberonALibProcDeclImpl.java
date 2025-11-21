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

public class OberonOberonALibProcDeclImpl extends Oberon2ElementMixin implements OberonOberonALibProcDecl {

  public OberonOberonALibProcDeclImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull OberonVisitor visitor) {
    visitor.visitOberonALibProcDecl(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof OberonVisitor) accept((OberonVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public OberonFormalType getFormalType() {
    return findChildByClass(OberonFormalType.class);
  }

  @Override
  @NotNull
  public OberonIdentDef getIdentDef() {
    return findNotNullChildByClass(OberonIdentDef.class);
  }

  @Override
  @Nullable
  public OberonOberonAFormalParameters getOberonAFormalParameters() {
    return findChildByClass(OberonOberonAFormalParameters.class);
  }

  @Override
  @NotNull
  public OberonOberonASharedLibLvData getOberonASharedLibLvData() {
    return findNotNullChildByClass(OberonOberonASharedLibLvData.class);
  }

  @Override
  public int getTextOffset() {
    return OberonPsiImplUtil.getTextOffset(this);
  }

  @Override
  public @NotNull PsiElement getNameIdentifier() {
    return OberonPsiImplUtil.getNameIdentifier(this);
  }

  @Override
  public @NotNull PsiElement setName(@NotNull String newName) {
    return OberonPsiImplUtil.setName(this, newName);
  }

  @Override
  public boolean isMarkedForExport() {
    return OberonPsiImplUtil.isMarkedForExport(this);
  }

  @Override
  public @Nullable PsiElement getReadWriteExportMark() {
    return OberonPsiImplUtil.getReadWriteExportMark(this);
  }

  @Override
  public @Nullable PsiElement getReadOnlyExportMark() {
    return OberonPsiImplUtil.getReadOnlyExportMark(this);
  }

}
