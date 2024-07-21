package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.AdditionalLibraryRootsProvider
import com.intellij.openapi.roots.SyntheticLibrary
import com.intellij.openapi.roots.SyntheticLibrary.ExcludeFileCondition
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.pom.NavigatableWithText
import java.nio.file.Paths
import java.util.*
import java.util.function.BooleanSupplier
import javax.swing.Icon

private val log = logger<OberonALibraryRootProvider>()
private val libraryFileRegex = Regex(".*\\.(mode|o(bjs)?|sym)", RegexOption.IGNORE_CASE)

class OberonALibraryRootProvider : AdditionalLibraryRootsProvider() {
    override fun getAdditionalProjectLibraries(project: Project): MutableCollection<SyntheticLibrary> {
        log.debug("OberonALibraryRootProvider, getAdditionalProjectLibraries; project: $project")

        val basePath = Paths.get("/Users/mrolappe/studio/oberon-a-fs-uae-env/Oberon-A")

        try {
            val sourceRoots = listOf("source/Library", "source/amiga")
                .mapNotNull { LocalFileSystem.getInstance().findFileByNioFile(basePath.resolve(it)) }

            val binaryRoots = listOf("OLIB")
                .mapNotNull { LocalFileSystem.getInstance().findFileByNioFile(basePath.resolve(it)) }

            val exclCond = ExcludeFileCondition { isDir, filename, isRoot, isStrictRootChild, hasParentNotGrandparent ->
                !isDir && !filename.matches(libraryFileRegex)
            }

            val library = SyntheticLibrary.newImmutableLibrary("Oberon-A Library", sourceRoots, binaryRoots, emptySet(), exclCond);

            log.debug("library: $library")
            return mutableListOf(library)
        } catch (e: Exception) {
            log.error(e)
            return Collections.emptyList()
        }
    }
}

data class OberonALibrary(val dir: VirtualFile) : SyntheticLibrary("Oberon-A", MyExcludeFileCondition),
    ItemPresentation, NavigatableWithText {
    override fun getSourceRoots(): MutableCollection<VirtualFile> {
        return mutableListOf(dir)
    }

    override fun getPresentableText(): String {
        return "Oberon-A Library"
    }

    override fun getLocationString(): String {
        return "Oberon-A Library Location"
    }

    override fun getIcon(unused: Boolean): Icon? {
        return null
    }

    override fun getNavigateActionText(focusEditor: Boolean): String? {
        log.debug("OberonALibraryRootProvider, getNavigateActionText, focusEditor=$focusEditor")
        return "Oberon-A library navigate action text"
    }

}

object MyExcludeFileCondition : SyntheticLibrary.ExcludeFileCondition {
    override fun shouldExclude(
        isDir: Boolean,
        filename: String,
        isRoot: BooleanSupplier,
        isStrictRootChild: BooleanSupplier,
        hasParentNotGrandparent: BooleanSupplier
    ): Boolean {
        return !filename.endsWith(".mod")
    }
}

