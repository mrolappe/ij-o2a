package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.icons.AllIcons
import com.intellij.navigation.ItemPresentation
import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.AdditionalLibraryRootsProvider
import com.intellij.openapi.roots.SyntheticLibrary
import com.intellij.openapi.roots.SyntheticLibrary.ExcludeFileCondition
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.pom.NavigatableWithText
import java.nio.file.Path
import java.nio.file.Paths
import java.util.*
import javax.swing.Icon
import kotlin.io.path.absolutePathString
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

private val log = logger<OberonALibraryRootProvider>()
private val libraryFileRegex = Regex(".*\\.(mod|o(bjs)?|sym)", RegexOption.IGNORE_CASE)

class OberonALibraryRootProvider : AdditionalLibraryRootsProvider() {
    override fun getAdditionalProjectLibraries(project: Project): Collection<SyntheticLibrary> {
        log.debug("OberonALibraryRootProvider, getAdditionalProjectLibraries; project: $project")

        // TODO config
        val basePath = Paths.get("/Users/mrolappe/studio/oberon-a-fs-uae-env/Oberon-A")

        try {
//            val sourceRoots = listOf("source/Library", "source/amiga")
//                .mapNotNull { LocalFileSystem.getInstance().findFileByNioFile(basePath.resolve(it)) }
//
//            val binaryRoots = listOf("OLIB")
//                .mapNotNull { LocalFileSystem.getInstance().findFileByNioFile(basePath.resolve(it)) }
//
//            val exclCond = ExcludeFileCondition { isDir, filename, isRoot, isStrictRootChild, hasParentNotGrandparent ->
//                !isDir && !filename.matches(libraryFileRegex)
//            }
//
//            val library = SyntheticLibrary.newImmutableLibrary(
//                "Oberon-A Library",
//                sourceRoots,
//                binaryRoots,
//                emptySet(),
//                exclCond
//            );

            val library = OberonALibrary(basePath)
            log.debug("library: $library")
            return listOf(library)
        } catch (e: Exception) {
            log.error(e)
            return Collections.emptyList()
        }
    }
}

private val libraryExcludeFileCondition = ExcludeFileCondition { isDir, filename, _, _, _ ->
    !isDir && !filename.matches(libraryFileRegex)
}

data class OberonALibrary(val basePath: Path) :
    SyntheticLibrary("Oberon-A", libraryExcludeFileCondition), ItemPresentation, NavigatableWithText {

    private val sourceRoots: List<VirtualFile>
    private val binaryRoots: List<VirtualFile>

    init {
        require(basePath.exists())
        require(basePath.isDirectory())

        sourceRoots = listOf("source/Library", "source/amiga")
            .mapNotNull { LocalFileSystem.getInstance().findFileByNioFile(basePath.resolve(it)) }

        binaryRoots = listOf("OLIB")
            .mapNotNull { LocalFileSystem.getInstance().findFileByNioFile(basePath.resolve(it)) }
    }

    override fun getSourceRoots(): Collection<VirtualFile> {
        return sourceRoots
    }

    override fun getPresentableText(): String {
        return "Oberon-A Library presentable text"
    }

    override fun getBinaryRoots(): Collection<VirtualFile> {
        return binaryRoots
    }

    override fun getLocationString(): String {
        return basePath.absolutePathString()
    }

    override fun getIcon(unused: Boolean): Icon {
        return AllIcons.Nodes.Module
    }

    override fun getNavigateActionText(focusEditor: Boolean): String {
        log.debug("OberonALibraryRootProvider, getNavigateActionText, focusEditor=$focusEditor")
        return "Oberon-A library navigate action text"
    }
}


