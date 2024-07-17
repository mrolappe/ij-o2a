package me.mrolappe.intellij.lang.oberon

import com.intellij.lang.ExpressionTypeProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import me.mrolappe.intellij.lang.oberon.psi.OberonExpr

class OberonExpressionTypeProvider : ExpressionTypeProvider<OberonExpr>() {
    override fun getInformationHint(element: OberonExpr): String {
        // TODO
        return "oberon expr type: TODO"
    }

    override fun getErrorHint(): String {
        return "oberon expr type: je ne sais pas"
    }

    override fun getExpressionsAt(elementAt: PsiElement): MutableList<OberonExpr> {
        val expr = PsiTreeUtil.getParentOfType(elementAt, OberonExpr::class.java)
        val result = mutableListOf<OberonExpr>()

        if (expr != null) {
            result += expr
        }

        return result
    }
}