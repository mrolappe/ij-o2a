package de.rholambdapi.o2a.intellij.lang.oberon2.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.descendantsOfType
import com.intellij.psi.util.prevLeaf
import com.intellij.util.ProcessingContext
import de.rholambdapi.o2a.intellij.lang.oberon2.importedModulesAndAliases
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonFile

class ImportedModulesCompletionProvider : CompletionProvider<CompletionParameters>() {
    private val log = thisLogger()

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val prefix = result.prefixMatcher.prefix
        val position = parameters.position
        val prevNonWsLeaf = position.prevLeaf { it !is PsiWhiteSpace }
        println("position: $position, parent: ${position.parent}, prev non-ws leaf: $prevNonWsLeaf, parent: ${prevNonWsLeaf?.parent}, prefix: $prefix")

        position.containingFile.descendantsOfType<OberonFile>().firstOrNull()
            ?.importedModulesAndAliases
            ?.map { (modules, alias) ->
                if (modules.size > 1) {
                    log.warn("More than 1 module found for name ${modules.first().name}, will use first result.")
                }

                if (modules.isEmpty()) {
                    null
                } else {
                    val module = modules[0]

                    if (alias != null) {
                        LookupElementBuilder.createWithIcon(alias)
                            .withLookupStrings(listOf(alias.name, module.name))
                            .withPresentableText("${alias.name} -> ${module.name}")
                            .withTypeText("Alias")
//                    .withTypeIconRightAligned(true)
                    } else {
                        LookupElementBuilder.createWithIcon(module)
                            .withLookupString(module.name!!)
                            .withPresentableText(module.name!!)
                    }
                }
            }
            ?.filterNotNull()
            ?.let { result.addAllElements(it) }
    }
}