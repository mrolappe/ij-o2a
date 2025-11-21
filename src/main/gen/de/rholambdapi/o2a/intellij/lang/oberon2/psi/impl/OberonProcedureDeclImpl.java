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

public class OberonProcedureDeclImpl extends Oberon2ElementMixin implements OberonProcedureDecl {

  public OberonProcedureDeclImpl(ASTNode node) {
    super(node);
  }

  public void accept(@NotNull OberonVisitor visitor) {
    visitor.visitProcedureDecl(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof OberonVisitor) accept((OberonVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<OberonAmigaOberonExternalProcDecl> getAmigaOberonExternalProcDeclList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, OberonAmigaOberonExternalProcDecl.class);
  }

  @Override
  @NotNull
  public List<OberonAmigaOberonSharedLibProcDecl> getAmigaOberonSharedLibProcDeclList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, OberonAmigaOberonSharedLibProcDecl.class);
  }

  @Override
  @NotNull
  public List<OberonConstSection> getConstSectionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, OberonConstSection.class);
  }

  @Override
  @Nullable
  public OberonFormalParams getFormalParams() {
    return findChildByClass(OberonFormalParams.class);
  }

  @Override
  @NotNull
  public List<OberonForwardDecl> getForwardDeclList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, OberonForwardDecl.class);
  }

  @Override
  @NotNull
  public List<OberonOberonAExternalProcDecl> getOberonAExternalProcDeclList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, OberonOberonAExternalProcDecl.class);
  }

  @Override
  @NotNull
  public List<OberonOberonALibProcDecl> getOberonALibProcDeclList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, OberonOberonALibProcDecl.class);
  }

  @Override
  @NotNull
  public OberonProcDeclName getProcDeclName() {
    return findNotNullChildByClass(OberonProcDeclName.class);
  }

  @Override
  @NotNull
  public List<OberonProcedureDecl> getProcedureDeclList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, OberonProcedureDecl.class);
  }

  @Override
  @Nullable
  public OberonProcedureDeclBodyBlock getProcedureDeclBodyBlock() {
    return findChildByClass(OberonProcedureDeclBodyBlock.class);
  }

  @Override
  @Nullable
  public OberonReceiverName getReceiverName() {
    return findChildByClass(OberonReceiverName.class);
  }

  @Override
  @Nullable
  public OberonReceiverType getReceiverType() {
    return findChildByClass(OberonReceiverType.class);
  }

  @Override
  @NotNull
  public List<OberonTypeSection> getTypeSectionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, OberonTypeSection.class);
  }

  @Override
  @NotNull
  public List<OberonVarSection> getVarSectionList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, OberonVarSection.class);
  }

  @Override
  @Nullable
  public PsiElement getIntLit() {
    return findChildByType(INT_LIT);
  }

  @Override
  public int getTextOffset() {
    return OberonPsiImplUtil.getTextOffset(this);
  }

  @Override
  public boolean markedForExport() {
    return OberonPsiImplUtil.markedForExport(this);
  }

  @Override
  public @Nullable PsiElement getNameIdentifier() {
    return OberonPsiImplUtil.getNameIdentifier(this);
  }

  @Override
  public @NotNull PsiElement setName(@NotNull String newName) {
    return OberonPsiImplUtil.setName(this, newName);
  }

  @Override
  public @Nullable PsiElement getReadOnlyExportMark() {
    return OberonPsiImplUtil.getReadOnlyExportMark(this);
  }

  @Override
  public @Nullable PsiElement getReadWriteExportMark() {
    return OberonPsiImplUtil.getReadWriteExportMark(this);
  }

}
