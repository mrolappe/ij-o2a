package de.rholambdapi.o2a.intellij.lang.oberon2.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonNamedElement

abstract class OberonNamedElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), OberonNamedElement
