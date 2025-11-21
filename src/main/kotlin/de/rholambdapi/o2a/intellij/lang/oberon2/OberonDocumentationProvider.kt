package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.text.HtmlBuilder
import com.intellij.openapi.util.text.HtmlChunk
import com.intellij.psi.PsiComment
import com.intellij.psi.PsiDocCommentBase
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.descendantsOfType
import com.intellij.psi.util.elementType
import de.rholambdapi.o2a.intellij.*
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.*
import java.nio.file.Paths
import java.util.function.Consumer

class OberonDocumentationProvider : AbstractDocumentationProvider() {
    private val log = thisLogger()

    private val autodocLookup =
        AutodocLookup(Paths.get("/Users/mrolappe/studio/rlp-ami-vbcc-dev/NDK3.2/Autodocs")) // TODO path via config

    override fun getQuickNavigateInfo(element: PsiElement?, originalElement: PsiElement?): String {
        val msg = "getQuickNavigateInfo, element: $element, originalElement: $originalElement"
        log.debug(msg)
        return msg
    }

    override fun getUrlFor(element: PsiElement?, originalElement: PsiElement?): List<String> {
        log.debug(
            "getUrlFor, super.getUrlFor: ${
                super.getUrlFor(
                    element,
                    originalElement
                )
            }, element: $element, originalElement: $originalElement"
        )
        return listOf("https://rolappe.it/uno", "https://rolappe.it/dos")
    }

    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        log.debug("generateDoc, element: $element, originalElement: $originalElement")

        return when (element) {
            is OberonImportAlias -> {
                val module = PsiTreeUtil.getNextSiblingOfType(element, OberonImportModuleReference::class.java)
                "Import alias for module ${module?.ident?.text}"
            }

            //            element is OberonIdentDef && element.parent is OberonAmigaOberonSharedLibProcDecl ->
            //                autodocFunctionDescriptionByProcName(Autodoc.ElementName(element.ident.text))
            is OberonOberonALibProcDecl -> generateDocFromAutodocEntry(element.identDef.ident, originalElement)
            is OberonProcDeclName -> generateDocFromAutodocEntry(element.procedureName, originalElement)
            else -> "doc for element $element, original element $originalElement"
        }
    }

    private fun buildDocString(procName: Autodoc.ElementName, autodoc: Autodoc): String? {
        val contentBySection = autodoc.contentBySection(procName) ?: return null

        return buildString {
            append(DocumentationMarkup.SECTIONS_START)

            contentBySection[NAME]?.let { appendMarkupForSection("Name", it) }
            contentBySection[SYNOPSIS]?.let { appendMarkupForSynopsisSection(it) }
            contentBySection[FUNCTION]?.let { markupForFunctionSection(it) }
            contentBySection[INPUTS]?.let { appendMarkupForSection("Inputs", it) }
            contentBySection[BUGS]?.let { appendMarkupForSection("Bugs", it) }
            contentBySection[RESULT]?.let { appendMarkupForSection("Result", it) }
            contentBySection[RETURNS]?.let { appendMarkupForSection("Returns", it) }
            contentBySection[SEE_ALSO]?.let { appendMarkupForSection("See also", it) }

            append(DocumentationMarkup.SECTIONS_END)
        }
    }

    private fun StringBuilder.appendMarkupForSection(sectionTitle: String, sectionContent: SectionContent) {
        append(DocumentationMarkup.SECTION_HEADER_START)
        append(sectionTitle)
        append(DocumentationMarkup.SECTION_START)
        append(sectionContent.asString)
        append(DocumentationMarkup.SECTION_END)
    }

    private fun StringBuilder.appendMarkupForSynopsisSection(sectionContent: SectionContent) {
        append(DocumentationMarkup.SECTION_HEADER_START)
        append("Synopsis")
        append(DocumentationMarkup.SECTION_START)
        append(DocumentationMarkup.CONTENT_START)
        var preElement = HtmlChunk.tag("pre").child(HtmlChunk.tag("code"))//.style("white-space: nowrap;")
        sectionContent.asString.trimIndent().lineSequence().forEach { line ->
            preElement = preElement.addText(line)
            preElement = preElement.child(HtmlChunk.br())
        }
        preElement.appendTo(this)
//        HtmlChunk.tag("pre").addText(sectionContent.asString).appendTo(this)
        append(DocumentationMarkup.CONTENT_END)
        append(DocumentationMarkup.SECTION_END)
    }

    private fun StringBuilder.markupForFunctionSection(sectionContent: SectionContent): java.lang.StringBuilder? {
        append(DocumentationMarkup.SECTION_HEADER_START)
        append("Function")
        append(DocumentationMarkup.SECTION_START)
        HtmlChunk.text(sectionContent.asString).appendTo(this)
        return append(DocumentationMarkup.SECTION_END)
    }

    override fun getCustomDocumentationElement(
        editor: Editor,
        file: PsiFile,
        contextElement: PsiElement?,
        targetOffset: Int
    ): PsiElement? {
//        println("getCustomDocumentationElement, contextElement: $contextElement")
        return null
    }

    private fun generateDocFromAutodocEntry(element: PsiElement, originalElement: PsiElement?): String? {
        val procName = element.text
        val elementName = Autodoc.ElementName(procName)
        val autodocs = autodocLookup.getAutodocsByFunctionName(elementName)
        val autodoc = autodocs.firstOrNull() ?: return null
        val function = autodoc?.contentForSection(elementName, FUNCTION)?.asString ?: "???"

        return buildDocString(elementName, autodoc)
    }

    override fun generateHoverDoc(element: PsiElement, originalElement: PsiElement?): String {
        val msg = "hover doc, element: $element, originalElement: $originalElement"
        log.debug(msg)
        return msg
    }

    override fun generateRenderedDoc(comment: PsiDocCommentBase): String {
        val msg = "generateRenderedDoc, comment: $comment"
        log.debug(msg)
        val doc = HtmlBuilder()
            .append(DocumentationMarkup.CENTERED_ELEMENT)
            .append("Pseudo comment: ").append(HtmlChunk.tag("code").addText(comment.text))
            .wrapWithHtmlBody()
            .toString()
        return doc
    }

    override fun getDocumentationElementForLookupItem(
        psiManager: PsiManager?,
        `object`: Any?,
        element: PsiElement?
    ): PsiElement? {
        log.debug("getDocumentationElementForLookupItem, object: $`object`, element: $element")
        return super.getDocumentationElementForLookupItem(psiManager, `object`, element)
    }

    override fun getDocumentationElementForLink(
        psiManager: PsiManager?,
        link: String?,
        context: PsiElement?
    ): PsiElement? {
        log.debug("getDocumentationElementForLink, link: $link, context: $context")
        return super.getDocumentationElementForLink(psiManager, link, context)
    }

    override fun collectDocComments(file: PsiFile, sink: Consumer<in PsiDocCommentBase>) {
        file.descendantsOfType<PsiComment>().firstOrNull { it.elementType == OberonTypes.PSEUDO_COMMENT }
            ?.let { sink.accept(MyDocComment(it)) }
    }
}

class MyDocComment(private val pseudoComment: PsiComment) : PsiDocCommentBase, PsiComment by pseudoComment {
    override fun getOwner() = pseudoComment

}