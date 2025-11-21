package de.rholambdapi.o2a.intellij.lang.oberon2

import com.github.michaelbull.result.*
import java.io.*
import java.nio.ByteBuffer
import java.nio.ByteOrder

class OberonASymFileParser(val inputStream: InputStream) {

}

const val symFileVersion = 8

sealed class IoProblem {
    data object EOF : IoProblem()
    class Other(val throwable: Throwable) : IoProblem()
}

fun matchSymFileTagV8(dataInput: DataInput): Result<Boolean, IOException> {

    try {
        val symFileTag = dataInput.readInt()

        val expectedTag = 'S'.code.shl(24) or 'Y'.code.shl(16) or 'M'.code.shl(8) or symFileVersion
        return Ok(symFileTag == expectedTag)
    } catch (e: IOException) {
        return Err(e)
    }
}

fun DataInput.readModuleKey(): Result<Int, IOException> {
    try {
        val bytes = ByteArray(4)
        readFully(bytes)
        val moduleKey = ByteBuffer.wrap(bytes)
            .order(ByteOrder.LITTLE_ENDIAN)
            .getInt()
        return Ok(moduleKey)
    } catch (e: IOException) {
        return Err(e)
    }
}

sealed class ReadElementError {
    data class IoProblem(val e: IOException) : ReadElementError()
    data class IllegalElementClass(val elementClass: Int) : ReadElementError()
}

fun DataInput.readByteOrGetIoException(): Result<Int, IOException> {
    return try {
        Ok(readByte().toInt().and(0xFF))
    } catch (e: IOException) {
        Err(e)
    }
}

@OptIn(ExperimentalStdlibApi::class)
fun DataInput.readElementClass(): Result<ElementClass, ReadElementError> {
    return readByteOrGetIoException()
        .mapError { ioe -> ReadElementError.IoProblem(ioe) }
        .flatMap { elementClassData ->
            ElementClass.entries.find { it.value == elementClassData }
                .toResultOr { ReadElementError.IllegalElementClass(elementClassData) }
        }
}

enum class ElementClass(val value: Int) {
    UNDEFINED(0), CONSTANT(1), TYPE_E(2), TYPE(3), VARIABLE(4), XPROC(5),
    LIBCALL(6), M2PROC(7), CPROC(8), APROC(9), POINTER(10), PROC_TYPE(11), ARRAY(12),
    DYN_ARRAY(13), RECORD(14), PARAMETER_LIST(15), VALUE_PARAMETER(16), VAR_PARAMETER(17),
    VAR_ARG(18), FIELD_LIST(19), FIELD(20), HPTR(21), HPROC(22), TPROC_E(23),
    TPROC(24), FIXUP(25), MODULE(26), EXT_LIB(27)
}

fun DataInput.readId(): Result<String, IOException> {
    val buffer = StringBuffer()

    try {
        var readChar = readByte().toInt().and(0xff).toChar()
        while (readChar.code != 0) {
            buffer.append(readChar)
            val readByte = readByte()
            val toInt = readByte.toInt()
            readChar = toInt.toChar()
        }

        return Ok(buffer.toString())
    } catch (e: IOException) {
        return Err(e)
    }
}

fun DataInput.readCompactInt(): Int {
    var n = 0
    var shift = 0

    var x = readByte().toInt().and(0xFF)

    while (x >= 128) {
        n += (x - 128).shl(shift).and(0xFF)
        shift += 7
        x = readByte().toInt().and(0xFF)
    }

    return n + (x % 64 - (x / 64) * 64).shl(shift)
}

sealed interface StructureForm {
    val id: Int
}

enum class PredefinedStructureForm(override val id: Int) : StructureForm {
    UNDEFINED(0), BYTE(1), BOOL(2), CHAR(3), SHORT_INT(4), INT(5), LONG_INT(6), REAL(7), LONG_REAL(8),
    BYTE_SET(9), WORD_SET(10), SET(11), STRING(12), NIL_TYPE(13), NO_TYPE(14), POINTER_TYPE(15),
    ADDRESS_TYPE(16), BPTR_TYPE(17), WORD(18), LONG_WORD(19), TAG_TYPE(20), POINTER(21),
    PROC_TYPE(24), ARRAY(25), DYN_ARRAY(26), RECORD(27)
}

data class UserDefinedStructureForm(override val id: Int) : StructureForm

