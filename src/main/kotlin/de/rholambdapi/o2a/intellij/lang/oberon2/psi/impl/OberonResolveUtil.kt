package de.rholambdapi.o2a.intellij.lang.oberon2.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.parentOfType
import de.rholambdapi.o2a.intellij.lang.oberon2.*
import de.rholambdapi.o2a.intellij.lang.oberon2.OberonUtil
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.*

internal fun moduleContaining(element: PsiElement): OberonModuleDef? = element.parentOfType<OberonModuleDef>()

internal fun moduleNameForImportNameOrNull(module: OberonModuleDef, name: String): String? {
    return module.importList?.importDeclList
        ?.firstOrNull { it.importAlias?.aliasName?.textMatches(name) ?: false || it.importModuleReference.ident.textMatches(name) }
        ?.importModuleReference?.ident?.text
}

internal fun procedureDeclContaining(element: PsiElement): OberonProcedureDecl? {
    val parentOfType = element.parentOfType<OberonProcedureDecl>()
    return parentOfType
}

internal fun topLevelProcedureDeclNames(module: OberonModuleDef): List<OberonProcedureDecl> {
    return PsiTreeUtil.getChildrenOfTypeAsList(module, OberonProcedureDecl::class.java)
}

internal fun topLevelConstantNames(module: OberonModuleDef): List<OberonConstDeclName> =
    PsiTreeUtil.getChildrenOfTypeAsList(module, OberonConstSection::class.java)
        .flatMap { PsiTreeUtil.findChildrenOfType(it, OberonConstDeclName::class.java) }

internal fun topLevelTypeDecls(module: OberonModuleDef): List<OberonTypeDecl> =
    module.typeSectionList
        .flatMap { section -> section.typeDeclList }

internal fun topLevelVarDeclNames(module: OberonModuleDef): List<OberonVarDeclName> = topLevelVarSections(module)
    .flatMap { PsiTreeUtil.findChildrenOfType(it, OberonVarDeclName::class.java) }

internal fun topLevelVarSections(module: OberonModuleDef): List<OberonVarSection> =
    PsiTreeUtil.getChildrenOfTypeAsList(module, OberonVarSection::class.java)

internal fun resolveByModuleNameAndMemberName(referencingElement: PsiElement, moduleName: String, memberName: String): List<OberonNamedElement> {
    val resolveResults = mutableListOf<OberonNamedElement>()

    OberonUtil.findModulesByName(referencingElement.project, moduleName)
        .forEach { module ->
            findConstDeclNameExportedByModule(memberName, module)
                ?.let { resolveResults.add(it) }

            findProcedureDeclNameInFile(module.containingFile, memberName)?.procDeclName
                ?.let { resolveResults.add(it) }

            findOberonASharedLibraryProcedureDeclExportedByModule(memberName, module)
                ?.let { resolveResults.add(it.procDeclName) }

            findTypeDeclNameExportedByModule(memberName, module)
                ?.let { resolveResults.add(it) }

            findVarDeclNameExportedByModule(memberName, module)
                ?.let { resolveResults.add(it) }
        }

    return resolveResults
}

internal fun resolveTypeByNameInModule(typeName: String, module: OberonModuleDef): OberonTypeDecl? {
    val typeDecls = topLevelTypeDecls(module)
    return typeDecls.firstOrNull {
        val matches = it.typeNameMatches(typeName)
        matches
    }
}

