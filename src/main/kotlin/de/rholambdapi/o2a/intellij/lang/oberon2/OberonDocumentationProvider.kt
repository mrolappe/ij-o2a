package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.psi.PsiDocCommentBase
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.util.PsiTreeUtil

class OberonDocumentationProvider : AbstractDocumentationProvider() {
    val LOG = thisLogger()

    override fun getQuickNavigateInfo(element: PsiElement?, originalElement: PsiElement?): String =
        "quick navigate info, element: $element, originalElement: $originalElement"

    override fun getUrlFor(element: PsiElement?, originalElement: PsiElement?): List<String> {
        LOG.debug("OberonDocumentationProvider::getUrlFor, super: ${super.getUrlFor(element, originalElement)}")
        return listOf("https://rolappe.it/uno", "https://rolappe.it/dos")
    }

    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String {
        return when {
            element is de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonImportAlias -> {
                val module = PsiTreeUtil.getNextSiblingOfType(element, de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonImportModuleReference::class.java)
                "Import alias for module ${module?.ident?.text}"
            }

            element is de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonIdentDef && element.parent is de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonAmigaOberonSharedLibProcDecl -> autodocFunctionDescriptionByProcName(element.ident.text)
            element is de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonProcDeclName -> lookupAutoDoc(element, originalElement)
            else -> "doc for element $element, original element $originalElement"
        }
    }

    private fun autodocFunctionDescriptionByProcName(procName: String): String {
        val functionDescription = de.rholambdapi.o2a.intellij.autodocFunctionDescriptionOrNull(procName)

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

    private fun lookupAutoDoc(element: de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonProcDeclName, originalElement: PsiElement?): String {
        val procName = element.procedureName.text

        val functionDescription = de.rholambdapi.o2a.intellij.autodocFunctionDescriptionOrNull(procName)

        return "TODO look up autodoc for ${element.procedureName.text}<br/>element: $element<br/>original element: $originalElement<br/>" +
                "element parent: ${element.parent}<br/>text from autodoc:<br/>$functionDescription"
    }

    override fun generateHoverDoc(element: PsiElement, originalElement: PsiElement?): String =
        "hover doc, element: $element, originalElement: $originalElement"

    override fun generateRenderedDoc(comment: PsiDocCommentBase): String {
        return "diz iz super cool rendered doc"
    }

    override fun getDocumentationElementForLookupItem(
        psiManager: PsiManager?,
        `object`: Any?,
        element: PsiElement?
    ): PsiElement? {
        return super.getDocumentationElementForLookupItem(psiManager, `object`, element)
    }

    override fun getDocumentationElementForLink(
        psiManager: PsiManager?,
        link: String?,
        context: PsiElement?
    ): PsiElement? {
        return super.getDocumentationElementForLink(psiManager, link, context)
    }
}

