// This is a generated file. Not intended for manual editing.
package de.rholambdapi.o2a.intellij.lang.oberon2.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface OberonTopLevelDecls extends PsiElement {

  @NotNull
  List<OberonAmigaOberonExternalProcDecl> getAmigaOberonExternalProcDeclList();

  @NotNull
  List<OberonAmigaOberonSharedLibProcDecl> getAmigaOberonSharedLibProcDeclList();

  @NotNull
  List<OberonConstSection> getConstSectionList();

  @NotNull
  List<OberonForwardDecl> getForwardDeclList();

  @NotNull
  List<OberonOberonAExternalProcDecl> getOberonAExternalProcDeclList();

  @NotNull
  List<OberonOberonALibProcDecl> getOberonALibProcDeclList();

  @NotNull
  List<OberonProcedureDecl> getProcedureDeclList();

  @NotNull
  List<OberonTypeSection> getTypeSectionList();

  @NotNull
  List<OberonVarSection> getVarSectionList();

}
