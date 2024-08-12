package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ex.ProblemDescriptorImpl
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.openapi.diagnostic.trace
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.NlsSafe
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiErrorElement
import com.intellij.psi.PsiWhiteSpace
import com.intellij.psi.util.elementType
import com.intellij.psi.util.parentOfType
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.*
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonTypes.IDENT
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.impl.procedureNameMatches

/**
 * Semantic highlighting for constant names (for now only declaration site)
 */
class ConstAnnotator : Annotator {
    val log = thisLogger()

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element.elementType == IDENT && element.parent is OberonConstDeclName) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(element)
                .textAttributes(OberonSyntaxHighlighterColors.CONSTANT)
                .create()
        }

        val markAndTextAttributes = when {
            hasReadWriteExportMark(element) -> readWriteExportMark(element)!! to OberonSyntaxHighlighterColors.READ_WRITE_EXPORT_MARK
            hasReadOnlyExportMark(element) -> readOnlyExportMark(element)!! to OberonSyntaxHighlighterColors.READ_ONLY_EXPORT_MARK
            else -> null
        }

        log.debug(
            "element: $element, has export mark R/W: ${hasReadWriteExportMark(element)}, R/O: ${
                hasReadOnlyExportMark(
                    element
                )
            }"
        )

        markAndTextAttributes?.let { (markElement, textAttributes) ->
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(markElement)
                .textAttributes(textAttributes)
                .create()
        }
    }

}

fun hasReadOnlyExportMark(element: PsiElement): Boolean {
    return readOnlyExportMark(element) != null
}

fun readOnlyExportMark(element: PsiElement): PsiElement? {
    return when (element) {
        is Oberon2ExportableDeclarationElement -> element.readOnlyExportMark
        else -> null
    }
}

fun hasReadWriteExportMark(element: PsiElement): Boolean {
    return readWriteExportMark(element) != null
}

fun readWriteExportMark(element: PsiElement): PsiElement? {
    return when (element) {
        is Oberon2ExportableDeclarationElement -> element.readWriteExportMark
        is PsiWhiteSpace, is PsiErrorElement -> null
        else -> {
            null
        }
    }
}

private val OberonModuleDef.endIdentifier: PsiElement
    get() = this.moduleTail.endIdentifier!!

val PREDEFINED_TYPE_IDENTIFIERS = setOf(
//            BYTE,
    "BOOLEAN",
    "CHAR",
    "INTEGER",
    "LONGINT",
    "LONGREAL",
    "REAL",
    "SET",
    "SHORTINT",
)

val PREDEFINED_PROCEDURE_IDENTIFIERS = setOf(
    "ASSERT",
    "COPY",
    "DEC",
    "EXCL",
    "HALT",
    "INC",
    "INCL",
    "NEW",
)

val PREDEFINED_FUNCTION_IDENTIFIERS = setOf(
    "ABS",
    "ASH",
    "CAP",
    "CHR",
    "ENTIER",
    "LEN",
    "LONG",
    "MAX",
    "MIN",
    "ODD",
    "ORD",
    "SHORT",
    "SIZE",
)

val ADDITIONAL_AMIGA_OBERON_TYPE_IDENTIFIERS = setOf(
    "SHORTSET",
    "LONGSET"
)

val PREDEFINED_CONSTANT_IDENTIFIERS = setOf(
    "FALSE",
    "TRUE",
)

val PREDEFINED_IDENTIFIERS =
    PREDEFINED_TYPE_IDENTIFIERS union PREDEFINED_PROCEDURE_IDENTIFIERS union PREDEFINED_FUNCTION_IDENTIFIERS union
            PREDEFINED_CONSTANT_IDENTIFIERS union ADDITIONAL_AMIGA_OBERON_TYPE_IDENTIFIERS

class PredeclaredIdentifierAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element.elementType == IDENT && (PREDEFINED_IDENTIFIERS.contains(element.text)
                    || PREDEFINED_TYPE_IDENTIFIERS.contains(element.text) || ADDITIONAL_AMIGA_OBERON_TYPE_IDENTIFIERS.contains(
                element.text
            ))
        ) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(element)
                .textAttributes(OberonSyntaxHighlighterColors.PREDEFINED_SYMBOL)
                .create()
        }
    }
}


class EndIdentifierMismatchAnnotator : Annotator {
    private val log = thisLogger()

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        log.trace { "EndIdentifierMismatchAnnotator::annotate, element: $element" }

        when (element) {
            is OberonProcedureDecl -> {
                val endIdentifier = element.endIdentifier ?: return
                val endIdentifierName = endIdentifier.text

                if (!element.procedureNameMatches(endIdentifierName)) {
                    val procedureName = element.procDeclName.procedureName.text
                    holder.newAnnotation(
                        HighlightSeverity.ERROR,
                        "End identifier does not match procedure name '$procedureName'",
                    )
                        .range(endIdentifier)
                        .newLocalQuickFix(
                            RenameEndIdentifierFix(procedureName),
                            ProblemDescriptorImpl(
                                endIdentifier,
                                endIdentifier,
                                "TODO EndIdentifierMismatchAnnotator problem description template",
                                emptyArray(),
                                ProblemHighlightType.ERROR,
                                true,
                                null,
                                true
                            )
                        ).registerFix()
                        .create()
                }
            }

            is OberonModuleTail -> {
                val endIdentifier = element.endIdentifier
                val endIdentifierName = endIdentifier?.text
                val moduleHead = element.parentOfType<OberonModuleDef>()?.moduleHead!!

                if (endIdentifier != null && moduleHead.nameIdentifier.text != endIdentifierName) {
                    val moduleName = moduleHead.nameIdentifier.text
                    holder.newAnnotation(
                        HighlightSeverity.ERROR,
                        "End identifier '$endIdentifierName' does not match name '$moduleName' in module declaration"
                    )
                        .range(endIdentifier)
                        .newLocalQuickFix(
                            RenameEndIdentifierFix(moduleName), ProblemDescriptorImpl(
                                endIdentifier,
                                endIdentifier,
                                "TODO EndIdentifierMismatchAnnotator problem description template",
                                emptyArray(),
                                ProblemHighlightType.ERROR,
                                true,
                                null,
                                true
                            )
                        ).registerFix()
                        .create()
                }
            }
        }
    }
}

val OberonProcedureDecl.endIdentifier: PsiElement?
    get() = procedureDeclBodyBlock?.procedureDeclTail?.endIdentifier

class RenameEndIdentifierFix(private val targetName: @NlsSafe String) : LocalQuickFix {
    private val log = thisLogger()

    override fun getFamilyName() = "Change end identifier"

    override fun getName() = "Change end identifier to '$targetName'"

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val element = descriptor.psiElement
        val parent = element?.parent
        log.debug("RenameEndIdentifierFix::applyFix, project: $project, descriptor: $descriptor, element: $element, element parent: $parent")

        when (parent) {
            is OberonProcedureDeclTail -> {
                OberonElementFactory.createEmptyNoArgProcedure(
                    element.project,
                    targetName
                ).procedureDeclBodyBlock?.procedureDeclTail?.endIdentifier?.let {
                    parent.endIdentifier.replace(it)
                }
            }

            is OberonModuleTail -> {
                OberonElementFactory.createEmptyModule(project, targetName).moduleTail.endIdentifier?.let {
                    parent.endIdentifier?.replace(it)
                }
            }

            else -> log.warn("Unexpected type of parent: $parent")
        }

    }
}