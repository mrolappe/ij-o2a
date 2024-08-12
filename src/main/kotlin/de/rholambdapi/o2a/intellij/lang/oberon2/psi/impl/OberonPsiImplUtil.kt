package de.rholambdapi.o2a.intellij.lang.oberon2.psi.impl

import com.intellij.icons.AllIcons
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.PsiElementBase
import com.intellij.psi.search.LocalSearchScope
import com.intellij.psi.search.SearchScope
import com.intellij.psi.util.elementType
import com.intellij.psi.util.nextLeaf
import com.intellij.psi.util.parentOfType
import de.rholambdapi.o2a.intellij.lang.oberon2.OberonElementFactory
import de.rholambdapi.o2a.intellij.lang.oberon2.OberonIcons
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.*
import javax.swing.Icon

object OberonPsiImplUtil {
    private val log = Logger.getInstance(OberonPsiImplUtil::class.java)

    @JvmStatic
//    fun getName(element: OberonProcedureDecl): String = "OberonPsiImplUtil.getName: ${element.node.findChildByType(OberonTypes.IDENT)?.text}"
    fun getName(element: OberonProcedureDecl): String = "${
        element.node.findChildByType(
            OberonTypes.IDENT
        )?.text
    }"

    @JvmStatic
    fun getName(element: OberonModuleDef): String = element.moduleHead.moduleName.text ?: "??? module name"

    @JvmStatic
    fun getName(element: OberonModuleHead): String = element.moduleName.text ?: "??? module name"

//    @JvmStatic
//    fun getName(element: OberonModuleDefName): String = "module def name: ${element.moduleName.text}"

    @JvmStatic
    fun getName(element: OberonIdentDef): String {
        println("OberonPsiImplUtil::getName(OberonIdentDef), element: $element")
        return element.ident.text
    }

    @JvmStatic
    fun getName(element: OberonOberonAExternalProcDecl) = "ext proc: ${element.procDeclName.procedureName.text}"

    @JvmStatic
    fun getName(element: OberonOberonALibProcDecl): String = element.identDef.ident.text

    @JvmStatic
    fun getName(element: OberonConstDecl): String = getName(element.constDeclName)

    @JvmStatic
    fun getName(element: OberonConstDeclName): String = element.constantName.text

    @JvmStatic
    fun getName(element: OberonImportAlias): String = element.aliasName.text

    @JvmStatic
    fun getName(element: OberonVarDeclName): String = element.varName.text

    @JvmStatic
    fun getName(element: OberonProcDeclName): String = element.procedureName.text

    @JvmStatic
    fun getName(element: OberonTypeDeclName): String = element.typeName.text

    @JvmStatic
    fun getName(element: OberonTypeDecl): String = getName(element.typeDeclName)

    @JvmStatic
    fun getName(element: OberonFormalParamName): String = element.paramName.text

    @JvmStatic
    fun getName(element: OberonReceiverName): String = element.nameIdentifier.text

    @JvmStatic
    fun setName(element: OberonModuleDef, newName: String): PsiElement {
        val newHead = OberonElementFactory.createModuleHead(element.project, newName)
        element.moduleHead.replace(newHead)
        return element
    }

