package me.mrolappe.intellij.lang.oberon.parser

import com.intellij.lang.PsiBuilder
import com.intellij.lang.parser.GeneratedParserUtilBase

object OberonParserUtilKt : GeneratedParserUtilBase() {
    @JvmStatic
    fun amiga_oberon_mode(b: PsiBuilder?, l: Int): Boolean {
//        println("amiga_oberon_mode")
        // TODO
        return true
    }

    @JvmStatic
    fun pling_plong(b: PsiBuilder, l: Int, zeugs: String): Boolean {
        println("pling plong kt, zeugs: $zeugs, token text: ${b.tokenText}, token type: ${b.tokenType}, latest done marker: ${b.latestDoneMarker}")
        return true
    }
}