sealed interface ParseSymFileError {
    data object FailedToMatchTag : ParseSymFileError
    data class IoError(val e: IOException) : ParseSymFileError
    data class FailedToParseElement(val error: ParseElementError) : ParseSymFileError
}

fun parseSymFile(input: InputStream): Result<OberonASymFile, ParseSymFileError> {
    val pushback = PushbackInputStream(input)
    val dataInput: DataInput = DataInputStream(pushback)

    val matchTagResult = matchSymFileTagV8(dataInput)

    if (matchTagResult.isErr) {
        return Err(ParseSymFileError.IoError(matchTagResult.getError()!!))
    }

    var done = false

    var moduleImports = mutableListOf<ModuleImportElement>()
    var moduleVariables = mutableListOf<ModuleVariableElement>()
    var parameterLists = mutableListOf<ParameterListElement>()
    var valueParameters = mutableListOf<ValueParameterElement>()
    var libCalls = mutableListOf<LibCallElement>()
    var constants = mutableListOf<ConstantElement>()

    while (!done) {

        val result = dataInput.parseElement()
            .andThen { element ->
                when (element) {
                    is ModuleImportElement -> moduleImports.add(element)
                    is ModuleVariableElement -> moduleVariables.add(element)
                    is ParameterListElement -> parameterLists.add(element)
                    is ValueParameterElement -> valueParameters.add(element)
                    is LibCallElement -> libCalls.add(element)
                    is ConstantElement -> constants.add(element)
                    is PointerElement -> {
                        println("TODO pointer element $element")
                    }

                    is TypeElement -> {
                        println("TODO type element $element")
                    }

                    is FieldListElement -> {
                        println("TODO field list element $element")
                    }

                    is FieldElement -> {
                        println("TODO field element $element")
                    }

                    is RecordElement -> {
                        println("TODO record element $element")
                    }

                    is VarParameterElement -> {
                        println("TODO var parameter element $element")
                    }

                    is XProcElement -> {
                        println("TODO xproc element $element")
                    }

                    is ArrayElement -> {
                        println("TODO array element $element")
                    }

                    is ProcTypeElement -> {
                        println("TODO proc type element $element")
                    }

                    is VarArgElement -> {
                        println("TODO var arg element: $element")
                    }

                    is DynArrayElement -> {
                        println("TODO dynarray element $element")
                    }

                    is FixupElement -> {
                        println("TODO fixup element $element")
                    }
                    else -> throw IllegalArgumentException("element: $element")
                }

                Ok(null)
            }

        if (result.isErr) {
            return Err(ParseSymFileError.FailedToParseElement(result.getError()!!))
        }

        done = pushback.read().also { pushback.unread(it) }.let { nextByte ->
            nextByte == -1
        }
    }

    return Ok(OberonASymFile(moduleImports, constants, parameterLists, libCalls, moduleVariables))
}

sealed interface ParseElementError {
    data class FailedToReadElementClass(val error: ReadElementError) : ParseElementError
    data class FailedToReadModuleKey(val e: IOException) : ParseElementError
    data class FailedToReadId(val e: IOException) : ParseElementError
    data class IllegalStructureFormId(val structureFormId: Int, val context: String? = null) : ParseElementError
    data class FailedToParseConstantElement(val e: IOException) : ParseElementError
    data class FailedToParsePointerElement(val e: IOException) : ParseElementError
}

