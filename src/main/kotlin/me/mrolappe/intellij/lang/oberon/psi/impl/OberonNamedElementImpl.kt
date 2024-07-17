package me.mrolappe.intellij.lang.oberon.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import me.mrolappe.intellij.lang.oberon.psi.OberonNamedElement

abstract class OberonNamedElementImpl(node: ASTNode) : ASTWrapperPsiElement(node), OberonNamedElement
