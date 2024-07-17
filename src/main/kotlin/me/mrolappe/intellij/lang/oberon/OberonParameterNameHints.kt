package me.mrolappe.intellij.lang.oberon

import com.intellij.codeInsight.hints.InlayInfo
import com.intellij.codeInsight.hints.InlayParameterHintsProvider
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.PsiUtilCore
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import com.intellij.refactoring.suggested.startOffset
import me.mrolappe.intellij.lang.oberon.psi.*
import me.mrolappe.intellij.lang.oberon.psi.OberonTypes.NIL
import me.mrolappe.intellij.lang.oberon.psi.impl.designatorString

class OberonParameterNameHints : InlayParameterHintsProvider {
    companion object {
        private val LOG = thisLogger()
    }

    override fun getParameterHints(element: PsiElement): List<InlayInfo> {
        return when (element) {
            is OberonProcCallStmt -> parameterHintsFor(element)
            else -> emptyList<InlayInfo>()
        }
    }

    private fun parameterHintsFor(procCall: OberonProcCallStmt): List<InlayInfo> {
        if (!procCall.hasAnyArguments) return emptyList()

        val resolvedElement = procCall.procedureDesignator.reference?.resolve() ?: return emptyList()

        val procDecl = when (resolvedElement) {
            is OberonProcedureDecl -> resolvedElement
            is OberonProcDeclName -> resolvedElement.parentOfType<OberonProcedureDecl>() ?: return emptyList()
            is OberonIdentDef -> resolvedElement.parentOfType<OberonProcedureDecl>() ?: return emptyList()
            is OberonFormalParamName -> {
                LOG.error("TODO procdecl via formal param type definition")
                return emptyList()
            }
            else -> throw IllegalStateException("Designator resolved to unexpected element type; proc designator: ${procCall.procedureDesignator.designatorString}, element: $resolvedElement")
        }

        val parameterNames = procDecl.formalParams?.formalParamSectionList
            ?.flatMap { it.formalParamNameList }
            ?.map { it.paramName.text }
            ?: return emptyList()   // TODO log problem or throw?

//        println("parameter names: $parameterNames")

        return procCall.actualParams?.exprList?.exprList
            ?.mapIndexedNotNull { idx, expr -> if (expr.isLiteral) InlayInfo(parameterNames[idx], expr.startOffset) else null }
            ?: emptyList()
    }

    override fun getDefaultBlackList() = emptySet<String>()
}

val OberonProcCallStmt.hasAnyArguments: Boolean
    get() = this.actualParams?.exprList?.exprList?.isNotEmpty() ?: false

// TODO use mixed-in functionality after cleaning up/optimizing expr node hierarchy
val OberonExpr.isLiteral: Boolean
    get() = this.simpleExprList.size == 1 && this.simpleExprList.first().isLiteral

val OberonSimpleExpr.isLiteral: Boolean
    get() = this.termList.size == 1 && this.termList.first().isLiteral

val OberonTerm.isLiteral: Boolean
    get() = this.factorList.size == 1 && this.factorList.first().isLiteral

val OberonFactor.isLiteral: Boolean
    get() = this.intLit != null || this.realLit != null || this.setLit != null || this.charConst != null || this.strLitMulti != null
            || this.node.findChildByType(NIL) != null
            || this.designator != null && setOf<String>("FALSE", "TRUE").contains(this.text)    // TODO find a good solution for check
/*|| etc.*/
