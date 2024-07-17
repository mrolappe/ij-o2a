package de.rholambdapi.o2a.intellij.lang.oberon2

import com.intellij.lang.Language

class OberonLanguage : Language("Oberon") {
    companion object {
        val INSTANCE: OberonLanguage = OberonLanguage()
    }
}