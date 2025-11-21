package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.icons.AllIcons
import com.intellij.icons.AllIcons.Nodes
import com.intellij.ide.structureView.*
import com.intellij.ide.util.treeView.smartTree.*
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.editor.Editor
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.*

class OberonStructureViewFactory : PsiStructureViewFactory {
    override fun getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder {
        require(psiFile is OberonFile) { "PSI file must be on Oberon file" }
        return object : TreeBasedStructureViewBuilder() {
            override fun createStructureViewModel(editor: Editor?): StructureViewModel {
                return OberonStructureViewModel(psiFile as OberonFile, editor)
            }

        }
    }
}

class OberonStructureViewModel(file: OberonFile, editor: Editor?) :
    StructureViewModelBase(file, editor, FileNode(file)),
    StructureViewModel.ElementInfoProvider {

    init {
        withSuitableClasses(
            OberonConstDecl::class.java,
            OberonModuleDef::class.java,
            OberonProcedureDecl::class.java,
//            OberonOberonALibProcDecl::class.java,
            OberonTypeDecl::class.java
        )
    }

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement?) = false

    override fun isAlwaysLeaf(element: StructureViewTreeElement?) = when (element) {
        is OberonConstDecl, is OberonTypeDecl, is OberonVarDecl,
        is OberonConstDeclName, is OberonTypeDeclName, is OberonVarDeclName,
            /*is OberonOberonALibProcDecl*/
            -> true

        else -> false
    }

    override fun getGroupers(): Array<Grouper> {
        return arrayOf(/*TmpDummyGrouper()*/)
    }

    override fun getFilters(): Array<Filter> {
        return arrayOf(/*TmpDummyFilter()*/)
    }

    override fun getSorters(): Array<Sorter> = arrayOf(Sorter.ALPHA_SORTER/*, MySorter1, MySorter2*/)
}


abstract class BaseNode<T : NavigatablePsiElement>(protected val element: T) : StructureViewTreeElement {
    override fun navigate(requestFocus: Boolean) = element.navigate(requestFocus)
    override fun canNavigate() = element.canNavigate()
    override fun canNavigateToSource() = element.canNavigateToSource()
    override fun getValue() = element
}

data class FileNode(val file: OberonFile) : BaseNode<NavigatablePsiElement>(file), SortableTreeElement {
    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText() = "File ${file.name}"
            override fun getIcon(unused: Boolean) = OberonIcons.MODULE
        }
    }

    override fun getChildren(): Array<TreeElement> {
        return PsiTreeUtil.getChildrenOfTypeAsList(element, OberonModuleDef::class.java)
            .map { ModuleNode(it) }
            .toTypedArray()
    }

    override fun getAlphaSortKey() = element.name ?: ""
}

class ModuleNode(
    module: OberonModuleDef,
    private val separateEntryPerSection: Boolean = false
) : BaseNode<OberonModuleDef>(module) {
    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText() = "Module ${element.name}"
            override fun getIcon(unused: Boolean) = Nodes.Package
        }
    }

    override fun getChildren(): Array<TreeElement> {
        val treeElements = mutableListOf<TreeElement>()

        element.moduleInit?.let { treeElements.add(ModuleInitNode(it)) }

        // TODO separate entry per section? as option?
        val varSections = element.topLevelDecls.varSectionList

        if (separateEntryPerSection) {
            varSections
                .map { section -> section.varDeclList }
                .map { varDecls ->
                    varDecls.map { it.varDeclNameList.varDeclNameList }.mapTo(treeElements) { VarsNode(it) }
                }
        } else {
            varSections
                .flatMap { it.varDeclList }
                .mapTo(treeElements) { VarsNode(it.varDeclNameList.varDeclNameList) }
        }

        element.topLevelDecls.procedureDeclList.also {
            if (it.isNotEmpty()) {
                treeElements.add(ProceduresNode(it))
            }
        }

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

        return treeElements.toTypedArray()
    }
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

class ConstNode(constDecl: OberonConstDeclName) : BaseNode<OberonConstDeclName>(constDecl) {
    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText() = element.name
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

    override fun getChildren(): Array<TreeElement> = elements.map { ConstNode(it.constDeclName) }.toTypedArray()
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

val Oberon2ProcedureElement.procedureName
    get() = when (this) {
        is OberonProcedureDecl -> procDeclName.procedureName.text
        is OberonOberonALibProcDecl -> identDef.ident.text
        else -> null
    }

data class ProceduresNode(private val elements: List<Oberon2ProcedureElement>) : StructureViewTreeElement {
    override fun getPresentation(): ItemPresentation {
        return object : ItemPresentation {
            override fun getPresentableText() = "Procedures"
            override fun getIcon(unused: Boolean) = null
        }
    }

    override fun getChildren(): Array<TreeElement> = elements.map { ProcedureNode(it) }.toTypedArray()

    override fun getValue() = elements
}

class ProcedureNode(element: Oberon2ProcedureElement) : BaseNode<Oberon2ProcedureElement>(element) {
    private val presentation: ItemPresentation

    init {
        val text = when (element) {
            is OberonProcedureDecl -> element.procDeclName.procedureName.text
            is OberonOberonALibProcDecl -> element.procedureName
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

//            is OberonOberonALibProcDecl -> TreeElement.EMPTY_ARRAY
            else -> throw IllegalArgumentException(element.toString())
        }
    }

    override fun getValue() = element

    private fun localProceduresOf(scope: OberonProcedureDecl): Collection<OberonProcedureDecl> =
        scope.procedureDeclList
}
