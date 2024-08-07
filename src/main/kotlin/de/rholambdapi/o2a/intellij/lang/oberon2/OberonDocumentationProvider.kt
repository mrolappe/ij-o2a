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
import com.intellij.psi.javadoc.PsiDocComment
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.descendants
import com.intellij.psi.util.descendantsOfType
import com.intellij.psi.util.elementType
import de.rholambdapi.o2a.intellij.Autodoc
import de.rholambdapi.o2a.intellij.AutodocLookup
import de.rholambdapi.o2a.intellij.FUNCTION
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.*
import java.nio.file.Paths
import java.util.function.Consumer

class OberonDocumentationProvider : AbstractDocumentationProvider() {
    private val log = thisLogger()

    private val autodocLookup = AutodocLookup(Paths.get("/Users/mrolappe/studio/rlp-ami-vbcc-dev/NDK3.2/Autodocs")) // TODO path via config

    override fun getQuickNavigateInfo(element: PsiElement?, originalElement: PsiElement?): String {
        val msg = "getQuickNavigateInfo, element: $element, originalElement: $originalElement"
        log.debug(msg)
        return msg
    }

    override fun getUrlFor(element: PsiElement?, originalElement: PsiElement?): List<String> {
        log.debug("getUrlFor, super.getUrlFor: ${super.getUrlFor(element, originalElement)}, element: $element, originalElement: $originalElement")
        return listOf("https://rolappe.it/uno", "https://rolappe.it/dos")
    }

    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String {
        log.debug("generateDoc, element: $element, originalElement: $originalElement")

        return when {
            element is OberonImportAlias -> {
                val module = PsiTreeUtil.getNextSiblingOfType(element, OberonImportModuleReference::class.java)
                "Import alias for module ${module?.ident?.text}"
            }

            element is OberonIdentDef && element.parent is OberonAmigaOberonSharedLibProcDecl ->
                autodocFunctionDescriptionByProcName(Autodoc.ElementName(element.ident.text))
            element is OberonProcDeclName -> lookupAutoDoc(element, originalElement)
            else -> "doc for element $element, original element $originalElement"
        }
    }

    private fun autodocFunctionDescriptionByProcName(procName: Autodoc.ElementName): String {
        val functionDescription = autodocLookup.autodocFunctionDescriptionOrNull(procName)

        return buildDocString(procName, functionDescription)
    }

    private fun buildDocString(procName: Autodoc.ElementName, functionDescription: String?): String {
        val docString = buildString {
            append("autodoc function description for $procName\n$functionDescription")

            append(DocumentationMarkup.DEFINITION_ELEMENT.addText("definition 1"))
            append(DocumentationMarkup.DEFINITION_ELEMENT.addText("definition 2"))
            append(DocumentationMarkup.DEFINITION_ELEMENT.addText("definition 3"))

            append(DocumentationMarkup.DEFINITION_START)
            append("definition 4")
            append(DocumentationMarkup.DEFINITION_END)

            append(DocumentationMarkup.DEFINITION_START)
            append("definition 5")
            append(DocumentationMarkup.DEFINITION_END)

            append(DocumentationMarkup.CONTENT_START)
            append("content")
            append(DocumentationMarkup.CONTENT_END)

            append(DocumentationMarkup.EXTERNAL_LINK_ICON)
            append("external link")

            append(DocumentationMarkup.INFORMATION_ICON)
            append("information")

            append(DocumentationMarkup.SECTIONS_START)
            append(DocumentationMarkup.SECTION_HEADER_START)
            append("section header 1")
            append(DocumentationMarkup.SECTION_START)
            append("section 1")
            append(DocumentationMarkup.SECTION_END)

            append(DocumentationMarkup.SECTION_SEPARATOR)

            append(DocumentationMarkup.SECTION_HEADER_START)
            append("section header 2")
            append(DocumentationMarkup.SECTION_START)
            append("section 2")
            append(DocumentationMarkup.SECTION_END)

            append(DocumentationMarkup.SECTION_HEADER_START)
            append("etwas längerer section header 3")
            append(DocumentationMarkup.SECTION_START)
            append("section 3")
            append(DocumentationMarkup.SECTION_END)

            append(DocumentationMarkup.SECTIONS_END)
        }
        return docString
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

    private fun lookupAutoDoc(element: OberonProcDeclName, originalElement: PsiElement?): String {
        val procName = element.procedureName.text

        val elementName = Autodoc.ElementName(procName)
        val autodocs = autodocLookup.getAutodocsByFunctionName(elementName)
        val function = autodocs.firstOrNull()?.contentForSection(elementName, FUNCTION)?.asString ?: "???"

        return buildDocString(elementName, function)
//        return "TODO look up autodoc for ${element.procedureName.text}<br/>element: $element<br/>original element: $originalElement<br/>" +
//                "element parent: ${element.parent}<br/>text from autodoc:<br/>$function"
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