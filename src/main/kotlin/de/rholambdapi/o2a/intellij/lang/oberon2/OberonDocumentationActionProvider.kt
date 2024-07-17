package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.codeInsight.documentation.DocumentationActionProvider
import com.intellij.codeInsight.documentation.DocumentationComponent
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiDocCommentBase

private const val ACTION_ID_NAVIGATE_TO_CONTENTS = "me.mrolappe.intellij.lang.amigaguide.editor.NavigateToContentsNodeAction"
private const val ACTION_ID_NAVIGATE_TO_LAST_VISITED_NODE = "me.mrolappe.intellij.lang.amigaguide.editor.NavigateToLastVisitedNodeAction"
private const val ACTION_ID_NAVIGATE_TO_NEXT_NODE = "me.mrolappe.intellij.lang.amigaguide.editor.NavigateToNextNodeAction"
private const val ACTION_ID_NAVIGATE_TO_PREVIOUS_NODE = "me.mrolappe.intellij.lang.amigaguide.editor.NavigateToPreviousNodeAction"

class OberonDocumentationActionProvider : DocumentationActionProvider {
    companion object {
        val LOG = thisLogger()
    }

    override fun additionalActions(editor: Editor?, docComment: PsiDocCommentBase?, renderedText: String?): List<AnAction> {
        LOG.info("OberonDocumentationActionProvider::additionalActions, editor: $editor, docComment: $docComment, renderedText: $renderedText")
        val actions = listOf(
            ACTION_ID_NAVIGATE_TO_CONTENTS,
            ACTION_ID_NAVIGATE_TO_LAST_VISITED_NODE,
            ACTION_ID_NAVIGATE_TO_NEXT_NODE,
            ACTION_ID_NAVIGATE_TO_PREVIOUS_NODE
        )
            .mapNotNull { ActionManager.getInstance().getAction(it) }

        return actions
    }

    override fun additionalActions(component: DocumentationComponent?): List<AnAction> {
        LOG.info("OberonDocumentationActionProvider::additionalActions, component: $component")
        return super.additionalActions(component)
    }
}
