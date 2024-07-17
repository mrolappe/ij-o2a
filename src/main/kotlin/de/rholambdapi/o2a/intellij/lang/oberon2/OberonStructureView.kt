package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.icons.AllIcons
import com.intellij.ide.projectView.PresentationData
import com.intellij.ide.structureView.*
import com.intellij.ide.util.treeView.smartTree.SortableTreeElement
import com.intellij.ide.util.treeView.smartTree.Sorter
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.lang.PsiStructureViewFactory
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.editor.Editor
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.*

class OberonStructureViewFactory : PsiStructureViewFactory {
    override fun getStructureViewBuilder(psiFile: PsiFile): StructureViewBuilder {
        return object : TreeBasedStructureViewBuilder() {
            override fun createStructureViewModel(editor: Editor?): StructureViewModel {
                return OberonStructureViewModel(psiFile, editor)
            }

        }
    }
}

class OberonStructureViewModel(psiFile: PsiFile, editor: Editor?) : StructureViewModelBase(psiFile, editor, OberonStructureViewElement(psiFile)),
    StructureViewModel.ElementInfoProvider {

    init {
        withSuitableClasses(OberonModuleDef::class.java, OberonProcedureDecl::class.java)
    }

    override fun isAlwaysShowsPlus(element: StructureViewTreeElement?) = false

    override fun isAlwaysLeaf(element: StructureViewTreeElement?) = false    // TODO

    override fun getSorters(): Array<Sorter> = arrayOf(Sorter.ALPHA_SORTER)
}


class OberonStructureViewElement(private val element: NavigatablePsiElement) : StructureViewTreeElement, SortableTreeElement {
    override fun getPresentation(): ItemPresentation {
        // TODO
        return element.presentation ?: PresentationData().apply {
            presentableText = when (element) {
                is OberonConstDeclName -> element.constantName.text
                is OberonModuleDef -> (element as de.rholambdapi.o2a.intellij.lang.oberon2.psi.impl.OberonModuleDefImpl).name
                is OberonModuleInit -> "<module init>"
                is OberonOberonALibProcDecl -> element.procDeclName.procedureName.text
                is OberonProcedureDecl -> element.ident.text
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
            is OberonFile -> {
                val modules = PsiTreeUtil.getChildrenOfTypeAsList(element, OberonModuleDef::class.java)

                modules
                    .map { OberonStructureViewElement(it) }
                    .toTypedArray()
            }

            is OberonModuleDef -> {
                val result = mutableListOf<PsiElement>()

                PsiTreeUtil.findChildOfType(element, OberonModuleInit::class.java)
                    ?.also { result.add(it) }

                PsiTreeUtil.findChildrenOfType(element, OberonVarDeclName::class.java)
                    .also { result.addAll(it) }

                val procedureDecls = PsiTreeUtil.getChildrenOfAnyType(element, OberonConstDeclName::class.java, OberonProcedureDecl::class.java,
                    de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonOberonAExternalProcDecl::class.java, OberonOberonALibProcDecl::class.java, OberonTypeDeclName::class.java)

                val constDeclNames = PsiTreeUtil.findChildrenOfType(element, de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonConstSection::class.java)
                    .flatMap { section -> PsiTreeUtil.collectElementsOfType(section, OberonConstDeclName::class.java) }

                val typeDeclNames = PsiTreeUtil.findChildrenOfType(element, de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonTypeSection::class.java)
                    .flatMap { section -> PsiTreeUtil.collectElementsOfType(section, OberonTypeDeclName::class.java) }

                result.addAll(constDeclNames)
                result.addAll(typeDeclNames)
                result.addAll(procedureDecls)

                result
                    .map { OberonStructureViewElement(it as NavigatablePsiElement) }
                    .toTypedArray()
            }

            is OberonProcedureDecl -> {
                val procedureDecls = PsiTreeUtil.getChildrenOfTypeAsList(element, OberonProcedureDecl::class.java)

                procedureDecls
                    .map { OberonStructureViewElement(it as NavigatablePsiElement) }
                    .toTypedArray()
            }

            else -> TreeElement.EMPTY_ARRAY
        }

    }

    override fun navigate(requestFocus: Boolean) = element.navigate(requestFocus)

    override fun canNavigate() = element.canNavigate()

    override fun canNavigateToSource() = element.canNavigateToSource()

    override fun getValue() = element

    override fun getAlphaSortKey() = element.name ?: ""

}