internal fun resolveByMemberName(
    referencingElement: PsiElement,
    memberName: String
): List<OberonNamedElement> {
    val resolveResults = mutableListOf<OberonNamedElement>()

    val containingProcedure = procedureDeclContaining(referencingElement)
    val proceduresToSearch = containingProcedure?.let { parentProcedureDeclsOf(it) + it }

//    println("resolveByMemberName, containingProcedure: $containingProcedure, proceduresToSearch: $proceduresToSearch")

    proceduresToSearch?.forEach { procedure ->
        procedureLocalConstDeclNameOrNull(procedure, memberName)
            ?.let { resolveResults.add(it) }

        procedureParamOrNull(procedure, memberName)
            ?.let { resolveResults.add(it) }

        procedureLocalProcDeclNameOrNull(procedure, memberName)
            ?.let { resolveResults.add(it) }

        procedureLocalVarDeclNameOrNull(procedure, memberName)
            ?.let { resolveResults.add(it) }
    }

    moduleContaining(referencingElement)
        ?.let { module ->
            topLevelConstantNames(module)
                .filter { constDeclName -> constDeclName.constantName.textMatches(memberName) }
                .let { constDeclNames -> resolveResults.addAll(constDeclNames) }

            topLevelProcedureDeclNames(module)
                .filter { procedureDecl -> procedureDecl.procedureNameMatches(memberName) }
                .map { it.procDeclName }
                .let { procedureDecls -> resolveResults.addAll(procedureDecls) }

            topLevelTypeDecls(module)
                .filter { typeDecl -> typeDecl.typeNameMatches(memberName) }
                .map { it.typeDeclName }
                .let { resolveResults.addAll(it) }

            topLevelVarDeclNames(module)
                .filter { varDeclName -> varDeclName.varName.textMatches(memberName) }
                .let { varDeclNames -> resolveResults.addAll(varDeclNames) }
        }

    return resolveResults
}

fun parentProcedureDeclsOf(containingProcedure: OberonProcedureDecl): Collection<OberonProcedureDecl> =
    PsiTreeUtil.collectParents(containingProcedure, OberonProcedureDecl::class.java, false) { it is OberonModuleDef }

internal fun procedureParamOrNull(procedure: OberonProcedureDecl, paramName: String): de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonFormalParamName? {
    return PsiTreeUtil.findChildrenOfType(procedure, de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonFormalParamName::class.java)
        .firstOrNull { it.textMatches(paramName) }
}

internal fun receiverNameOrNull(procedure: OberonProcedureDecl, receiverName: String): de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonReceiverName? {
    return PsiTreeUtil.findChildOfType(procedure, de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonReceiverName::class.java)
        ?.takeIf { it.textMatches(receiverName) }
}

internal fun procedureLocalVarDeclNameOrNull(procedure: OberonProcedureDecl, varName: String): OberonVarDeclName? {
    val varDeclName = procedure.varSectionList.asSequence()
        .flatMap { it.varDeclList }
        .flatMap { it.varDeclNameList.varDeclNameList }
        .firstOrNull { it.variableNameMatches(varName) }
    return varDeclName
}

private fun procedureLocalConstDeclNameOrNull(procedure: OberonProcedureDecl, varName: String): OberonConstDeclName? {

    return procedure.constSectionList
        .asSequence()
        .flatMap { it.constDeclList }
        .map { it.constDeclName }
        .filter { it.constantName.textMatches(varName) }
        .firstOrNull()
}

private fun procedureLocalProcDeclNameOrNull(procedure: OberonProcedureDecl, procName: String): OberonProcDeclName? {
    return procedure.procedureDeclList
        .asSequence()
        .map { it.procDeclName }
        .filter { it.procedureNameMatches(procName) }
        .firstOrNull()
}

val OberonDesignator.designatorString: String
    get() = this.text

val OberonProcedureDecl.procedureName: String?
    get() = this.procDeclName.procedureName.text

fun OberonProcedureDecl.procedureNameMatches(matchName: String) = this.procDeclName.procedureName.textMatches(matchName)

fun OberonProcDeclName.procedureNameMatches(matchName: String) = this.procedureName.textMatches(matchName)

fun OberonTypeDecl.typeNameMatches(matchName: String) = this.typeDeclName.typeName.textMatches(matchName)

fun OberonVarDeclName.variableNameMatches(matchName: String) = this.varName.textMatches(matchName)
