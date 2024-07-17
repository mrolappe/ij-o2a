package me.mrolappe.intellij.lang.oberon

import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType
import me.mrolappe.intellij.lang.oberon.psi.OberonTypes.*

class OberonBraceMatcher : PairedBraceMatcher {

    override fun getPairs(): Array<BracePair> = Companion.BRACE_PAIRS

    override fun isPairedBracesAllowedBeforeType(lbraceType: IElementType, contextType: IElementType?): Boolean = true

    override fun getCodeConstructStart(file: PsiFile?, openingBraceOffset: Int) = openingBraceOffset

    companion object {
        private val BRACE_PAIRS = arrayOf(
            BracePair(LPAREN, RPAREN, false),
            BracePair(LBRACK, RBRACK, false),
            BracePair(LBRACE, RBRACE, false)
        )
    }
}