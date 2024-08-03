package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.codeInsight.completion.*
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns
import com.intellij.patterns.PlatformPatterns.*
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.*
import com.intellij.util.ProcessingContext
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.*

val insideProc = psiElement().inside(OberonProcedureDecl::class.java)
val hasTopLevelDeclParent = psiElement().withParent(OberonTopLevelDecls::class.java)
val notInsideProc = not(insideProc)
val insideModuleTail = psiElement().inside(OberonModuleTail::class.java)
val topLevelPlaces = notInsideProc
    // not inside module head, i.e. the module declaration and the import list
    .andNot(psiElement().inside(OberonModuleHead::class.java))
    // not inside module tail, i.e. the module init block
    .andNot(insideModuleTail)
    // not after module (tail)
    .andNot(
        psiElement().afterSiblingSkipping(
            psiElement().whitespaceCommentEmptyOrError(),
            psiElement(OberonModuleDef::class.java)
        )
    )

class OberonCompletionContributor : CompletionContributor() {
    init {
        val completionProvider = object : CompletionProvider<CompletionParameters>() {
            override fun addCompletions(
                parameters: CompletionParameters,
                context: ProcessingContext,
                result: CompletionResultSet
            ) {
                val oldTopLevelPattern = psiElement()
                    .withLanguage(OberonLanguage.INSTANCE)
//                    .andNot(psiElement().afterSibling(instanceOf(OberonModuleInit::class.java)))
                    .with(object : PatternCondition<PsiElement>("custom") {
                        override fun accepts(t: PsiElement, context: ProcessingContext?): Boolean {

                            if (t.parent is PsiErrorElement) {

                                return t.parent.parent is OberonModuleDef
                            } else {
                                return t.parent is OberonModuleDef
                            }
                        }

                    })

                println("matches top level: ${topLevelPlaces.accepts(parameters.position)}")
                println("has top level decl parent: ${hasTopLevelDeclParent.accepts(parameters.position)}")
                println("inside proc: ${insideProc.accepts(parameters.position)}")
                println("inside module tail: ${insideModuleTail.accepts(parameters.position)}")

                val originalPosition = parameters.originalPosition
                val file = originalPosition?.containingFile ?: return

//                println("orig pos $originalPosition, text: ${originalPosition?.text}; prev sib: ${originalPosition?.prevSibling}, text: ${originalPosition?.prevSibling?.text}")

                handleModulePrefix(parameters, result)
                addProceduresDefinedInModule(result, file)

                result.addAllElements(

                    listOf(
                        LookupElementBuilder.create("orig pos: $originalPosition, prev sib text: ${originalPosition?.prevSibling?.prevSibling?.text}"),
                        LookupElementBuilder.create("EXIT"),
                        LookupElementBuilder.create("RETURN")
                    )
                )
            }

        }
        addProviderForLocalProcedureMembers()
        val places = PlatformPatterns.or(
//            psiElement().withTreeParent(psiElement(OberonConstSection::class.java)),
            psiElement().inFile(psiFile().withLanguage(OberonLanguage.INSTANCE)),
        )

        extend(CompletionType.BASIC, places, completionProvider)

        extend(CompletionType.BASIC, topLevelPlaces, TopLevelKeywordCompletionProvider())
    }

    // If in procedure, add completions for constants, variables and procedures defined in the procedure
    private fun addProviderForLocalProcedureMembers() {
        // TODO also in procedures with receiver
        val inProcedureHead = psiElement().afterLeaf(psiElement().withParent(OberonProcedureDecl::class.java))
        val inProcedureBody = psiElement().afterLeaf(psiElement().withParent(OberonProcedureDeclBody::class.java))

        extend(CompletionType.BASIC, inProcedureHead, object : CompletionProvider<CompletionParameters>() {
            override fun addCompletions(
                parameters: CompletionParameters,
                context: ProcessingContext,
                result: CompletionResultSet
            ) {
                val elements = listOf("CONST", "PROCEDURE", "TYPE", "VAR")
                    .map { LookupElementBuilder.create(it) }
                result.addAllElements(elements)
            }
        })

        extend(CompletionType.BASIC, inProcedureBody, object : CompletionProvider<CompletionParameters>() {
            override fun addCompletions(
                parameters: CompletionParameters,
                context: ProcessingContext,
                result: CompletionResultSet
            ) {
                result.addElement(LookupElementBuilder.create("local procedure members"))
            }
        })
    }

    override fun beforeCompletion(context: CompletionInitializationContext) {
        context.dummyIdentifier = ""
    }

    private fun addProceduresDefinedInModule(result: CompletionResultSet, file: PsiFile) {
        file.moduleDef?.let { m ->
            m.topLevelDecls.procedureDeclList
                .mapNotNull { d -> d.procDeclName }
                .forEach {
                    println("decl name: $it");
                    result.addElement(
                        LookupElementBuilder
                            .createWithIcon(it)
                            .withTailText("der tail text")
                            .appendTailText("noch mehr tail text?", true)
                            .withTypeIconRightAligned(true)
                    )
                }
        }
    }

    val PsiFile.moduleDef
        get() = this.descendantsOfType<OberonModuleDef>().firstOrNull()

    private fun handleModulePrefix(
        parameters: CompletionParameters,
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

        val elements = getNamesOfImportedModules(leafBeforePosition.containingFile)
            .flatMap { entry -> listOf(entry.key, entry.value) }
            .filterNotNull()
//            .map { LookupElementBuilder.create(it) }
            .forEach { result.addElement(LookupElementBuilder.create(it)) }
//        result.addLookupAdvertisement("ziz iz my lookup advertisement")
//        result.addAllElements(elements)
//        if (leafBeforePosition.prevSibling?.textMatches(
//                "Dos"
//            ) == true
//        ) {
//            result.addElement(LookupElementBuilder.create("Dos funcs (elem vor . : ${leafBeforePosition.prevSibling})"))
//        }
    }

    fun PsiElement?.isIdentifier() = elementType == OberonTypes.IDENT

//    override fun fillCompletionVariants(parameters: CompletionParameters, result: CompletionResultSet) {
//        println("fillCompletionVariants")
//        super.fillCompletionVariants(parameters, result)
//    }
}

class TopLevelKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {
    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        result.addElement(LookupElementBuilder.create("toplebel 1"))
        result.addElement(LookupElementBuilder.create("toplebel 2"))
        result.addElement(LookupElementBuilder.create("toplebel 3"))
    }
}
