package me.mrolappe.intellij.lang.oberon

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler
import me.mrolappe.intellij.lang.oberon.psi.OberonTypes.*

class OberonQuoteHandler : SimpleTokenSetQuoteHandler(STR_LIT)
