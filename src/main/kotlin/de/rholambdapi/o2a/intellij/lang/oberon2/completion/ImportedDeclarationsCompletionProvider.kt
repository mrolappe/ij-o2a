package de.rholambdapi.o2a.intellij.lang.oberon2.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.util.descendantsOfType
import com.intellij.psi.util.elementType
import com.intellij.psi.util.prevLeaf
import com.intellij.util.ProcessingContext
import de.rholambdapi.o2a.intellij.lang.oberon2.importedModulesAndAliases
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.*

class ImportedDeclarationsCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val position = parameters.position
        val prefix = result.prefixMatcher.prefix

        // requires the parser to be robust enough to yield the node-to-be in presence of an error due to incomplete input
        if (/*prefix.isEmpty() &&*/ position.prevSibling?.parent is OberonQualIdentQualified) {
            // LHS of . -> module/alias name
            val qualifier = position.prevLeaf { it.elementType == OberonTypes.IDENT }?.text!!

            // TODO use indexing infrastructure
            val modulesAndAliases = position.oberonFile?.importedModulesAndAliases
            modulesAndAliases?.entries
                ?.filter { (modules, alias) ->
                    modules.any { it.name == qualifier } || alias?.name == qualifier
                }
                ?.flatMap { (modules, _) -> modules }
                ?.flatMap { module ->
                    println("module: $module")
                    module.exportedProcedures
                        .filter { it.procDeclName.text.startsWith(prefix) }
                        .map { LookupElementBuilder.create(it) }

                    module.exportedOberonASharedLibraryProcedures
                        .filter { it.identDef.ident.text.startsWith(prefix) }
                        .map { LookupElementBuilder.create(it).withCaseSensitivity(false) }
                }
                ?.also { elements -> result.addAllElements(elements) }

//            result.addElement(LookupElementBuilder.create("import declarations aus ${position.prevLeaf { it.elementType == OberonTypes.IDENT }?.text}"))
        }
    }
}

val PsiElement.oberonFile: OberonFile?
    get() = if (this is OberonFile) {
        this
    } else {
        this.containingFile.descendantsOfType<OberonFile>().firstOrNull()
    }

val OberonModuleDef.exportedProcedures: Set<OberonProcedureDecl>
    get() = topLevelDecls.procedureDeclList
        .filter { procedure ->
            val name = procedure.name
            val markedForExport = procedure.markedForExport()
            println("exportedProcedures, name: $name, export: $markedForExport")
            markedForExport
        }
        .toSet()

val OberonModuleDef.exportedOberonASharedLibraryProcedures: Set<OberonOberonALibProcDecl>
    get() = topLevelDecls.oberonALibProcDeclList
        .filter { it.isMarkedForExport }
        .toSet()