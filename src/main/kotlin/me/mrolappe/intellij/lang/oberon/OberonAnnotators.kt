package me.mrolappe.intellij.lang.oberon

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import com.intellij.psi.util.elementType
import me.mrolappe.intellij.lang.oberon.psi.OberonConstDeclName
import me.mrolappe.intellij.lang.oberon.psi.OberonTypes.IDENT

/**
 * Semantic highlighting for constant names (for now only declaration site)
 */
class ConstAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
//        println("ConstAnnotator::annotate, element: $element")

        if (element.elementType == IDENT && element.parent is OberonConstDeclName) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(element)
                .textAttributes(OberonSyntaxHighlighterColors.CONSTANT)
                .create()
        }
    }
}

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

val PREDEFINED_IDENTIFIERS = PREDEFINED_TYPE_IDENTIFIERS union PREDEFINED_PROCEDURE_IDENTIFIERS union PREDEFINED_FUNCTION_IDENTIFIERS union
        PREDEFINED_CONSTANT_IDENTIFIERS union ADDITIONAL_AMIGA_OBERON_TYPE_IDENTIFIERS

class PredeclaredIdentifierAnnotator : Annotator {
    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element.elementType == IDENT && (PREDEFINED_IDENTIFIERS.contains(element.text)
                    || PREDEFINED_TYPE_IDENTIFIERS.contains(element.text) || ADDITIONAL_AMIGA_OBERON_TYPE_IDENTIFIERS.contains(element.text))
        ) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .range(element)
                .textAttributes(OberonSyntaxHighlighterColors.PREDEFINED_SYMBOL)
                .create()
        }
    }
}