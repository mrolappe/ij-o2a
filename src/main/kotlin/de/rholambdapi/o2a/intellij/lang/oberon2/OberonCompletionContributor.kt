package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.codeInsight.completion.*
import com.intellij.openapi.util.TextRange
import com.intellij.patterns.ElementPattern
import com.intellij.patterns.PatternCondition
import com.intellij.patterns.PlatformPatterns.*
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.*
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import com.intellij.util.ProcessingContext
import de.rholambdapi.o2a.intellij.lang.oberon2.completion.*
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.*

infix fun <T> ElementPattern<T>.or(other: ElementPattern<T>): ElementPattern<T> = or(this, other)
infix fun <T> ElementPattern<T>.and(other: ElementPattern<T>): ElementPattern<T> = and(this, other)

internal val INSIDE_PROC = psiElement().inside(OberonProcedureDecl::class.java)
internal val NOT_INSIDE_PROC = not(INSIDE_PROC)
internal val INSIDE_MODULE_TAIL = psiElement().inside(OberonModuleTail::class.java)

internal val INSIDE_MODULE_INIT = psiElement().with(object : PatternCondition<PsiElement>("insideModuleInit") {
    override fun accepts(t: PsiElement, context: ProcessingContext?): Boolean {
        val moduleTail = t.parentsOfType<OberonModuleTail>().firstOrNull() ?: return false
        val begin = moduleTail.moduleInit?.beginKeyword ?: return false
        val end = moduleTail.endKeyword ?: return false
        return TextRange(begin.endOffset, end.startOffset).contains(t.startOffset)
    }
})

internal val IN_PROCEDURE_HEAD = psiElement().afterLeaf(psiElement().withParent(OberonProcedureDecl::class.java))
internal val INSIDE_PROCEDURE_BODY = psiElement().inside(psiElement(OberonProcedureDeclBody::class.java)) or
        psiElement().inside(OberonProcedureDeclBodyBlock::class.java).beforeLeaf(psiElement(OberonTypes.END))

internal val TOP_LEVEL_PLACES = NOT_INSIDE_PROC
    // not inside module head, i.e. the module declaration and the import list
    .andNot(psiElement().inside(OberonModuleHead::class.java))
    // not inside module tail, i.e. the module init block
    .andNot(INSIDE_MODULE_TAIL)
    // not after module (tail)
    .andNot(
        psiElement().afterSiblingSkipping(
            psiElement().whitespaceCommentEmptyOrError(),
            psiElement(OberonModuleDef::class.java)
        )
    )

class OberonCompletionContributor : CompletionContributor() {
    init {
        extend(CompletionType.BASIC, INSIDE_PROCEDURE_BODY or INSIDE_MODULE_INIT, ModuleLocalTopLevelDeclarationsCompletionProvider())
        // TODO also in procedures with receiver
        extend(CompletionType.BASIC, INSIDE_PROCEDURE_BODY, ProcedureLocalDeclarationsCompletionProvider())

        extend(CompletionType.BASIC, INSIDE_PROCEDURE_BODY or INSIDE_MODULE_INIT, ImportedDeclarationsCompletionProvider())

        // inside procedure body or module init body, but not when already in qualified identifier
        extend(CompletionType.BASIC, (INSIDE_PROCEDURE_BODY or INSIDE_MODULE_INIT)
            .and(not(psiElement(OberonTypes.IDENT).withParent(OberonQualIdentQualified::class.java))),
            ImportedModulesCompletionProvider())

        extend(CompletionType.BASIC, TOP_LEVEL_PLACES, KeywordCompletionProvider())
    }

    override fun beforeCompletion(context: CompletionInitializationContext) {
        val element = context.file.findElementAt(context.startOffset)
        val inModuleInit = INSIDE_MODULE_INIT.accepts(element)
//        println("element: $element, in module init: $inModuleInit")
//        context.dummyIdentifier = ""
    }

    fun PsiElement?.isIdentifier() = elementType == OberonTypes.IDENT
}

internal val PsiFile.moduleDef
    get() = this.descendantsOfType<OberonModuleDef>().firstOrNull()
