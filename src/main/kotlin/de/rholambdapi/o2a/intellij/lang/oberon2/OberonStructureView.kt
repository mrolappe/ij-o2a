package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.icons.AllIcons
import com.intellij.icons.AllIcons.Nodes
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.*
import com.intellij.ide.util.treeView.smartTree.*
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.editor.Editor
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.*
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.impl.OberonModuleDefImpl

class OberonStructureViewFactory : PsiStructureViewFactory {
    override fun getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder {
        return object : TreeBasedStructureViewBuilder() {
            override fun createStructureViewModel(editor: Editor?): StructureViewModel {
                return OberonStructureViewModel(psiFile, editor)
            }

        }
    }
}

class OberonStructureViewModel(psiFile: PsiFile, editor: Editor?) :
    StructureViewModelBase(psiFile, editor, OberonStructureViewElement(psiFile)),
    StructureViewModel.ElementInfoProvider {

    init {
        withSuitableClasses(
            OberonConstDecl::class.java,
            OberonModuleDef::class.java,
            OberonProcedureDecl::class.java,
            OberonOberonALibProcDecl::class.java,
            OberonTypeDecl::class.java
        )
    }

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement?) = false

    override fun isAlwaysLeaf(element: StructureViewTreeElement?) = when (element) {
        is OberonConstDecl, is OberonTypeDecl, is OberonVarDecl,
        is OberonConstDeclName, is OberonTypeDeclName, is OberonVarDeclName,
        is OberonOberonALibProcDecl -> true

        else -> false
    }

    override fun getGroupers(): Array<Grouper> {
        return arrayOf(/*TmpDummyGrouper()*/)
    }

    override fun getFilters(): Array<Filter> {
        return arrayOf(TmpDummyFilter())
    }

    override fun getSorters(): Array<Sorter> = arrayOf(Sorter.ALPHA_SORTER/*, MySorter1, MySorter2*/)
}


abstract class BaseNode<T : NavigatablePsiElement>(protected val element: T) : StructureViewTreeElement {
    override fun navigate(requestFocus: Boolean) = element.navigate(requestFocus)
    override fun canNavigate() = element.canNavigate()
    override fun canNavigateToSource() = element.canNavigateToSource()
    override fun getValue() = element
}

class OberonStructureViewElement(element: NavigatablePsiElement, private val separateEntryPerSection: Boolean = false) :
    BaseNode<NavigatablePsiElement>(element), SortableTreeElement {
    override fun getPresentation(): ItemPresentation {
        // TODO
        return element.presentation ?: PresentationData().apply {
            presentableText = when (element) {
                is OberonConstDeclName -> element.constantName.text
                is OberonModuleDef -> (element as OberonModuleDefImpl).name
                is OberonModuleInit -> "<module init>"
                is OberonOberonALibProcDecl -> element.procDeclName.procedureName.text
                is OberonTypeDeclName -> element.typeName.text
                is OberonVarDeclName -> element.varName.text
                else -> "$element"
            }

            when (element) {
                is OberonConstDeclName -> AllIcons.Nodes.Constant
                is OberonModuleDef -> AllIcons.Nodes.Module
                is OberonModuleInit -> AllIcons.Nodes.ClassInitializer
                is OberonProcedureDecl, is OberonOberonALibProcDecl -> AllIcons.Nodes.Function
                is OberonTypeDeclName -> AllIcons.Nodes.Type
                is OberonVarDeclName -> AllIcons.Nodes.Variable
                else -> null
            }?.let { setIcon(it) }
        }
    }

    override fun getChildren(): Array<TreeElement> {
        return when (element) {
            is OberonFile ->
                PsiTreeUtil.getChildrenOfTypeAsList(element, OberonModuleDef::class.java)
                    .map { OberonStructureViewElement(it) }
                    .toTypedArray()

            is OberonModuleDef -> {
                val treeElements = mutableListOf<TreeElement>()

                element.moduleInit?.let { treeElements.add(ModuleInitNode(it)) }

                // TODO separate entry per section? as option?
                val varSections = element.topLevelDecls.varSectionList

                if (separateEntryPerSection) {
                    varSections
                        .map { section -> section.varDeclList }
                        .map { varDecls -> varDecls.map { it.varDeclNameList.varDeclNameList }.mapTo(treeElements) { VarsNode(it) } }
                } else {
                    varSections
                        .flatMap { it.varDeclList }
                        .mapTo(treeElements) { VarsNode(it.varDeclNameList.varDeclNameList) }
                }

                element.topLevelDecls.procedureDeclList
                    .mapTo(treeElements) { ProcedureNode(it) }

                if (separateEntryPerSection) {
                    element.topLevelDecls.constSectionList
                        .mapTo(treeElements) { ConstSectionNode(it.constDeclList) }
                } else {
                   element.topLevelDecls.constSectionList
                       .flatMap { it.constDeclList }
                       .toList()
                       .also { treeElements.add(ConstSectionNode(it)) }
                }

                if (separateEntryPerSection) {
                    element.topLevelDecls.typeSectionList
                        .filter { it.typeDeclList.isNotEmpty() }
                        .mapTo(treeElements) { TypeSectionNode(it.typeDeclList) }
                } else {
                    element.topLevelDecls.typeSectionList
                        .flatMap { it.typeDeclList }
                        .toList()
                        .also { if (it.isNotEmpty()) treeElements.add(TypeSectionNode(it)) }
                }

                treeElements.toTypedArray()
            }

            is OberonProcedureDecl ->
                PsiTreeUtil.getChildrenOfTypeAsList(element, OberonProcedureDecl::class.java)
                    .map { ProcedureNode(it) }
                    .toTypedArray()

            else -> TreeElement.EMPTY_ARRAY
        }
    }

    override fun getAlphaSortKey() = element.name ?: ""
}

