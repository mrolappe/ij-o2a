package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PlatformPatterns.psiElement
import com.intellij.util.ProcessingContext

class OberonCompletionContributor : CompletionContributor() {
    init {
        val completionProvider = object : CompletionProvider<CompletionParameters>() {
            override fun addCompletions(
                parameters: CompletionParameters,
                context: ProcessingContext,
                result: CompletionResultSet
            ) {
                println("OberonCompletionContributor, parameters: $parameters, context: $context, result: $result")
                result.addAllElements(
                    listOf(
                        LookupElementBuilder.create("EXIT"),
                        LookupElementBuilder.create("RETURN")
                    )
                )
            }

        }
        val places = PlatformPatterns.or(
            psiElement().withTreeParent(psiElement(de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonConstSection::class.java))
        )

        extend(CompletionType.BASIC, places, completionProvider)
    }
}