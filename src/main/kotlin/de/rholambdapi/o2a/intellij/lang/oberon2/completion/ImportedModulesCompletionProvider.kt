package de.rholambdapi.o2a.intellij.lang.oberon2.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.psi.util.descendantsOfType
import com.intellij.util.ProcessingContext
import de.rholambdapi.o2a.intellij.lang.oberon2.importedModules
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonFile

class ImportedModulesCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        parameters.position.containingFile.descendantsOfType<OberonFile>().firstOrNull()
            ?.importedModules
            ?.map {
                LookupElementBuilder.createWithIcon(it)
                    .withTailText("tail text")
                    .withTypeText("type text")
                    .withTypeIconRightAligned(true)
//                    .withIcon(AllIcons.Nodes.Module)
            }
            ?.let { result.addAllElements(it) }
    }
}