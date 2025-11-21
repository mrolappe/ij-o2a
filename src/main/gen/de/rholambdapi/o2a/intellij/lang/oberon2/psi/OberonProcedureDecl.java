// This is a generated file. Not intended for manual editing.
package de.rholambdapi.o2a.intellij.lang.oberon2.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface OberonProcedureDecl extends Oberon2ProcedureElement {

  @NotNull
  List<OberonAmigaOberonExternalProcDecl> getAmigaOberonExternalProcDeclList();

  @NotNull
  List<OberonAmigaOberonSharedLibProcDecl> getAmigaOberonSharedLibProcDeclList();

  @NotNull
  List<OberonConstSection> getConstSectionList();

  @Nullable
  OberonFormalParams getFormalParams();

  @NotNull
  List<OberonForwardDecl> getForwardDeclList();

  @NotNull
  List<OberonOberonAExternalProcDecl> getOberonAExternalProcDeclList();

  @NotNull
  List<OberonOberonALibProcDecl> getOberonALibProcDeclList();

  @NotNull
  OberonProcDeclName getProcDeclName();

  @NotNull
  List<OberonProcedureDecl> getProcedureDeclList();

  @Nullable
  OberonProcedureDeclBodyBlock getProcedureDeclBodyBlock();

  @Nullable
  OberonReceiverName getReceiverName();

  @Nullable
  OberonReceiverType getReceiverType();

  @NotNull
  List<OberonTypeSection> getTypeSectionList();

  @NotNull
  List<OberonVarSection> getVarSectionList();

  @Nullable
  PsiElement getIntLit();

  int getTextOffset();

  boolean markedForExport();

  @Nullable PsiElement getNameIdentifier();

  @NotNull PsiElement setName(@NotNull String newName);

  @Nullable PsiElement getReadOnlyExportMark();

  @Nullable PsiElement getReadWriteExportMark();

  //WARNING: getIcon(...) is skipped
  //matching getIcon(OberonProcedureDecl, ...)
  //methods are not found in OberonPsiImplUtil

}
