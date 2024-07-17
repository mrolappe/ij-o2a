package de.rholambdapi.o2a.intellij.lang.oberon2.psi

import com.intellij.psi.tree.IElementType
import de.rholambdapi.o2a.intellij.lang.oberon2.OberonLanguage

class OberonTokenType(debugName: String) : IElementType(debugName, OberonLanguage.INSTANCE) {
    override fun toString(): String = javaClass.simpleName + "." + super.toString()
}