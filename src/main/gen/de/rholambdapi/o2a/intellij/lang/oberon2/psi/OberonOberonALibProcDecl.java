// This is a generated file. Not intended for manual editing.
package de.rholambdapi.o2a.intellij.lang.oberon2.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface OberonOberonALibProcDecl extends Oberon2ExportableDeclarationElement {

  @Nullable
  OberonFormalType getFormalType();

  @NotNull
  OberonIdentDef getIdentDef();

  @Nullable
  OberonOberonAFormalParameters getOberonAFormalParameters();

  @NotNull
  OberonOberonASharedLibLvData getOberonASharedLibLvData();

  int getTextOffset();

  @NotNull PsiElement getNameIdentifier();

  @NotNull PsiElement setName(@NotNull String newName);

  boolean isMarkedForExport();

  @Nullable PsiElement getReadWriteExportMark();

  @Nullable PsiElement getReadOnlyExportMark();

}
