// This is a generated file. Not intended for manual editing.
package de.rholambdapi.o2a.intellij.lang.oberon2.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface OberonStmt extends PsiElement {

  @Nullable
  OberonAssignStmt getAssignStmt();

  @Nullable
  OberonCaseStmt getCaseStmt();

  @Nullable
  OberonExpr getExpr();

  @Nullable
  OberonForStmt getForStmt();

  @Nullable
  OberonIfStmt getIfStmt();

  @Nullable
  OberonLoopStmt getLoopStmt();

  @Nullable
  OberonProcCallStmt getProcCallStmt();

  @Nullable
  OberonRepeatStmt getRepeatStmt();

  @Nullable
  OberonWhileStmt getWhileStmt();

  @Nullable
  OberonWithStmt getWithStmt();

}
