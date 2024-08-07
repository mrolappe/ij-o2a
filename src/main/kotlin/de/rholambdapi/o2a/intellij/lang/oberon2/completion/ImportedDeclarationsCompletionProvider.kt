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

        val text = "imported declarations; prefix: $prefix"

        // requires the parser to be robust enough to yield the node-to-be in presence of an error due to incomplete input
        if (/*prefix.isEmpty() &&*/ position.prevSibling?.parent is OberonQualIdentQualified) {
            // LHS of . -> module/alias name
            val qualifier = position.prevLeaf { it.elementType == OberonTypes.IDENT }?.text!!

            // TODO use indexing infrastructure
            val modulesAndAliases = position.oberonFile?.importedModulesAndAliases
            modulesAndAliases?.entries
                ?.filter { (modules, alias) ->
                    val matches = modules.any { it.name == qualifier } || alias?.name == qualifier
                    println("qual $qualifier matches: $matches")
                    matches
                }
                ?.flatMap { (modules, _) -> modules }
                ?.flatMap { module ->
                    println("module: $module")
                    module.exportedProcedures
                        .filter { val procDeclName = it.procDeclName
                            val nameText = procDeclName.text
                            val startsWith = nameText.startsWith(prefix)
                            println("decl name: $procDeclName, nameText: $nameText, startsWith: $startsWith")
                            startsWith
                        }
                        .map { LookupElementBuilder.create(it) }

//                    module.exportedOberonASharedLibProcedures
                }
                ?.also { elements -> result.addAllElements(elements) }

//            result.addElement(LookupElementBuilder.create("import declarations aus ${position.prevLeaf { it.elementType == OberonTypes.IDENT }?.text}"))
        }
        result.addElement(LookupElementBuilder.create(text))

        if (position == null
//            || (leafBeforePosition.elementType != OberonTypes.DOT && !leafBeforePosition.prevSibling.isIdentifier())
        ) {
            return
        }

        // falls vorhanden, liste der importierten module ermitteln
        // prüfen, ob IDENT in der liste vorkommt
        // falls nein, fertig; falls ja, top level member des ermittelten moduls ermitteln und lookup elements ableiten

//        result.addLookupAdvertisement("ziz iz my lookup advertisement")
//        result.addAllElements(elements)
//        if (leafBeforePosition.prevSibling?.textMatches(
//                "Dos"
//            ) == true
//        ) {
//            result.addElement(LookupElementBuilder.create("Dos funcs (elem vor . : ${leafBeforePosition.prevSibling})"))
//        }
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

//val OberonModuleDef.exportedOberonASharedLibProcedures: Set<OberonOberonALibProcDecl>
//    get() = topLevelDecls.oberonALibProcDeclList
//        .filter { procDecl -> procDecl }