class ModuleInitNode(moduleInit: OberonModuleInit) : BaseNode<OberonModuleInit>(moduleInit) {
    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText() = "<module init>"
            override fun getIcon(unused: Boolean) = Nodes.ClassInitializer
        }
    }

    override fun getChildren(): Array<TreeElement> = TreeElement.EMPTY_ARRAY
}

class ConstNode(constDecl: OberonConstDecl) : BaseNode<OberonConstDecl>(constDecl) {
    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText() = element.constantNameAsString
            override fun getIcon(unused: Boolean) = Nodes.Constant
        }
    }

    override fun getChildren(): Array<TreeElement> = TreeElement.EMPTY_ARRAY
}

data class ConstSectionNode(private val elements: List<OberonConstDecl>) : StructureViewTreeElement {
    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText() = "Constants"
            override fun getIcon(unused: Boolean) = null
        }
    }

    override fun getChildren(): Array<TreeElement> = elements.map { ConstNode(it) }.toTypedArray()
    override fun getValue() = elements
}

data class TypeSectionNode(private val elements: List<OberonTypeDecl>) : StructureViewTreeElement {
    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText() = "Types"
            override fun getIcon(unused: Boolean) = null
        }
    }

    override fun getChildren(): Array<TreeElement> = elements.map { TypeNode(it.typeDeclName) }.toTypedArray()
    override fun getValue() = elements
}

class TypeNode(element: OberonTypeDeclName) : BaseNode<OberonTypeDeclName>(element) {
    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText() = element.name
            override fun getIcon(unused: Boolean) = AllIcons.Nodes.Type
        }
    }

    override fun getChildren(): Array<TreeElement> = TreeElement.EMPTY_ARRAY
}

data class VarsNode(private val elements: List<OberonVarDeclName>) : StructureViewTreeElement {
    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText() = "Variables"
            override fun getIcon(unused: Boolean) = null
        }
    }

    override fun getChildren(): Array<TreeElement> = elements.map { VarNode(it) }.toTypedArray()

    override fun getValue() = elements
}

class VarNode(element: OberonVarDeclName) : BaseNode<OberonVarDeclName>(element) {
    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText() = element.name

            override fun getIcon(unused: Boolean) = AllIcons.Nodes.Variable
        }
    }

    override fun getChildren(): Array<TreeElement> = TreeElement.EMPTY_ARRAY
}

val OberonConstDecl.constantNameAsString
    get() = constDeclName.name

val OberonModuleDef.moduleInit
    get() = moduleTail.moduleInit

val PsiElement.procedureName
    get() = when (this) {
        is OberonProcedureDecl -> procDeclName.procedureName.text
        is OberonOberonALibProcDecl -> procDeclName.procedureName.text
        else -> null
    }


class ProcedureNode(element: Oberon2ProcedureElement) : BaseNode<Oberon2ProcedureElement>(element) {
    private val presentation: ItemPresentation

    init {
        val text = when (element) {
            is OberonProcedureDecl -> element.procDeclName.procedureName.text
            is OberonOberonALibProcDecl -> element.procDeclName.procedureName.text
            is OberonOberonAExternalProcDecl -> element.procDeclName.procedureName.text
            is OberonAmigaOberonExternalProcDecl -> element.identDef.nameIdentifier.text    // TODO
            else -> null!!
        }

        presentation = object : ItemPresentation {
            override fun getPresentableText() = text
            override fun getIcon(unused: Boolean) = AllIcons.Nodes.Function
        }
    }

    override fun getPresentation(): ItemPresentation = presentation

    override fun getChildren(): Array<TreeElement> {
        return when (element) {
            is OberonProcedureDecl -> {
                val procedures: Collection<OberonProcedureDecl> = localProceduresOf(element)
                procedures
                    .map { ProcedureNode(it) }
                    .toTypedArray<TreeElement>()
            }

            is OberonOberonALibProcDecl -> TreeElement.EMPTY_ARRAY
            else -> throw IllegalArgumentException(element.toString())
        }
    }

    override fun getValue() = element

    private fun localProceduresOf(scope: OberonProcedureDecl): Collection<OberonProcedureDecl> =
        scope.procedureDeclList
}
