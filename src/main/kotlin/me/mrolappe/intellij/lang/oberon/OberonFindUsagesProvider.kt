package me.mrolappe.intellij.lang.oberon

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.NlsSafe
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import me.mrolappe.intellij.lang.oberon.psi.*
import me.mrolappe.intellij.lang.oberon.psi.impl.procedureName

class OberonFindUsagesProvider : FindUsagesProvider {
    companion object {
        val LOG = Logger.getInstance(OberonFindUsagesProvider::class.java)
    }

    override fun getWordsScanner() = DefaultWordsScanner(
        OberonLexerAdapter(),
        TokenSet.create(OberonTypes.IDENT),
        TokenSet.create(OberonTypes.COMMENT),
        TokenSet.create(OberonTypes.STR_LIT)
    )

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean {
        val canFind = psiElement.parent is OberonProcedureDecl || psiElement is OberonConstDeclName || psiElement is OberonVarDeclName
                || psiElement is OberonTypeDeclName || psiElement is OberonImportAlias || psiElement is OberonModuleDefName
                || psiElement is OberonFormalParamName || psiElement is OberonReceiverName
        LOG.debug("OberonFindUsagesProvider::canFindUsagesFor, psiElement: $psiElement (parent: ${psiElement.parent}) -> $canFind")
        return canFind
    }

    override fun getHelpId(psiElement: PsiElement) = null

    override fun getType(element: PsiElement): String {
        val type = when (element) {
            is OberonConstDeclName -> "constant"
            is OberonFormalParamName -> "formal parameter"
            is OberonImportAlias -> "import alias"
            is OberonProcedureDecl -> "procedure"
            is OberonReceiverName -> "receiver"
            is OberonTypeDeclName -> "type"
            is OberonVarDeclName -> "variable"
            else -> "??? usage type"
        }

        LOG.debug("OberonFindUsagesProvider::getType, element: $element, element parent: ${element.parent} -> $type")
        return type
    }

    override fun getDescriptiveName(element: PsiElement): @NlsSafe String {
        val descriptiveName = when (val parent = element.parent) {
            is OberonProcedureDecl -> parent.procedureName

            else -> when (element) {
                is OberonConstDeclName -> element.constantName.text
                is OberonFormalParamName -> element.paramName.text
                is OberonImportAlias -> element.aliasName.text
                is OberonProcedureDecl -> element.procedureName
                is OberonReceiverName -> element.text
                is OberonTypeDeclName -> element.typeName.text
                is OberonVarDeclName -> element.varName.text
                else -> "??? descriptive name"
            }
        }

        LOG.debug("OberonFindUsagesProvider::getDescriptiveName, element: $element -> $descriptiveName")
        return descriptiveName!!
    }

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String {
        val nodeText = when (val parent = element.parent) {
//            "node text for find usages tree for ${(element.parent as OberonProcedureDecl).identDef.text}"
            is OberonProcedureDecl -> parent.procDeclName?.procedureName?.text

            else -> when (element) {
                is OberonVarDeclName -> element.varName.text
                is OberonTypeDeclName -> element.typeName.text
                else -> "??? nodeText"
            }
        }
        LOG.debug("OberonFindUsagesProvider::getNodeText, element: $element, useFullName: $useFullName -> $nodeText")
        return nodeText!!
    }
}