package de.rholambdapi.o2a.intellij.lang.oberon2.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext

class KeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val elements = listOf("CONST", "PROCEDURE", "TYPE", "VAR")
            .map { LookupElementBuilder.create(it).withBoldness(true) }
        result.addAllElements(elements)

    }
}