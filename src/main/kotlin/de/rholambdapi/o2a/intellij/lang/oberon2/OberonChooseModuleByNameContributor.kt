package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.navigation.ChooseByNameContributor
import com.intellij.navigation.ChooseByNameContributorEx
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.util.Processor
import com.intellij.util.containers.ContainerUtil
import com.intellij.util.indexing.FindSymbolParameters
import com.intellij.util.indexing.IdFilter

// TODO use ChooseByNameContributorEx
class OberonChooseModuleByNameContributor : ChooseByNameContributor {
    override fun getNames(project: Project, includeNonProjectItems: Boolean): Array<String> {
        // TODO use IJ indexing framework
        val modules = OberonUtil.findAllModules(project)
        return modules.map { it.moduleDefName.moduleName.text!! }.toTypedArray()
    }

    override fun getItemsByName(
        name: String?,
        pattern: String?,
        project: Project?,
        includeNonProjectItems: Boolean
    ): Array<NavigationItem> {
        if (project == null || name == null) return NavigationItem.EMPTY_NAVIGATION_ITEM_ARRAY

        return OberonUtil.findModulesByName(project, name)
            .map { it }
            .toTypedArray()
    }
}

class OberonChooseTypeByNameContributor : ChooseByNameContributorEx {
    override fun processNames(processor: Processor<in String>, scope: GlobalSearchScope, filter: IdFilter?) {
        scope.project?.let {
            val allTypes = OberonUtil.findAllTypes(it)
            println("OberonChooseTypeByNameContributor::processNames, allTypes #: ${allTypes.size}")

            ContainerUtil.process(allTypes.map(OberonUtil::typeName), processor::process)
        }
    }

    override fun processElementsWithName(
        name: String,
        processor: Processor<in NavigationItem>,
        parameters: FindSymbolParameters
    ) {
        val project = parameters.project
        val searchInLibraries = parameters.isSearchInLibraries
        val types = OberonUtil.findTypesByName(project, name)
        println("OberonChooseTypeByNameContributor::processNames, name: $name, types: $types")
        ContainerUtil.process(types.toList(), processor::process)
    }
}