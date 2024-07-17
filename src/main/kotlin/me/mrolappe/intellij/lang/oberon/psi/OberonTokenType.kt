package me.mrolappe.intellij.lang.oberon.psi

import com.intellij.psi.tree.IElementType
import me.mrolappe.intellij.lang.oberon.OberonLanguage

class OberonTokenType(debugName: String) : IElementType(debugName, OberonLanguage.INSTANCE) {
    override fun toString(): String = javaClass.simpleName + "." + super.toString()
}