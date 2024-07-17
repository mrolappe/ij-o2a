package me.mrolappe.intellij.lang.oberon

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsSafe
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.SyntaxTraverser
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import me.mrolappe.intellij.lang.oberon.psi.*
import me.mrolappe.intellij.lang.oberon.psi.impl.procedureNameMatches

private val logger = Logger.getInstance(OberonUtil::class.java)

internal fun logDebug(message: String) {
    logger.debug(message)
}

internal object OberonUtil {
    fun findProcedures(project: Project, procName: String): List<OberonProcedureDecl> {
        // TODO
        return emptyList<OberonProcedureDecl>()
    }


    fun findProcedures(project: Project): List<OberonProcedureDecl> {
        TODO("Not yet implemented")
    }

    fun findAllModules(project: Project): List<OberonModuleDef> {
        val oberonFiles = getAllOberonFilesInProject(project)
        val psiManager = PsiManager.getInstance(project)

        return oberonFiles.mapNotNull { psiManager.findFile(it) }
            .flatMap { file ->
                val moduleDefs = PsiTreeUtil.getChildrenOfTypeAsList(file, OberonModuleDef::class.java)
                moduleDefs
            }
    }

    fun findModulesByName(project: Project, name: String): List<OberonModuleDef> {
        val oberonFiles = getAllOberonFilesInProject(project)
        val psiManager = PsiManager.getInstance(project)

        return oberonFiles.mapNotNull { vfile -> psiManager.findFile(vfile) }
            .flatMap { file ->
                PsiTreeUtil.getChildrenOfTypeAsList(file, OberonModuleDef::class.java)
                    .filterNotNull()
                    .filter { it.moduleDefName?.moduleName?.text!! == name }
            }
    }

    private fun getAllOberonFilesInProject(project: Project): MutableCollection<VirtualFile> {
        val oberonFiles = FileTypeIndex.getFiles(OberonFileType.INSTANCE, GlobalSearchScope.allScope(project))
//        println("getAllOberonFilesInProject, #: ${oberonFiles.size}")
        return oberonFiles
    }

    fun findImportedProcedure(file: PsiFile, moduleOrAliasName: String, procedureName: String): OberonProcedureDecl? {
        PsiTreeUtil.findChildOfType(file, OberonImportList::class.java)?.let { imports ->
            val importDecl = PsiTreeUtil.findChildrenOfType(imports, OberonImportDecl::class.java)
                .firstOrNull { decl ->
//                    println("decl, importAlias: ${decl.importAlias?.aliasName?.text}, importModuleReference: ${decl.importModuleReference.text}")
                    decl.importAlias?.aliasName?.textMatches(moduleOrAliasName) ?: false
                            || decl.importModuleReference.textMatches(moduleOrAliasName)
                }

            importDecl?.importModuleReference?.text?.let { moduleName ->
                val modules = findModulesByName(file.project, moduleName)

                val foundProcedure = if (modules.isNotEmpty()) {
                    findProcedureDeclNameInFile(modules.first().containingFile, procedureName)
                } else null

                println("OberonUtil::findImportedProcedure, file: $file, moduleOrAliasName: $moduleOrAliasName, procedureName: $procedureName -> $foundProcedure")
                // TODO possibly resolve to many
                return foundProcedure
            }
        }

        return null
    }

    fun findAllTypes(project: Project): Collection<OberonTypeDecl> {
        val psiManager = PsiManager.getInstance(project)

        return getAllOberonFilesInProject(project)
            .mapNotNull { psiManager.findFile(it) }
            .flatMap { psiFile ->
                val children = SyntaxTraverser.psiTraverser(psiFile)
                    .filter(OberonTypeDecl::class.java)
                    .toList()
//                val children = PsiTreeUtil.getChildrenOfTypeAsList(psiFile, OberonTypeDecl::class.java)
                println("type decl children #: ${children.size}")
                children
            }
    }

    fun findTypesByName(project: Project, name: String): Collection<OberonTypeDecl> {
        val psiManager = PsiManager.getInstance(project)

        return getAllOberonFilesInProject(project)
            .mapNotNull { psiManager.findFile(it) }
            .flatMap { psiFile ->
//                PsiTreeUtil.getChildrenOfTypeAsList(psiFile, OberonTypeDecl::class.java)
                val children = SyntaxTraverser.psiTraverser(psiFile)
                    .filter(OberonTypeDecl::class.java)
                    .filter { typeDecl -> typeDecl != null && name.equals(typeName(typeDecl), ignoreCase = true) }
                    .toList()
                println("OberonUtil::findTypesByName, name: $name -> $children")
                children
            }
//            .filter { typeDecl -> typeDecl != null && name.equals(typeName(typeDecl), ignoreCase = true) }
    }

    fun typeName(typeDecl: OberonTypeDecl): @NlsSafe String? =
        typeDecl.typeDeclName.typeName.text
}

fun findProcedureDeclNameInFile(file: PsiFile, procedureName: String, exportedOnly: Boolean = true): OberonProcedureDecl? {
    val procedureDecl = PsiTreeUtil.findChildrenOfType(file, OberonProcedureDecl::class.java)
        .filterNotNull()
        .firstOrNull { it.procedureNameMatches(procedureName) }

    val isExported = false //procedureDecl.markedForExport()

    logDebug("OberonUtil::findProcedureDeclInFile, file: $file, procedureName: $procedureName, exported only: $exportedOnly -> procedureDecl: $procedureDecl (is exported: $isExported)")
    return procedureDecl
}

fun findConstDeclNameExportedByModule(constantName: String, moduleDef: OberonModuleDef): OberonConstDeclName? {
    return moduleDef.constSectionList.asSequence()
        .flatMap { it.constDeclList }
        .map { it.constDeclName }
        .filter {
            val markedForExport = false //it.markedForExport()
            val textMatches = it.constantName.textMatches(constantName)
            markedForExport && textMatches
        }
        .firstOrNull()
}

fun findVarDeclNameExportedByModule(varName: String, moduleDef: OberonModuleDef): OberonVarDeclName? {
    return moduleDef.varSectionList.asSequence()
        .flatMap { it.varDeclList }
        .flatMap { it.varDeclNameList.varDeclNameList }
        .filterNotNull()
        .filter { it.isExported && it.varName.textMatches(varName) }
        .firstOrNull()
}

fun findTypeDeclNameExportedByModule(typeName: String, module: OberonModuleDef): OberonTypeDeclName? {
    return module.typeSectionList.asSequence()
        .flatMap { it.typeDeclList }
        .filterNotNull()
        .map { it.typeDeclName }
        .filter { it.isExported && it.typeNameMatches(typeName) }
        .firstOrNull()
}

fun findOberonASharedLibraryProcedureDeclExportedByModule(procedureName: String, module: OberonModuleDef): OberonOberonALibProcDecl? {
    return module.oberonALibProcDeclList.asSequence()
        .filterNotNull()
        .filter { it.isExported && it.procedureNameMatches(procedureName) }
        .firstOrNull()
}

val OberonVarDeclName.isExported
    get() = this.exportMark != null

fun OberonTypeDeclName.typeNameMatches(matchName: String) = this.typeName.textMatches(matchName)

val OberonTypeDeclName.isExported
    get() = this.exportMark != null

fun OberonOberonALibProcDecl.procedureNameMatches(matchName: String) = this.procDeclName.procedureName.textMatches(matchName)

val OberonOberonALibProcDecl.isExported
    get() = this.procDeclName.exportMark != null