package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.psi.PsiDocCommentBase
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil
import de.rholambdapi.o2a.intellij.AutodocLookup
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.*
import java.nio.file.Paths

class OberonDocumentationProvider : AbstractDocumentationProvider() {
    private val LOG = thisLogger()

    private val autodocLookup = AutodocLookup(Paths.get("/Users/mrolappe/studio/rlp-ami-vbcc-dev/NDK3.2/Autodocs"))

    override fun getQuickNavigateInfo(element: PsiElement?, originalElement: PsiElement?): String {
        val msg = "getQuickNavigateInfo, element: $element, originalElement: $originalElement"
        LOG.debug(msg)
        return msg
    }

    override fun getUrlFor(element: PsiElement?, originalElement: PsiElement?): List<String> {
        LOG.debug("getUrlFor, super.getUrlFor: ${super.getUrlFor(element, originalElement)}, element: $element, originalElement: $originalElement")
        return listOf("https://rolappe.it/uno", "https://rolappe.it/dos")
    }

    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String {
        LOG.debug("generateDoc, element: $element, originalElement: $originalElement")

        return when {
            element is OberonImportAlias -> {
                val module = PsiTreeUtil.getNextSiblingOfType(element, OberonImportModuleReference::class.java)
                "Import alias for module ${module?.ident?.text}"
            }

            element is OberonIdentDef && element.parent is OberonAmigaOberonSharedLibProcDecl -> autodocFunctionDescriptionByProcName(element.ident.text)
            element is OberonProcDeclName -> lookupAutoDoc(element, originalElement)
            else -> "doc for element $element, original element $originalElement"
        }
    }

    private fun autodocFunctionDescriptionByProcName(procName: String): String {
        val functionDescription = autodocLookup.autodocFunctionDescriptionOrNull(procName)

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

    private fun lookupAutoDoc(element: OberonProcDeclName, originalElement: PsiElement?): String {
        val procName = element.procedureName.text

        val functionDescription = autodocLookup.autodocFunctionDescriptionOrNull(procName)

        return "TODO look up autodoc for ${element.procedureName.text}<br/>element: $element<br/>original element: $originalElement<br/>" +
                "element parent: ${element.parent}<br/>text from autodoc:<br/>$functionDescription"
    }

    override fun generateHoverDoc(element: PsiElement, originalElement: PsiElement?): String {
        val msg = "hover doc, element: $element, originalElement: $originalElement"
        LOG.debug(msg)
        return msg
    }

    override fun generateRenderedDoc(comment: PsiDocCommentBase): String {
        val msg = "generateRenderedDoc, comment: $comment"
        LOG.debug(msg)
        return msg
    }

    override fun getDocumentationElementForLookupItem(
        psiManager: PsiManager?,
        `object`: Any?,
        element: PsiElement?
    ): PsiElement? {
        LOG.debug("getDocumentationElementForLookupItem, object: $`object`, element: $element")
        return super.getDocumentationElementForLookupItem(psiManager, `object`, element)
    }

    override fun getDocumentationElementForLink(
        psiManager: PsiManager?,
        link: String?,
        context: PsiElement?
    ): PsiElement? {
        LOG.debug("getDocumentationElementForLink, link: $link, context: $context")
        return super.getDocumentationElementForLink(psiManager, link, context)
    }
}