//    @JvmStatic
//    fun setName(element: OberonProcedureDecl, newName: String): PsiElement {
//        println("TODO OberonPsiImplUtil::setName, OberonProcedureDecl element: $element, newName: $newName")
//        element.node.findChildByType(OberonTypes.IDENT)?.also {
//            // TODO
//        }
//
//        return element
//    }

    @JvmStatic
    fun setName(element: OberonOberonAExternalProcDecl, newName: String): PsiElement {
        println("TODO OberonPsiImplUtil::setName, OberonOberonAExternalProcDecl element: $element, newName: $newName")
        element.node.findChildByType(OberonTypes.IDENT)?.also {
            // TODO
        }

        return element
    }

    @JvmStatic
    fun setName(element: OberonConstDecl, newName: String): PsiElement {
        println("TODO OberonPsiImplUtil::setName, OberonConstDecl element: $element, newName: $newName")
        element.node.findChildByType(OberonTypes.IDENT)?.also {
            // TODO
        }

        return element
    }

    @JvmStatic
    fun setName(element: OberonConstDeclName, newName: String): PsiElement {
        println("TODO OberonPsiImplUtil::setName, OberonConstDeclName element: $element, newName: $newName")
        element.node.findChildByType(OberonTypes.IDENT)?.also {
            // TODO
        }

        return element
    }

    @JvmStatic
    fun setName(element: OberonOberonALibProcDecl, newName: String): PsiElement {
        println("TODO OberonPsiImplUtil::setName, OberonOberonALibProcDecl element: $element, newName: $newName")
        element.node.findChildByType(OberonTypes.IDENT)?.also {
            // TODO
        }

        return element
    }

    @JvmStatic
    fun setName(element: OberonImportAlias, newName: String): PsiElement {
        println("TODO OberonPsiImplUtil::setName, OberonImportAlias element: $element, newName: $newName")
        element.node.findChildByType(OberonTypes.IDENT)?.also {
            // TODO
        }

        return element
    }

    @JvmStatic
    fun setName(element: OberonIdentDef, newName: String): PsiElement {
        println("TODO OberonPsiImplUtil::setName, OberonIdentDef element: $element, newName: $newName")
        element.node.findChildByType(OberonTypes.IDENT)?.also {
            // TODO
        }

        return element
    }

    @JvmStatic
    fun setName(element: OberonVarDeclName, newName: String): PsiElement {
        println("TODO OberonPsiImplUtil::setName, OberonVarDeclName element: $element, newName: $newName")
        element.node.findChildByType(OberonTypes.IDENT)?.also {
            // TODO
        }

        return element
    }

    @JvmStatic
    fun setName(element: OberonProcedureDecl, newName: String): PsiElement {
        log.debug("setName, proc decl: $element, newName: $newName")

        setName(element.procDeclName, newName)
        return element
    }

    @JvmStatic
    fun setName(element: OberonProcDeclName, newName: String): PsiElement {
        log.debug("setName, proc decl name: $element, newName: $newName")

        val decl = OberonElementFactory.createEmptyNoArgProcedure(element.project, newName)
        element.procedureName.replace(decl.procDeclName.procedureName)
        val procedureDecl = element.parentOfType<OberonProcedureDecl>()

        if (procedureDecl == null) {
            log.error("Containing procedure of $element not found (while setName; newName: $newName)")
            return element
        }

        val newEndIdentifier = decl.procedureDeclBodyBlock?.procedureDeclTail?.endIdentifier

        if (newEndIdentifier == null) {
            log.error("New end identifier was null")
            return element
        }

        procedureDecl.procedureDeclBodyBlock?.procedureDeclTail?.endIdentifier
            ?.replace(newEndIdentifier)
        return element
    }

//    @JvmStatic
//    fun setName(element: OberonModuleDefName, newName: String): PsiElement {
//        println("TODO OberonPsiImplUtil::setName, OberonModuleDefName element: $element, newName: $newName")
//        element.node.findChildByType(OberonTypes.IDENT)?.also {
//            // TODO
//        }
//
//        return element
//    }

    @JvmStatic
    fun setName(element: OberonModuleHead, newName: String): PsiElement {
        val newElement = OberonElementFactory.createModuleHead(element.project, newName).moduleName

        return newElement?.let { element.moduleName?.replace(it) } ?: element
    }

    @JvmStatic
    fun setName(element: OberonTypeDeclName, newName: String): PsiElement {
        println("TODO OberonPsiImplUtil::setName, OberonTypeDeclName element: $element, newName: $newName")
        element.node.findChildByType(OberonTypes.IDENT)?.also {
            // TODO
        }

        return element
    }

    @JvmStatic
    fun setName(element: OberonFormalParamName, newName: String): PsiElement {
        println("TODO OberonPsiImplUtil::setName, OberonFormalParamName element: $element, newName: $newName")
        element.node.findChildByType(OberonTypes.IDENT)?.also {
            // TODO
        }

        return element
    }

    @JvmStatic
    fun setName(element: OberonReceiverName, newName: String): PsiElement {
        println("TODO OberonPsiImplUtil::setName, OberonReceiverName element: $element, newName: $newName")
        element.node.findChildByType(OberonTypes.IDENT)?.also {
            // TODO
        }

        return element
    }

    @JvmStatic
    fun getNameIdentifier(element: OberonProcedureDecl): PsiElement? =
        element.procDeclName

    @JvmStatic
    fun getNameIdentifier(element: OberonModuleDef): PsiElement? = element.moduleHead.moduleName

    @JvmStatic
    fun getNameIdentifier(element: OberonOberonAExternalProcDecl): PsiElement = element.procDeclName.procedureName

    @JvmStatic
    fun getNameIdentifier(element: OberonOberonALibProcDecl): PsiElement = element.identDef.ident

    @JvmStatic
    fun getNameIdentifier(element: OberonConstDecl): PsiElement = element.constDeclName.constantName

    @JvmStatic
    fun getNameIdentifier(element: OberonImportAlias): PsiElement = element.aliasName

    @JvmStatic
    fun getNameIdentifier(element: OberonIdentDef): PsiElement = element.ident

    @JvmStatic
    fun getNameIdentifier(element: OberonVarDeclName): PsiElement = element.varName

    @JvmStatic
    fun getNameIdentifier(element: OberonProcDeclName): PsiElement = element.procedureName

