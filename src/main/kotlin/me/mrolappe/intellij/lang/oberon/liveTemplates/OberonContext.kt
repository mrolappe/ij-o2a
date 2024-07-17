package me.mrolappe.intellij.lang.oberon.liveTemplates

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType
import me.mrolappe.intellij.lang.oberon.OberonFileType

class OberonContext : TemplateContextType("Oberon") {
    override fun isInContext(templateActionContext: TemplateActionContext) =
        templateActionContext.file.fileType.equals(OberonFileType.INSTANCE)
}