@OptIn(ExperimentalStdlibApi::class)
fun DataInput.parseElement(): Result<Element, ParseElementError> {
    val readElementClassResult = readElementClass()

    if (readElementClassResult.isErr) {
        return Err(ParseElementError.FailedToReadElementClass(readElementClassResult.getError()!!))
    }

    println("readElementClassResult: $readElementClassResult")
    when (val elementClass = readElementClassResult.get()) {
        ElementClass.MODULE -> {
            return binding<Element, ParseElementError> {
                val moduleKeyResult = readModuleKey().mapError { ParseElementError.FailedToReadModuleKey(it) }.bind()
                val id = readId().mapError { ParseElementError.FailedToReadModuleKey(it) }.bind()

                ModuleImportElement(id, moduleKeyResult)
            }
        }

        ElementClass.CONSTANT -> {
            return binding {
                val structureForm = readCompactInt().let { structureFormId ->
                    PredefinedStructureForm.entries.find { it.id == structureFormId }
                        ?: UserDefinedStructureForm(structureFormId)
                }
                val a0 = readCompactInt()
                val a1 = readCompactInt()
                val id = readId()
                    .mapError { ParseElementError.FailedToParseConstantElement(it) }.bind()
                ConstantElement(id, structureForm, a0, a1)
            }
        }

        ElementClass.LIBCALL -> {
            return binding {
                val structureForm = readCompactInt().let { structureFormId ->
                    PredefinedStructureForm.entries.find { it.id == structureFormId }
                        ?: UserDefinedStructureForm(structureFormId)
                }
                val a0 = readCompactInt()
                val a1 = readCompactInt()
                val id = readId()
                    .mapError { ParseElementError.FailedToReadId(it) }
                    .bind()

                LibCallElement(id, structureForm, LibCallElement.Offset(a0), LibCallElement.A1(a1))
            }
        }

        ElementClass.VARIABLE -> {
            return binding {
                val structureForm = readCompactInt().let { structureFormId ->
                    PredefinedStructureForm.entries.find { it.id == structureFormId }
                        ?: UserDefinedStructureForm(structureFormId)
                }

                val a0 = readCompactInt()
                val visible = readByte()
                val id = readId()
                    .mapError { ParseElementError.FailedToReadId(it) }
                    .bind()

                ModuleVariableElement(
                    id,
                    structureForm,
                    ModuleVariableElement.A0(a0),
                    ModuleVariableElement.Visibility(visible)
                )
            }
        }

        ElementClass.PARAMETER_LIST -> {
            return Ok(ParameterListElement())
        }

        ElementClass.VALUE_PARAMETER -> {
            return binding {
                val structureForm = readCompactInt().let { structureFormId ->
                    PredefinedStructureForm.entries.find { it.id == structureFormId }
                        ?: UserDefinedStructureForm(structureFormId)
                }
                val a0 = readCompactInt()
                val id = readId()
                    .mapError { ParseElementError.FailedToReadId(it) }
                    .bind()

                ValueParameterElement(id, structureForm, a0)
            }
        }

        ElementClass.POINTER -> {
            return binding {
                val baseType = readCompactInt().let { structureFormId ->
                    PredefinedStructureForm.entries.find { it.id == structureFormId }
                        ?: UserDefinedStructureForm(structureFormId)
                }

                val module = readCompactInt()
                val sysFlag = readCompactInt()
                val address = readCompactInt()
                // TODO value types definieren und daten im element speichern
                PointerElement(baseType)
            }
        }

        ElementClass.TYPE, ElementClass.TYPE_E -> {
            return binding {
                val type = readCompactInt().let { structureFormId ->
                    PredefinedStructureForm.entries.find { it.id == structureFormId }
                        ?: UserDefinedStructureForm(structureFormId)
                }
                val m = readCompactInt()
                val id = readId()
                    .mapError { ParseElementError.FailedToReadId(it) }
                    .bind()
                val visibility =
                    if (elementClass == ElementClass.TYPE_E) Visibility.EXPORTED else Visibility.NOT_EXPORTED
                TypeElement(id, type, visibility)
            }
        }

        ElementClass.FIELD_LIST -> return binding {
            FieldListElement(emptyList())
        }

        ElementClass.FIELD -> return binding {
            val type = readCompactInt().let { structureFormId ->
                PredefinedStructureForm.entries.find { it.id == structureFormId }
                    ?: UserDefinedStructureForm(structureFormId)
            }
            val a0 = readCompactInt()
            val visible = readByte()
            val id = readId()
                .mapError { ParseElementError.FailedToReadId(it) }
                .bind()

            FieldElement(id, type, visible, a0)
        }

        ElementClass.RECORD -> return binding {
            val baseType = readCompactInt().let { structureFormId ->
                PredefinedStructureForm.entries.find { it.id == structureFormId }
                    ?: UserDefinedStructureForm(structureFormId)
            }
            val module = readCompactInt()
            val typeSize = readCompactInt()
            val sysFlag = readCompactInt()
            val descriptorAddress = readCompactInt()
            RecordElement(baseType)
        }

        ElementClass.VAR_PARAMETER -> return binding {
            val type = readCompactInt().let { structureFormId ->
                PredefinedStructureForm.entries.find { it.id == structureFormId }
                    ?: UserDefinedStructureForm(structureFormId)
            }

            val a0 = readCompactInt()
            val id = readId()
                .mapError { ParseElementError.FailedToReadId(it) }
                .bind()
            VarParameterElement(id, type, a0)
        }

        ElementClass.XPROC -> return binding {
            val type = readCompactInt().let { structureFormId ->
                PredefinedStructureForm.entries.find { it.id == structureFormId }
                    ?: UserDefinedStructureForm(structureFormId)
            }

            val id = readId()
                .mapError { ParseElementError.FailedToReadId(it) }
                .bind()

            XProcElement(id, type)
        }

        ElementClass.ARRAY -> return binding {
            val baseType = readCompactInt().let { structureFormId ->
                PredefinedStructureForm.entries.find { it.id == structureFormId }
                    ?: UserDefinedStructureForm(structureFormId)
            }
            val module = readCompactInt()
            val size = readCompactInt()
            val address = readCompactInt()
            val n = readCompactInt()

            ArrayElement(baseType, module, size, address, n)
        }

        ElementClass.PROC_TYPE -> return binding {
            val baseType = readCompactInt().let { structureFormId ->
                PredefinedStructureForm.entries.find { it.id == structureFormId }
                    ?: UserDefinedStructureForm(structureFormId)
            }
            val module = readCompactInt()
            ProcTypeElement(baseType, module)
        }

        ElementClass.VAR_ARG -> return binding {
            val type = readCompactInt().let { structureFormId ->
                PredefinedStructureForm.entries.find { it.id == structureFormId }
                    ?: UserDefinedStructureForm(structureFormId)
            }

            val a0 = readCompactInt()
            val id = readId()
                .mapError { ParseElementError.FailedToReadId(it) }
                .bind()
            VarArgElement(id, type, a0)
        }

        ElementClass.DYN_ARRAY -> return binding {
            val baseType = readCompactInt().let { structureFormId ->
                PredefinedStructureForm.entries.find { it.id == structureFormId }
                    ?: UserDefinedStructureForm(structureFormId)
            }
            val module = readCompactInt()
            val size = readCompactInt()
            val address = readCompactInt()
            DynArrayElement(baseType, module, size, address)
        }

        ElementClass.FIXUP -> return binding {
            val type1 = readCompactInt().let { structureFormId ->
                PredefinedStructureForm.entries.find { it.id == structureFormId }
                    ?: UserDefinedStructureForm(structureFormId)
            }
            val type2 = readCompactInt().let { structureFormId ->
                PredefinedStructureForm.entries.find { it.id == structureFormId }
                    ?: UserDefinedStructureForm(structureFormId)
            }
            FixupElement(type1, type2)
        }
        else -> throw IllegalArgumentException("elementClass=$readElementClassResult")
    }
}

