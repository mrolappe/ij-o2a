package de.rholambdapi.o2a.intellij.lang.oberon2

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
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonOberonALibProcDecl
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonTypeDeclName
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonVarDeclName
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.impl.procedureNameMatches

private val logger = Logger.getInstance(OberonUtil::class.java)

internal fun logDebug(message: String) {
    logger.debug(message)
}

internal object OberonUtil {
    fun findProcedures(project: Project, procName: String): List<de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonProcedureDecl> {
        // TODO
        return emptyList<de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonProcedureDecl>()
    }


    fun findProcedures(project: Project): List<de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonProcedureDecl> {
        TODO("Not yet implemented")
    }

    fun findAllModules(project: Project): List<de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonModuleDef> {
        val oberonFiles = getAllOberonFilesInProject(project)
        val psiManager = PsiManager.getInstance(project)

        return oberonFiles.mapNotNull { psiManager.findFile(it) }
            .flatMap { file ->
                val moduleDefs = PsiTreeUtil.getChildrenOfTypeAsList(file, de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonModuleDef::class.java)
                moduleDefs
            }
    }

    fun findModulesByName(project: Project, name: String): List<de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonModuleDef> {
        val oberonFiles = getAllOberonFilesInProject(project)
        val psiManager = PsiManager.getInstance(project)

        return oberonFiles.mapNotNull { vfile -> psiManager.findFile(vfile) }
            .flatMap { file ->
                PsiTreeUtil.getChildrenOfTypeAsList(file, de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonModuleDef::class.java)
                    .filterNotNull()
                    .filter { it.moduleDefName.moduleName.text!! == name }
            }
    }

    private fun getAllOberonFilesInProject(project: Project): MutableCollection<VirtualFile> {
        val oberonFiles = FileTypeIndex.getFiles(OberonFileType.INSTANCE, GlobalSearchScope.allScope(project))
//        println("getAllOberonFilesInProject, #: ${oberonFiles.size}")
        return oberonFiles
    }

    fun findImportedProcedure(file: PsiFile, moduleOrAliasName: String, procedureName: String): de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonProcedureDecl? {
        PsiTreeUtil.findChildOfType(file, de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonImportList::class.java)?.let { imports ->
            val importDecl = PsiTreeUtil.findChildrenOfType(imports, de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonImportDecl::class.java)
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

    fun findAllTypes(project: Project): Collection<de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonTypeDecl> {
        val psiManager = PsiManager.getInstance(project)

        return getAllOberonFilesInProject(project)
            .mapNotNull { psiManager.findFile(it) }
            .flatMap { psiFile ->
                val children = SyntaxTraverser.psiTraverser(psiFile)
                    .filter(de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonTypeDecl::class.java)
                    .toList()
//                val children = PsiTreeUtil.getChildrenOfTypeAsList(psiFile, OberonTypeDecl::class.java)
                println("type decl children #: ${children.size}")
                children
            }
    }

    fun findTypesByName(project: Project, name: String): Collection<de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonTypeDecl> {
        val psiManager = PsiManager.getInstance(project)

        return getAllOberonFilesInProject(project)
            .mapNotNull { psiManager.findFile(it) }
            .flatMap { psiFile ->
//                PsiTreeUtil.getChildrenOfTypeAsList(psiFile, OberonTypeDecl::class.java)
                val children = SyntaxTraverser.psiTraverser(psiFile)
                    .filter(de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonTypeDecl::class.java)
                    .filter { typeDecl -> typeDecl != null && name.equals(typeName(typeDecl), ignoreCase = true) }
                    .toList()
                println("OberonUtil::findTypesByName, name: $name -> $children")
                children
            }
//            .filter { typeDecl -> typeDecl != null && name.equals(typeName(typeDecl), ignoreCase = true) }
    }

    fun typeName(typeDecl: de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonTypeDecl): @NlsSafe String? =
        typeDecl.typeDeclName.typeName.text
}

fun findProcedureDeclNameInFile(file: PsiFile, procedureName: String, exportedOnly: Boolean = true): de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonProcedureDecl? {
    val procedureDecl = PsiTreeUtil.findChildrenOfType(file, de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonProcedureDecl::class.java)
        .filterNotNull()
        .firstOrNull { it.procedureNameMatches(procedureName) }

    val isExported = false //procedureDecl.markedForExport()

    logDebug("OberonUtil::findProcedureDeclInFile, file: $file, procedureName: $procedureName, exported only: $exportedOnly -> procedureDecl: $procedureDecl (is exported: $isExported)")
    return procedureDecl
}

fun findConstDeclNameExportedByModule(constantName: String, moduleDef: de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonModuleDef): de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonConstDeclName? {
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

fun findVarDeclNameExportedByModule(varName: String, moduleDef: de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonModuleDef): OberonVarDeclName? {
    return moduleDef.varSectionList.asSequence()
        .flatMap { it.varDeclList }
        .flatMap { it.varDeclNameList.varDeclNameList }
        .filterNotNull()
        .filter { it.isExported && it.varName.textMatches(varName) }
        .firstOrNull()
}

fun findTypeDeclNameExportedByModule(typeName: String, module: de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonModuleDef): OberonTypeDeclName? {
    return module.typeSectionList.asSequence()
        .flatMap { it.typeDeclList }
        .filterNotNull()
        .map { it.typeDeclName }
        .filter { it.isExported && it.typeNameMatches(typeName) }
        .firstOrNull()
}

fun findOberonASharedLibraryProcedureDeclExportedByModule(procedureName: String, module: de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonModuleDef): OberonOberonALibProcDecl? {
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