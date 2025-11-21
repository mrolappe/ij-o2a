package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.codeInsight.hints.*
import com.intellij.codeInsight.hints.presentation.*
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiUtilCore
import com.intellij.psi.util.parentOfType
import com.intellij.refactoring.suggested.endOffset
import com.intellij.refactoring.suggested.startOffset
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.*
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.impl.procedureName
import java.awt.Point
import javax.swing.JPanel


class OberonInlayProvider : InlayHintsProvider<NoSettings> {
    private val log = thisLogger()

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
        log.debug("OberonInlayProvider, file: $file, editor: $editor, settings:$settings, sink: $sink")

        if (file.fileType != OberonFileType.INSTANCE) {
            return null
        }

        return object : FactoryInlayHintsCollector(editor) {
            override fun collect(element: PsiElement, editor: Editor, sink: InlayHintsSink): Boolean {
//                println("oberon inlay hints collector, element: $element, parent: ${element.parent}")
                log.debug("oberon inlay hints collector, element: $element, editor: $editor, sink: $sink")

                if (PsiUtilCore.getElementType(element) != OberonTypes.BEGIN) return true

                when (val parent = element.parent) {
                    is OberonModuleInit -> {
                        sink.addBlockElement(
                            element.startOffset,
                            relatesToPrecedingText = true,
                            showAbove = true,
                            0,
                            factory.smallTextWithoutBackground("module init")
                        )
                    }

                    // TODO more stable way of checking for procedure etc.
                    is OberonProcedureDeclBody -> {
                        val procedureDecl = parent.parentOfType<OberonProcedureDecl>()!!
                        val procedureName = procedureDecl?.procedureName ?: "???"

                        val presentation1 = {
                            factory.roundWithBackground(
                                factory.smallTextWithoutBackground("procedure $procedureName")
                            )
                        }
                        val presentation2 = { SpacePresentation(1, 1) }
                        val procedureNameInlay = BiStatePresentation(presentation1, presentation2, true)

                        ApplicationManager.getApplication().invokeLater {
                            editor.scrollingModel.addVisibleAreaListener {
                                if (isElementVisibleInEditor(procedureDecl, editor)) {
                                    procedureNameInlay.setSecond()
                                } else {
                                    procedureNameInlay.setFirst()
                                }
                            }
                        }

                        sink.addInlineElement(
                            element.endOffset,
                            relatesToPrecedingText = true,
                            presentation = procedureNameInlay,
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
                val name = if (element is OberonProcedureDecl) element.procedureName else null

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