package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.codeInsight.editorActions.SimpleTokenSetQuoteHandler
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonTypes.*

class OberonQuoteHandler : SimpleTokenSetQuoteHandler(STR_LIT)
