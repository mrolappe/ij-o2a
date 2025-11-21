// This is a generated file. Not intended for manual editing.
package de.rholambdapi.o2a.intellij.lang.oberon2.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.navigation.ItemPresentation;

public interface OberonModuleHead extends OberonNamedElement {

  @Nullable
  OberonImportList getImportList();

  @Nullable
  PsiElement getIntLit();

  @NotNull
  PsiElement getModuleName();

  @NotNull String getName();

  @NotNull PsiElement setName(@NotNull String newName);

  @NotNull PsiElement getNameIdentifier();

  @NotNull ItemPresentation getPresentation();

}
