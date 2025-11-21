// This is a generated file. Not intended for manual editing.
package de.rholambdapi.o2a.intellij.lang.oberon2.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface OberonConstDeclName extends OberonNamedElement {

  @NotNull
  PsiElement getConstantName();

  @Nullable
  PsiElement getReadWriteExportMark();

  @Nullable
  PsiElement getReadOnlyExportMark();

  @NotNull String getName();

  @NotNull PsiElement setName(@NotNull String newName);

  boolean markedForExport();

}