sealed interface Element

class ModuleImportElement(val moduleName: String, val moduleKey: Int) : Element

class ConstantElement(
    val id: String,
    val structureForm: StructureForm,
    val a0: Int,
    val a1: Int
) : Element

class ModuleVariableElement(
    val id: String,
    val structureForm: StructureForm,
    val a0: A0,
    val visibility: Visibility
) : Element {
    @JvmInline
    value class A0(val intValue: Int)

    @JvmInline
    value class Visibility(val byteValue: Byte)
}

class LibCallElement(
    val id: String,
    val structureForm: StructureForm,
    val offset: Offset,
    val a1: A1
) : Element {
    @JvmInline
    value class Offset(val intValue: Int)

    @JvmInline
    value class A1(val intValue: Int)
}

class ParameterListElement() : Element

class ValueParameterElement(val id: String, val structureForm: StructureForm, val a0: Int) : Element

data class PointerElement(val baseType: StructureForm) : Element

enum class Visibility {
    EXPORTED, NOT_EXPORTED, READ_ONLY
}

data class TypeElement(val id: String, val type: StructureForm, val visibility: Visibility) : Element

data class FieldListElement(val list: List<Any>) : Element

data class FieldElement(val id: String, val type: StructureForm, val visible: Byte, val a0: Int) : Element

data class RecordElement(val id: StructureForm) : Element

data class VarParameterElement(val id: String, val type: StructureForm, val a0: Int) : Element

data class XProcElement(val id: String, val type: StructureForm) : Element

data class ArrayElement(val baseType: StructureForm, val module: Int, val size: Int, val address: Int, val n: Int) : Element

data class ProcTypeElement(val baseType: StructureForm, val module: Int) : Element

data class VarArgElement(val id: String, val type: StructureForm, val a0: Int) : Element

data class DynArrayElement(val type: StructureForm, val module: Int, val size: Int, val address: Int) : Element

data class FixupElement(val type1: StructureForm, val type2: StructureForm) : Element