// This is a generated file. Not intended for manual editing.
package de.rholambdapi.o2a.intellij.lang.oberon2.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;

public interface OberonDesignator extends PsiElement {

  @NotNull
  List<OberonExprList> getExprListList();

  @NotNull
  List<OberonQualIdent> getQualIdentList();

  @NotNull PsiReference getReference();

}
