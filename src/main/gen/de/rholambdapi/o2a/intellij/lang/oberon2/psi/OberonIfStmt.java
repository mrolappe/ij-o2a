// This is a generated file. Not intended for manual editing.
package de.rholambdapi.o2a.intellij.lang.oberon2.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface OberonIfStmt extends PsiElement {

  @Nullable
  OberonElseBranch getElseBranch();

  @NotNull
  List<OberonElsifBranch> getElsifBranchList();

  @NotNull
  OberonExpr getConditionExpression();

  //WARNING: stmt(...) is skipped
  //matching stmt(OberonIfStmt, ...)
  //methods are not found in OberonPsiImplUtil

  @Nullable
  OberonStmtSeq getThenBlock();

}
