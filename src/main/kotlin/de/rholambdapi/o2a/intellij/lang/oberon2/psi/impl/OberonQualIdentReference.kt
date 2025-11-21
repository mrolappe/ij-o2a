package de.rholambdapi.o2a.intellij.lang.oberon2.psi.impl

import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiNamedElement
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.util.parentOfType
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonQualIdent

private val logger = Logger.getInstance("OberonQualIdentReference")

private fun logDebug(message: String) {
    logger.debug(message)
}

class OberonQualIdentReference(
    private val referencingElement: OberonQualIdent,
    private val textRange: TextRange = TextRange(0, referencingElement.textLength),
    private val partIdx: Int = 0,
    private val parentReference: OberonQualIdentReference? = null
) :
    PsiReferenceBase<OberonQualIdent>(referencingElement, textRange) {

    override fun resolve(): PsiElement? {
//        val qualifier = referencingElement.qualIdentQualifier?.qualifier?.text
//        val identifier = referencingElement.qualIdentIdentifier?.identifier!!.text
        val qualifier = referencingElement.qualIdentQualified?.lhs?.text ?: referencingElement.qualIdentSimple!!.text
        val identifier = referencingElement.qualIdentQualified?.rhs?.text ?: referencingElement.qualIdentSimple!!.text
        val resolveResult = mutableListOf<PsiNamedElement>()

        if (qualifier != null) {
            val procedureDecl = procedureDeclContaining(referencingElement)

            procedureDecl?.let { proc -> procedureParamOrNull(proc, qualifier) }
                ?.also { if (partIdx == 0) { resolveResult.add(it) } }

            if (resolveResult.isEmpty()) {
                procedureDecl?.let { proc -> receiverNameOrNull(proc, qualifier) }
                    ?.also { if (partIdx == 0) { resolveResult.add(it)} }
            }

            if (resolveResult.isEmpty()) {
                procedureDecl?.let { proc -> procedureLocalVarDeclNameOrNull(proc, qualifier) }
                    ?.also { if (partIdx == 0) { resolveResult.add(it) } }
            }

            if (resolveResult.isEmpty()) {
                referencingElement.parentOfType<de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonModuleDef>()
                    ?.let { module ->
                        moduleNameForImportNameOrNull(module, qualifier)
                    }
                    ?.let { moduleName ->
                        resolveByModuleNameAndMemberName(referencingElement, moduleName, identifier)
                            .also { resolveResult.addAll(it) }
                    }
            }
        } else {
            resolveByMemberName(referencingElement, identifier)
                .also { resolveResult.addAll(it) }
        }

        val resolvedElement = resolveResult.firstOrNull()
        logDebug("OberonQualIdentReference, referencing element: $referencingElement, module: $qualifier, member: $identifier -> $resolvedElement (#: ${resolveResult.size})")
        return resolvedElement
    }

    // TODO maybe use variants below for completion by references
//    override fun getVariants(): Array<Any> {
//        println("qual ident ref, getVariants; value: ${this.value}")
//        return arrayOf("qual ident 1", "qual ident1")
//    }
}