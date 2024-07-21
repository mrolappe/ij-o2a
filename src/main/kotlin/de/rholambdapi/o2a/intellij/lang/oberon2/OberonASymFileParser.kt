package de.rholambdapi.o2a.intellij.lang.oberon2

import java.io.DataInput
import java.io.InputStream

class OberonASymFileParser(val inputStream: InputStream) {

}

const val symFileVersion = 8

fun matchSymFileTagV8(dataInput: DataInput): Boolean {

    val symFileTag = dataInput.readInt()

    val expectedTag = 'S'.code.shl(24) or 'Y'.code.shl(16) or 'M'.code.shl(8) or symFileVersion
    return symFileTag == expectedTag
//    symFileTag.shouldBeEqual(expectedTag)
}
