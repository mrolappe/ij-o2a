package de.rholambdapi.o2a.intellij.lang.oberon2.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElement
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import de.rholambdapi.o2a.intellij.lang.oberon2.moduleDef

class ModuleLocalTopLevelDeclarationsCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
//        result.addElement(LookupElementBuilder.create("module local top level decls"))
        val module = parameters.position.containingFile.moduleDef ?: return

        val elements = mutableListOf<LookupElement>()

        module.topLevelDecls.procedureDeclList
            .mapNotNull { it }
            .mapTo(elements) {
                LookupElementBuilder
                    .createWithIcon(it)
                    .withTailText("module local top level")
            }

        result.addAllElements(elements)
//        parameters.position.containingFile.moduleDef?.let { m ->
//            m.topLevelDecls.procedureDeclList
//                .mapNotNull { d -> d.procDeclName }
//                .forEach {
//                    println("decl name: $it");
//                    result.addElement(
//                        LookupElementBuilder.createWithIcon(it)
//                            .withTailText("der tail text")
//                            .appendTailText("noch mehr tail text?", true)
//                            .withTypeIconRightAligned(true)
//                    )
//                }
//        }
    }

}