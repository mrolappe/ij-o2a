package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import de.rholambdapi.o2a.intellij.lang.oberon2.psi.OberonTypes.*

class OberonBraceMatcher : PairedBraceMatcher {

    override fun getPairs(): Array<BracePair> = BRACE_PAIRS

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean = true

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int) = openingBraceOffset

}

private val BRACE_PAIRS = arrayOf(
    BracePair(LPAREN, RPAREN, false),
    BracePair(LBRACK, RBRACK, false),
    BracePair(LBRACE, RBRACE, false)
)