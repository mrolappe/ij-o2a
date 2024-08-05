package de.rholambdapi.o2a.intellij.lang.oberon2.completion

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.psi.util.prevLeaf
import com.intellij.util.ProcessingContext

class ImportedDeclarationsCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        val leafBeforePosition = parameters.position.prevLeaf()
        println("leafBeforePosition: $leafBeforePosition, text: ${leafBeforePosition?.text}, offs: ${leafBeforePosition?.textOffset}")

        if (leafBeforePosition == null
//            || (leafBeforePosition.elementType != OberonTypes.DOT && !leafBeforePosition.prevSibling.isIdentifier())
        ) {
            return
        }

        // prüfen auf . und IDENT davor
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