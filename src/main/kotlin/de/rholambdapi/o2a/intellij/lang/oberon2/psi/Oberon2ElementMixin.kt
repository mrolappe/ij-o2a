package de.rholambdapi.o2a.intellij.lang.oberon2.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.icons.AllIcons
import com.intellij.lang.ASTNode
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.psi.PsiNameIdentifierOwner
import javax.swing.Icon

abstract class Oberon2ElementMixin(node: ASTNode) : ASTWrapperPsiElement(node) {
    private val log = thisLogger()

    override fun getIcon(flags: Int): Icon? {
        return when (this) {
            is Oberon2ProcedureElement, is OberonOberonALibProcDecl -> AllIcons.Nodes.Function
            is Oberon2ModuleElement -> AllIcons.Nodes.Package
            else -> {
                log.info("No icon for element: $this")
                super.getIcon(flags)
            }
        }
    }

    override fun getName(): String? {
        return when (this) {
            is PsiNameIdentifierOwner -> nameIdentifier?.text
            else -> "??? Oberon2ElementMixin::getName($this)"
        }
    }
}
