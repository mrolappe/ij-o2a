// This is a generated file. Not intended for manual editing.
package de.rholambdapi.o2a.intellij.lang.oberon2.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.navigation.ItemPresentation;

public interface OberonModuleDef extends Oberon2ModuleElement {

  @NotNull
  OberonModuleHead getModuleHead();

  @NotNull
  OberonModuleTail getModuleTail();

  @NotNull
  OberonTopLevelDecls getTopLevelDecls();

  @NotNull String getName();

  @NotNull PsiElement setName(@NotNull String newName);

  @Nullable PsiElement getNameIdentifier();

  @NotNull ItemPresentation getPresentation();

  //WARNING: getIcon(...) is skipped
  //matching getIcon(OberonModuleDef, ...)
  //methods are not found in OberonPsiImplUtil

}
