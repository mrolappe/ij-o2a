// This is a generated file. Not intended for manual editing.
package de.rholambdapi.o2a.intellij.lang.oberon2.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface OberonFactor extends PsiElement {

  @Nullable
  OberonActualParams getActualParams();

  @Nullable
  OberonDesignator getDesignator();

  @Nullable
  OberonExpr getExpr();

  @Nullable
  OberonFactor getFactor();

  @Nullable
  OberonSetLit getSetLit();

  @Nullable
  OberonStrLitMulti getStrLitMulti();

  @Nullable
  PsiElement getCharConst();

  @Nullable
  PsiElement getIntLit();

  @Nullable
  PsiElement getRealLit();

  //WARNING: isNil(...) is skipped
  //matching isNil(OberonFactor, ...)
  //methods are not found in OberonPsiImplUtil

}
