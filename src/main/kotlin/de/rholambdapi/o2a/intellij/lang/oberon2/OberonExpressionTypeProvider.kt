package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.lang.ExpressionTypeProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil

class OberonExpressionTypeProvider : ExpressionTypeProvider<de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonExpr>() {
    override fun getInformationHint(element: de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonExpr): String {
        // TODO
        return "oberon expr type: TODO"
    }

    override fun getErrorHint(): String {
        return "oberon expr type: je ne sais pas"
    }

    override fun getExpressionsAt(elementAt: PsiElement): MutableList<de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonExpr> {
        val expr = PsiTreeUtil.getParentOfType(elementAt, de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonExpr::class.java)
        val result = mutableListOf<de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonExpr>()

        if (expr != null) {
            result += expr
        }

        return result
    }
}