package de.rholambdapi.o2a.intellij.lang.oberon2

import io.kotest.matchers.equals.shouldBeEqual
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import java.io.DataInput
import java.io.DataInputStream
import java.io.InputStream

class OberonASymFileParserTests {
    @Test
    fun shouldDetectSymFileTag() {
        val inputStream = OberonASymFileParserTests::class.java.getResourceAsStream("/MathL.sym")!!
        val dataInput: DataInput = DataInputStream(inputStream)

        matchSymFileTagV8(dataInput).shouldBe(true)
    }

}