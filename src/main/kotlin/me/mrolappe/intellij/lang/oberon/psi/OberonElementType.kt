package me.mrolappe.intellij.lang.oberon.psi

import com.intellij.psi.tree.IElementType
import me.mrolappe.intellij.lang.oberon.OberonLanguage

class OberonElementType(debugName: String) : IElementType(debugName, OberonLanguage.INSTANCE)