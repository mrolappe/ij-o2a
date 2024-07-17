package me.mrolappe.intellij.lang.oberon

import com.intellij.lang.Language

class OberonLanguage : Language("Oberon") {
    companion object {
        val INSTANCE: OberonLanguage = OberonLanguage()
    }
}