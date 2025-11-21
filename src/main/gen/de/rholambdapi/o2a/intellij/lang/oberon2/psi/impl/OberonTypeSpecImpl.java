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

public class OberonTypeSpecImpl extends ASTWrapperPsiElement implements OberonTypeSpec {

  public OberonTypeSpecImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull OberonVisitor visitor) {
    visitor.visitTypeSpec(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof OberonVisitor) accept((OberonVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @Nullable
  public OberonArrayTypeSpec getArrayTypeSpec() {
    return findChildByClass(OberonArrayTypeSpec.class);
  }

  @Override
  @Nullable
  public OberonFormalParams getFormalParams() {
    return findChildByClass(OberonFormalParams.class);
  }

  @Override
  @Nullable
  public OberonPointerTypeSpec getPointerTypeSpec() {
    return findChildByClass(OberonPointerTypeSpec.class);
  }

  @Override
  @Nullable
  public OberonQualIdent getQualIdent() {
    return findChildByClass(OberonQualIdent.class);
  }

  @Override
  @Nullable
  public OberonRecordTypeSpec getRecordTypeSpec() {
    return findChildByClass(OberonRecordTypeSpec.class);
  }

  @Override
  @Nullable
  public OberonStructTypeSpec getStructTypeSpec() {
    return findChildByClass(OberonStructTypeSpec.class);
  }

}
