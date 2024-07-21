package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.openapi.fileTypes.BinaryFileDecompiler
import com.intellij.openapi.vfs.VirtualFile

class OberonASymFileDecompiler : BinaryFileDecompiler {
    override fun decompile(file: VirtualFile): CharSequence {
        return "TODO decompile file $file"
    }
}
