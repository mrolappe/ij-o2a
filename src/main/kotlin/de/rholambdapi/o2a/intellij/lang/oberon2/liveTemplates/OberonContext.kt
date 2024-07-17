package de.rholambdapi.o2a.intellij.lang.oberon2.liveTemplates

import com.intellij.codeInsight.template.TemplateActionContext
import com.intellij.codeInsight.template.TemplateContextType
import de.rholambdapi.o2a.intellij.lang.oberon2.OberonFileType

class OberonContext : TemplateContextType("Oberon") {
    override fun isInContext(templateActionContext: TemplateActionContext) =
        templateActionContext.file.fileType.equals(OberonFileType.INSTANCE)
}