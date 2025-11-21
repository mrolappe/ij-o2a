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
import com.intellij.psi.util.descendantsOfType
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.*
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.impl.moduleName
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.impl.procedureNameMatches

private val logger = Logger.getInstance(OberonUtil::class.java)

internal fun logDebug(message: String) {
    logger.debug(message)
}

internal object OberonUtil {
    fun findProcedures(project: Project, procName: String): List<OberonProcedureDecl> {
        // TODO
        return emptyList()
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
                    .filter { it.moduleName?.text == name }
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

fun findProcedureDeclNameInFile(
    file: PsiFile,
    procedureName: String,
    exportedOnly: Boolean = true
): OberonProcedureDecl? {
    val procedureDecl = PsiTreeUtil.findChildrenOfType(file, OberonProcedureDecl::class.java)
        .filterNotNull()
        .firstOrNull { it.procedureNameMatches(procedureName) }

    val isExported = false //procedureDecl.markedForExport()

    logDebug("OberonUtil::findProcedureDeclInFile, file: $file, procedureName: $procedureName, exported only: $exportedOnly -> procedureDecl: $procedureDecl (is exported: $isExported)")
    return procedureDecl
}

fun findConstDeclNameExportedByModule(constantName: String, moduleDef: OberonModuleDef): OberonConstDeclName? {
    return moduleDef.constSections.asSequence()
        .flatMap { it.constDeclList }
        .map { it.constDeclName }
        .filter {
            val markedForExport = false //it.markedForExport()
            val textMatches = it.constantName.textMatches(constantName)
            markedForExport && textMatches
        }
        .firstOrNull()
}

val OberonModuleDef.constSections: List<OberonConstSection>
    get() = topLevelDecls.constSectionList

fun findVarDeclNameExportedByModule(varName: String, moduleDef: OberonModuleDef): OberonVarDeclName? {
    return moduleDef.varSections.asSequence()
        .flatMap { it.varDeclList }
        .flatMap { it.varDeclNameList.varDeclNameList }
        .filterNotNull()
        .filter { it.isExported && it.varName.textMatches(varName) }
        .firstOrNull()
}

val OberonModuleDef.varSections: List<OberonVarSection>
    get() = topLevelDecls.varSectionList

fun findTypeDeclNameExportedByModule(typeName: String, module: OberonModuleDef): OberonTypeDeclName? {
    return module.typeSections.asSequence()
        .flatMap { it.typeDeclList }
        .filterNotNull()
        .map { it.typeDeclName }
        .filter { it.isExported && it.typeNameMatches(typeName) }
        .firstOrNull()
}

val OberonModuleDef.typeSections: List<OberonTypeSection>
    get() = topLevelDecls.typeSectionList

fun findOberonASharedLibraryProcedureDeclExportedByModule(
    procedureName: String,
    module: OberonModuleDef
): OberonOberonALibProcDecl? {
    return module.oberonALibraryProcedures.asSequence()
        .filter { it.isExported && it.procedureNameMatches(procedureName) }
        .firstOrNull()
}

val OberonModuleDef.oberonALibraryProcedures: List<OberonOberonALibProcDecl>
    get() = topLevelDecls.oberonALibProcDeclList

val OberonVarDeclName.isExported
    get() = readWriteExportMark != null || readOnlyExportMark != null

fun OberonTypeDeclName.typeNameMatches(matchName: String) = this.typeName.textMatches(matchName)

val OberonTypeDeclName.isExported
    get() = readWriteExportMark != null || readOnlyExportMark != null

fun OberonOberonALibProcDecl.procedureNameMatches(matchName: String) =
    this.identDef.ident.textMatches(matchName)

val OberonOberonALibProcDecl.isExported
    get() = identDef.readWriteExportMark != null || identDef.readOnlyExportMark != null

val OberonFile.moduleHead
    get() = descendantsOfType<OberonModuleHead>(childrenFirst = true).firstOrNull()

val OberonFile.importedModulesAndAliases: Map<List<OberonModuleDef>, OberonImportAlias?>
    get() {
        return getNamesOfImportedModules(this, true)
            .mapKeys { (n, _) -> OberonUtil.findModulesByName(project, n) }
            .mapValues { (_, a) -> a?.let { findImportAlias(a) } }
    }

val OberonFile.importedModules
    get() = getNamesOfImportedModules(this, false)
        .flatMap { (name, _) -> OberonUtil.findModulesByName(project, name) }

val OberonFile.aliasedImportModules
    get() = getNamesOfImportedModules(this, true)
        .filter { (_, alias) -> alias != null }
        .map { (name, alias) -> alias to OberonUtil.findModulesByName(project, name) }
        .associate { it }

val OberonFile.importAliases: Sequence<OberonImportAlias>?
    get() = descendantsOfType<OberonImportList>().firstOrNull()
        ?.importDecls?.importDeclList?.asSequence()
        ?.filterNotNull()
        ?.map { it.importAlias }
        ?.filterNotNull()

fun OberonFile.findImportAlias(name: String): OberonImportAlias? {
    return importAliases?.firstOrNull { it.name == name }
}

fun getNamesOfImportedModules(file: PsiFile, includeAliases: Boolean = true): Map<String, String?> {
    return file.descendantsOfType<OberonImportList>()
        .flatMap { it.getModuleNames(includeAliases).entries }
        .associate { it.toPair() }
}

// determine the set of module names in the import list. if requested, return the alias mapped by
fun OberonImportList.getModuleNames(includeAliases: Boolean = true): Map<String, String?> {
    return importDecls?.importDeclList?.mapNotNull { importDecl ->
        val moduleName = importDecl.importModuleReference.ident.text
        val alias = if (includeAliases) importDecl?.importAlias?.name else null
        moduleName to alias
    }?.toMap() ?: emptyMap()
}
