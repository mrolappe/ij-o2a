package me.mrolappe.intellij.lang.oberon

import com.intellij.formatting.*
import com.intellij.psi.codeStyle.CodeStyleSettings
import me.mrolappe.intellij.lang.oberon.psi.OberonTypes.*

class OberonFormattingModelBuilder : FormattingModelBuilder {
    override fun createModel(formattingContext: FormattingContext): FormattingModel {
        val element = formattingContext.psiElement
        val settings = formattingContext.codeStyleSettings

        val rootBlock = OberonBlock(
            element.node!!, Wrap.createWrap(WrapType.NONE, false),
            Alignment.createAlignment(), createSpacingBuilder(settings))

        return FormattingModelProvider.createFormattingModelForPsiFile(
            element.containingFile,
            rootBlock, settings
        )
    }

    private fun createSpacingBuilder(settings: CodeStyleSettings) =
        SpacingBuilder(settings, OberonLanguage.INSTANCE)
            .around(COLON_EQUALS)
            .spaceIf(settings.getCommonSettings(OberonLanguage.INSTANCE).SPACE_AROUND_ASSIGNMENT_OPERATORS)
            .around(LPAREN).none()
            .around(RPAREN).none()

            .before(PROCEDURE)
            .none()

//            .after(OberonTypes.PROCEDURE).spaces(7)
//            .after(OberonTypes.IMPORT).spaces(13)
}