//    @JvmStatic
//    fun getNameIdentifier(element: OberonModuleDefName): PsiElement = element.moduleName

    @JvmStatic
    fun getNameIdentifier(element: OberonModuleHead): PsiElement = element.moduleName

    @JvmStatic
    fun getNameIdentifier(element: OberonTypeDeclName): PsiElement = element.typeName

    @JvmStatic
    fun getNameIdentifier(element: OberonFormalParamName): PsiElement = element.paramName

    @JvmStatic
    fun getNameIdentifier(element: OberonReceiverName): PsiElement = element

    @JvmStatic
    fun getPresentation(element: OberonModuleDef): ItemPresentation {
        return object : ItemPresentation {
            //            override fun getPresentableText(): String = element.moduleDefName.moduleName.text!!
            override fun getPresentableText(): String = "item presentation presentable text für OberonModuleDef"

            override fun getLocationString(): String? = element.containingFile?.name

            override fun getIcon(unused: Boolean): Icon = AllIcons.Nodes.Package
        }
    }

    @JvmStatic
    fun getPresentation(element: OberonModuleHead): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText(): String = "item presentation presentable text für OberonModuleHead"

            override fun getLocationString(): String? = element.containingFile?.name

            override fun getIcon(unused: Boolean): Icon = OberonIcons.MODULE
        }
    }

    @JvmStatic
    fun getReference(referencingElement: OberonImportModuleReference): PsiReference {
        val newReference = if (referencingElement.text.equals("SYSTEM")) {
            OberonPseudoModuleReference(referencingElement)
        } else {
            OberonModuleReference(referencingElement)
        }

        log.debug("getReference, referencing OberonImportModuleReference: $referencingElement -> newReference: $newReference")
        return newReference
    }

    @JvmStatic
    fun getReference(referencingElement: OberonProcCallStmt): PsiReference {
        val newReference = OberonDesignatorReference(referencingElement.procedureDesignator)
//        println("getReference, referencing OberonProcCallStmt: $referencingElement")
        return newReference
    }

    @JvmStatic
    fun getReference(referencingElement: OberonDesignator): PsiReference {
//        System.err.println("OberonPsiImplUtil::getReference(OberonDesignator)")
        return OberonDesignatorReference(referencingElement)
    }

    @JvmStatic
    fun getReference(referencingElement: OberonReceiverType): PsiReference {
        val reference = OberonTypeReference(referencingElement, TextRange(0, referencingElement.textLength))
//        println("OberonPsiImplUtil::getReference(OberonReceiverType) -> $reference")
        return reference
    }

    @JvmStatic
    fun getReferences(referencingElement: OberonQualIdent): Array<PsiReference> {
        val references = mutableListOf<PsiReference>()
        val elementText = referencingElement.text
        var startIdx = 0
        var endIdx = elementText.indexOf('.')
        var partIdx = 0
        var parentReference: OberonQualIdentReference? = null

        while (endIdx != -1) {
            parentReference =
                OberonQualIdentReference(referencingElement, TextRange(startIdx, endIdx), partIdx, parentReference)
                    .also { references.add(it) }

            ++partIdx
            startIdx = endIdx + 1
            endIdx = elementText.indexOf('.', startIndex = ++endIdx)
        }

        OberonQualIdentReference(referencingElement, TextRange(startIdx, elementText.length), partIdx, parentReference)
            .also { references.add(it) }

        log.debug("OberonPsiImplUtil::getReferences(OberonQualIdent), text: ${referencingElement.text} -> references: $references")
        return references.toTypedArray()
    }

    @JvmStatic
    fun getReference(referencingElement: OberonOberonASharedLibLvData): PsiReference {
//        System.err.println("OberonPsiImplUtil::getReference(OberonOberonASharedLibLvData)")
        return OberonSharedLibBaseVarReference(
            referencingElement,
            referencingElement.baseVarName.text,
            referencingElement.baseVarName.textRangeInParent
        )
    }

    @JvmStatic
    fun getReference(referencingElement: OberonAmigaOberonSharedLibLvData): PsiReference {
//        System.err.println("OberonPsiImplUtil::getReference(OberonOberonASharedLibLvData)")
        return OberonSharedLibBaseVarReference(
            referencingElement,
            referencingElement.baseVarName.text,
            referencingElement.baseVarName.textRangeInParent
        )
    }

    @JvmStatic
    fun getUseScope(paramName: OberonFormalParamName): SearchScope {
        val procedureDecl = paramName.parentOfType<OberonProcedureDecl>()
        val scope = procedureDecl?.let { LocalSearchScope(it) } ?: (paramName as PsiElementBase).useScope
        log.debug("OberonPsiImplUtil::getUseScope(OberonFormalParamName), paramName: $paramName, procedure: $procedureDecl -> scope $scope")
        return scope
    }

    @JvmStatic
    fun markedForExport(procedureDecl: OberonProcedureDecl): Boolean {
        val readWriteExportMark = procedureDecl.procDeclName?.readWriteExportMark
        val readOnlyExportMark = procedureDecl.procDeclName?.readOnlyExportMark
        val markedForExport = readWriteExportMark != null || readOnlyExportMark != null
//        println("OberonPsiImplUtil::markedForExport, procedure identdef: ${procedureDecl.identDef.text} -> $markedForExport")
        return markedForExport
    }

    @JvmStatic
    fun isMarkedForExport(decl: OberonOberonALibProcDecl): Boolean {
        val readWriteExportMark = decl.identDef.readWriteExportMark
        val readOnlyExportMark = decl.identDef.readOnlyExportMark
        val markedForExport = readWriteExportMark != null || readOnlyExportMark != null
        return markedForExport
    }

    @JvmStatic
    fun markedForExport(constDeclName: OberonConstDeclName): Boolean {
        val readWriteExportMark = constDeclName.readWriteExportMark
        val readOnlyExportMark = constDeclName.readOnlyExportMark
        val markedForExport = readWriteExportMark != null || readOnlyExportMark != null
//        println("OberonPsiImplUtil::markedForExport, constDeclName: ${constDeclName.text}, constant name: ${constDeclName.constantName.text}, export mark: $exportMark, read only export mark: $readOnlyExportMark -> $markedForExport")
        return markedForExport
    }

    @JvmStatic
    fun getLhs(qualified: OberonQualIdentQualified): PsiElement {
        return qualified.firstChild
    }

    @JvmStatic
    fun getRhs(qualified: OberonQualIdentQualified): PsiElement {
        return qualified.firstChild
            .nextLeaf { it.elementType == OberonTypes.DOT }
            ?.nextLeaf(skipEmptyElements = true)!!
    }

    @JvmStatic
    fun getReadWriteExportMark(decl: OberonProcedureDecl): PsiElement? {
        return decl.procDeclName?.readWriteExportMark
    }

    @JvmStatic
    fun getReadOnlyExportMark(decl: OberonProcedureDecl): PsiElement? {
        return decl.procDeclName?.readOnlyExportMark
    }

    @JvmStatic
    fun hasReadWriteExportMark(decl: OberonProcedureDecl): Boolean {
        return decl.procDeclName.readWriteExportMark != null
    }

    @JvmStatic
    fun hasReadOnlyExportMark(decl: OberonProcedureDecl): Boolean {
        return decl.procDeclName.readOnlyExportMark != null
    }

    @JvmStatic
    fun getReadWriteExportMark(decl: OberonConstDecl): PsiElement? {
        return decl.constDeclName.readWriteExportMark
    }

    @JvmStatic
    fun getReadOnlyExportMark(decl: OberonConstDecl): PsiElement? {
        return decl.constDeclName.readOnlyExportMark
    }

    @JvmStatic
    fun getReadWriteExportMark(decl: OberonOberonALibProcDecl): PsiElement? {
        return decl.identDef.readWriteExportMark
    }

    @JvmStatic
    fun getReadOnlyExportMark(decl: OberonOberonALibProcDecl): PsiElement? {
        return decl.identDef.readOnlyExportMark
    }

    @JvmStatic
    fun hasReadWriteExportMark(decl: OberonOberonALibProcDecl): Boolean {
        return decl.identDef.readWriteExportMark != null
    }

    @JvmStatic
    fun hasReadOnlyExportMark(decl: OberonOberonALibProcDecl): Boolean {
        return decl.identDef.readOnlyExportMark != null
    }

    @JvmStatic
    fun getTextOffset(decl: OberonProcedureDecl): Int = getNameIdentifier(decl)?.textOffset ?: -1

    @JvmStatic
    fun getTextOffset(decl: OberonOberonALibProcDecl): Int = getNameIdentifier(decl).textOffset
}
