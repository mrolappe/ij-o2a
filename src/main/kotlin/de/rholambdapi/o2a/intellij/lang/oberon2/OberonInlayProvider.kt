package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.codeInsight.hints.*
import com.intellij.codeInsight.hints.presentation.*
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiUtilCore
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.impl.procedureName
import java.awt.Point
import javax.swing.JPanel

private const val ACTION_ID_NAVIGATE_TO_CONTENTS =
    "me.mrolappe.intellij.lang.amigaguide.editor.NavigateToContentsNodeAction"
private const val ACTION_ID_NAVIGATE_TO_NEXT_NODE =
    "me.mrolappe.intellij.lang.amigaguide.editor.NavigateToNextNodeAction"
private const val ACTION_ID_NAVIGATE_TO_PREVIOUS_NODE =
    "me.mrolappe.intellij.lang.amigaguide.editor.NavigateToPreviousNodeAction"

class OberonInlayProvider : InlayHintsProvider<NoSettings> {
    override fun createConfigurable(settings: NoSettings): ImmediateConfigurable {
        return object : ImmediateConfigurable {
            override fun createComponent(listener: ChangeListener) = JPanel()
        }
    }

    override fun createSettings() = NoSettings()

    override fun getCollectorFor(
        file: PsiFile,
        editor: Editor,
        settings: NoSettings,
        sink: InlayHintsSink
    ): InlayHintsCollector? {
//        println("OberonInlayProvider, file: $file, editor: $editor, settings:$settings, sink: $sink")

        if (file.fileType != OberonFileType.INSTANCE) {
            return null
        }

        return object : FactoryInlayHintsCollector(editor) {
            override fun collect(element: PsiElement, editor: Editor, sink: InlayHintsSink): Boolean {
//                println("oberon inlay hints collector, element: $element, editor: $editor, sink: $sink")

                if (PsiUtilCore.getElementType(element) != de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonTypes.BEGIN) return true

                when (val parent = element.parent) {
                    is de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonModuleInit -> {
                        val elDelgadoText = factory.smallTextWithoutBackground("module init")
                        val elDelegado = MenuOnClickPresentation(elDelgadoText, editor.project!!) {
                            listOf(
                                ACTION_ID_NAVIGATE_TO_CONTENTS,
                                ACTION_ID_NAVIGATE_TO_NEXT_NODE,
                                ACTION_ID_NAVIGATE_TO_PREVIOUS_NODE
                            )
                                .mapNotNull { ActionManager.getInstance().getAction(it) }
                        }
//                        delegate.addListener(listener = PresentationListener {})

                        val presentationWrapper = BiStatePresentation({ elDelegado }, { factory.text("el delegado") }, true)
                        editor.scrollingModel.addVisibleAreaListener { e ->
                            val visualPosition = e.editor.caretModel.visualPosition
//                            println("editor visible area changed, event: $e, vis pos: $visualPosition")

                            if (visualPosition.column % 8 == 0) {
                                presentationWrapper.setSecond()
                            } else {
                                presentationWrapper.setFirst()
                            }
                        }

                        sink.addBlockElement(
                            element.startOffset, relatesToPrecedingText = true, showAbove = true, 0,
//                            VerticalListInlayPresentation(listOf(factory.text("eins"), factory.text("zwo")))
                            presentationWrapper
                        )
                    }

                    is de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonProcedureDecl -> {
                        val procedureDecl = parent
                        val procedureName = procedureDecl.procedureName

//                        ApplicationManager.getApplication().invokeLater {
//                            val procedurePos = editor.offsetToLogicalPosition(procedureDecl.textOffset)
//                            val beginPos = editor.offsetToLogicalPosition(element.textOffset)
//                            println("log pos ; procedure $procedureName, ${procedurePos}, begin: $beginPos")
//                        }

                        val procedureNameInlay = BiStatePresentation({ factory.roundWithBackground(factory.smallTextWithoutBackground("procedure $procedureName")) },
                            { SpacePresentation(1, 1) }, true)

                        ApplicationManager.getApplication().invokeLater {
                            editor.scrollingModel.addVisibleAreaListener {
//                                println("visible area, old: ${it.oldRectangle}, new: ${it.newRectangle}")
                                val origin = Point(0, 0)
//                                println("origin to logical: ${editor.xyToLogicalPosition(origin)}, to visual: ${editor.xyToVisualPosition(origin)}")
                                if (isElementVisibleInEditor(procedureDecl, editor)) procedureNameInlay.setSecond() else procedureNameInlay.setFirst()
                            }
                        }

                        sink.addInlineElement(
                            element.endOffset,
                            relatesToPrecedingText = true, presentation = factory.seq(
                                procedureNameInlay,
                                //                            factory.smallText("small text"),
                                //                            factory.collapsible(factory.text("collapsible prefix"), factory.text("collapsible collapsed"), { factory.text("collapsible expanded") }, factory.text("collapsible suffix")),
                                //                            factory.button(factory.smallText("kling"), factory.smallText("klang"), clickListener = null, hoverListener = null),
                                //                            factory.withTooltip("winnetou", factory.icon(AllIcons.Providers.Apache)),
                                //                            factory.roundWithBackgroundAndSmallInset(factory.smallScaledIcon(AllIcons.Gutter.Colors)),
                                //                            factory.psiSingleReference(factory.smallText("gehe modül")) { element.parentOfType<OberonModuleDef>() }
                            ),
                            placeAtTheEndOfLine = false
                        )
                    }
                }

                return true
            }

            private fun isElementVisibleInEditor(element: PsiElement, editor: Editor): Boolean {
                val visibleArea = editor.scrollingModel.visibleArea
                val elementXy = editor.offsetToXY(element.textOffset)
                val isVisible = visibleArea.contains(elementXy)
                val name = if (element is de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonProcedureDecl) element.procedureName else null

//                println("isElementVisibleInEditor, element: $element (name: $name), offset: ${element.textOffset}, editor: $editor, xy: $elementXy, vis area: $visibleArea -> $isVisible")
                return isVisible
            }

        }
    }

    companion object {
        private val settingsKey = SettingsKey<NoSettings>("OberonInlayProviderSettingsKey")
    }

    override val key: SettingsKey<NoSettings>
        get() = settingsKey

    override val name: String
        get() = "Oberon inlays"

    override val previewText: String
        get() = "Oberon inlays preview text"
}