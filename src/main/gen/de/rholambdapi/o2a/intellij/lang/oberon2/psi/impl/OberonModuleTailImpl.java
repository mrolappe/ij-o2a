// This is a generated file. Not intended for manual editing.
package de.rholambdapi.o2a.intellij.lang.oberon2.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.*;

public class OberonModuleTailImpl extends ASTWrapperPsiElement implements OberonModuleTail {

  public OberonModuleTailImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull OberonVisitor visitor) {
    visitor.visitModuleTail(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof OberonVisitor) accept((OberonVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public OberonModuleInit getModuleInit() {
    return findChildByClass(OberonModuleInit.class);
  }

  @Override
  @Nullable
  public OberonStmtSeq getStmtSeq() {
    return findChildByClass(OberonStmtSeq.class);
  }

  @Override
  @Nullable
  public PsiElement getEndKeyword() {
    return findChildByType(END);
  }

  @Override
  @Nullable
  public PsiElement getEndIdentifier() {
    return findChildByType(IDENT);
  }

}
