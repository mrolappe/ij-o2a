package de.rholambdapi.o2a.intellij.lang.oberon2

import com.github.michaelbull.result.Result
import com.github.michaelbull.result.annotation.UnsafeResultErrorAccess
import com.github.michaelbull.result.annotation.UnsafeResultValueAccess
import io.kotest.assertions.assertSoftly
import io.kotest.assertions.withClue
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeInstanceOf
import org.junit.jupiter.api.Test
import java.io.DataInput
import java.io.DataInputStream

@OptIn(UnsafeResultErrorAccess::class, UnsafeResultValueAccess::class)  // TODO adjust access
class OberonASymFileParserTests {
    @Test
    fun shouldParseIntuitionSymFile() {
        val inputStream = OberonASymFileParserTests::class.java.getResourceAsStream("/Intuition.sym")!!
        val parseResult = parseSymFile(inputStream)

        withClue({ "blablablabla -> ${parseResult.error}"  }) {
            parseResult.isOk shouldBe true
        }
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun shouldParseMathLSymFile() {
        val inputStream = OberonASymFileParserTests::class.java.getResourceAsStream("/MathL.sym")!!
        val dataInput: DataInput = DataInputStream(inputStream)

        matchSymFileTagV8(dataInput).shouldBeInstanceOf<Result<Boolean, Exception>>()

        val element = dataInput.parseElement()
            .shouldBeInstanceOf<Result<ModuleImportElement, ParseElementError>>()

        assertSoftly(element.value) {
            moduleKey shouldBe 0x675d571d
            moduleName shouldBe "MathL"
        }
    }

    @Test
    fun shouldParsePotgoSymFile() {
        val inputStream = OberonASymFileParserTests::class.java.getResourceAsStream("/Potgo.sym")!!
        val parseResult = parseSymFile(inputStream)

        parseResult.isOk shouldBe true

        assertSoftly(parseResult.value.moduleImports.first()) {
            moduleKey shouldBe 2036356893
            moduleName shouldBe "Potgo"
        }

        val dataInputStream = DataInputStream(inputStream)

        // IMPORT e := Exec, s := Sets;
        assertSoftly(parseResult.value.moduleImports[1]) {
            moduleName shouldBe "Exec"
            moduleKey shouldBe 0x7860571d
        }

//        run {
//            val element = dataInputStream.parseElement()
//                .shouldNotBeNull()
//                .shouldBeInstanceOf<ModuleImportElement>()
//
//            assertSoftly(element) {
//                moduleName shouldBe "Exec"
//                moduleKey shouldBe 0x7860571d
//            }
//        }

        assertSoftly(parseResult.value.moduleImports[2]) {
            moduleName shouldBe "Sets"
            moduleKey shouldBe 2019579677
        }
//        run {
//            val element = dataInputStream.parseElement()
//                .shouldNotBeNull()
//                .shouldBeInstanceOf<ModuleImportElement>()
//
//            assertSoftly(element) {
//                moduleName shouldBe "Sets"
//                moduleKey shouldBe 2019579677
//            }
////            val elementClass = dataInputStream.readElementClass()
////            elementClass shouldBe ElementClass.MODULE
////            val moduleKey = dataInputStream.readModuleKey()
////            moduleKey shouldBe 2019579677
////            dataInputStream.readId() shouldBe "Sets"
//        }

        assertSoftly(parseResult.value.moduleVariables[0]) {
            structureForm shouldBe PredefinedStructureForm.ADDRESS_TYPE
            a0.intValue shouldBe 0
            visibility.byteValue shouldBe -1
            id shouldBe "base"
        }
//        run {
//            val element = dataInputStream.parseElement()
//                .shouldNotBeNull()
//                .shouldBeInstanceOf<ModuleVariableElement>()
//
//            assertSoftly(element) {
//                structureForm shouldBe StructureForm.ADDRESS_TYPE
//                a0.intValue shouldBe 0
//                visibility.byteValue shouldBe -1
//                id shouldBe "base"
//            }
//        }

        assertSoftly(parseResult.value.libCalls[0]) {
            id shouldBe "AllocPotBits"
            structureForm shouldBe PredefinedStructureForm.WORD_SET
            offset.intValue shouldBe -6
            a1.intValue shouldBe 0
            // TODO param list; siehe folgdener kommentar
        }
//        run {
//            val element2 = dataInputStream.parseElement()
//                .shouldNotBeNull()
//                .shouldBeInstanceOf<ParameterListElement>()
//
//            val element1 = dataInputStream.parseElement()
//                .shouldNotBeNull()
//                .shouldBeInstanceOf<ValueParameterElement>()
//
//            assertSoftly(element1) {
//                id shouldBe "bits"
//                structureForm shouldBe StructureForm.WORD_SET
//                a0 shouldBe 0   // reg spec?
//            }
//
//            val element = dataInputStream.parseElement()
//                .shouldNotBeNull()
//                .shouldBeInstanceOf<LibCallElement>()
//
//            assertSoftly(element) {
//                id shouldBe "AllocPotBits"
//                structureForm shouldBe StructureForm.WORD_SET
//                offset.intValue shouldBe -6
//                a1.intValue shouldBe 0
//            }
//        }

        assertSoftly(parseResult.value.libCalls[1]) {
            id shouldBe "FreePotBits"
            structureForm shouldBe PredefinedStructureForm.NO_TYPE
            offset.intValue shouldBe -12
            a1.intValue shouldBe 0
            // TODO param list; siehe folgender kommentar
        }

//        run {
//            val parameterListElement = dataInputStream.parseElement()
//                .shouldNotBeNull()
//                .shouldBeInstanceOf<ParameterListElement>()
//
//            val parameterElement = dataInputStream.parseElement()
//                .shouldNotBeNull()
//                .shouldBeInstanceOf<ValueParameterElement>()
//
//            assertSoftly(parameterElement) {
//                id shouldBe "bits"
//                structureForm shouldBe StructureForm.WORD_SET
//                a0 shouldBe 0
//            }
//
//            val libCallElement = dataInputStream.parseElement()
//                .shouldNotBeNull()
//                .shouldBeInstanceOf<LibCallElement>()
//
//            assertSoftly(libCallElement) {
//                id shouldBe "FreePotBits"
//                structureForm shouldBe StructureForm.NO_TYPE
//                offset.intValue shouldBe -12
//                a1.intValue shouldBe 0
//            }
//        }

        assertSoftly(parseResult.value.libCalls[2]) {
            id shouldBe "WritePotgo"
            structureForm shouldBe PredefinedStructureForm.NO_TYPE
            offset.intValue shouldBe -18
            a1.intValue shouldBe 0
            // TODO parameter list; siehe folgender kommentar
        }

        assertSoftly(parseResult.value.constants[0]) {
            id shouldBe "potgoName"
            structureForm shouldBe PredefinedStructureForm.STRING
            a0 shouldBe 6
            a1 shouldBe 15   // export marker?
        }
        run {
//            val parameterListElement = dataInputStream.parseElement()
//                .shouldNotBeNull()
//                .shouldBeInstanceOf<ParameterListElement>()
//
//            val valueParameterElement = dataInputStream.parseElement()
//                .shouldNotBeNull()
//                .shouldBeInstanceOf<ValueParameterElement>()
//
//            assertSoftly(valueParameterElement) {
//                id shouldBe "word"
//                structureForm shouldBe StructureForm.WORD_SET
//                a0 shouldBe 0
//            }
//
//            val element1 = dataInputStream.parseElement()
//                .shouldNotBeNull()
//                .shouldBeInstanceOf<ValueParameterElement>()
//
//            assertSoftly(element1) {
//                id shouldBe "mask"
//                structureForm shouldBe StructureForm.WORD_SET
//                a0 shouldBe 1
//            }
//
//            val element3 = dataInputStream.parseElement()
//                .shouldNotBeNull()
//                .shouldBeInstanceOf<LibCallElement>()
//
//            assertSoftly(element3) {
//                id shouldBe "WritePotgo"
//                structureForm shouldBe StructureForm.NO_TYPE
//                offset.intValue shouldBe -18
//                a1.intValue shouldBe 0
//            }
//
//            val element4 = dataInputStream.parseElement()
//                .shouldNotBeNull()
//                .shouldBeInstanceOf<ConstantElement>()
//
//            assertSoftly(element4) {
//                id shouldBe "potgoName"
//                structureForm shouldBe StructureForm.STRING
//                a0 shouldBe 6
//                a1 shouldBe 15   // export marker?
//            }
//
//            dataInputStream.parseElement()
//                .shouldNotBeNull()
//                .shouldBeInstanceOf<ParameterListElement>()
        }
    }

    @Test
    fun shouldParseBattClockSymFile() {
        val inputStream = OberonASymFileParserTests::class.java.getResourceAsStream("/BattClock.sym")!!
        val parseResult = parseSymFile(inputStream).shouldNotBeNull()

        parseResult.isOk shouldBe true

        assertSoftly(parseResult.value.moduleImports.first()) {
            moduleKey shouldBe 0x7860571d
            moduleName shouldBe "BattClock"
        }

        assertSoftly(parseResult.value.moduleImports[1]) {
            moduleKey shouldBe 0x7860571d
            moduleName shouldBe "Exec"
        }

        val dataInputStream = DataInputStream(inputStream)

//        run {
//            val parseResult = dataInputStream.parseElement()
//                .shouldNotBeNull()
//                .shouldBeInstanceOf<Result<ModuleImportElement, ParseElementError>>()
//
//            parseResult.isOk shouldBe true
//
//            assertSoftly(parseResult.value) {
//                moduleKey shouldBe 0x7860571d
//                moduleName shouldBe "Exec"
//            }
//        }

        // CONST battClockName * = "battclock.resource";
        assertSoftly(parseResult.value.constants[0]) {
            structureForm shouldBe PredefinedStructureForm.STRING
            a0 shouldBe 10
            a1 shouldBe 19
            id shouldBe "battClockName"
        }

//        run {
//            val elementClass = dataInputStream.readElementClass()
//            elementClass shouldBe ElementClass.PARAMETER_LIST
//        }

        assertSoftly(parseResult.value.libCalls[0]) {
            structureForm shouldBe PredefinedStructureForm.NO_TYPE
            offset.intValue shouldBe -6
            a1.intValue shouldBe 0
            id shouldBe "ResetBattClock"
        }

//        run {
//            val element = dataInputStream.parseElement()
//                .shouldNotBeNull()
//                .shouldBeInstanceOf<LibCallElement>()
//
//            assertSoftly(element) {
//                structureForm shouldBe StructureForm.NO_TYPE
//                offset.intValue shouldBe -6
//                a1.intValue shouldBe 0
//                id shouldBe "ResetBattClock"
//            }
//        }

        // PROCEDURE ReadBattClock* [base,-12] () : e.ULONG;
        assertSoftly(parseResult.value.libCalls[1]) {
            structureForm shouldBe PredefinedStructureForm.LONG_INT  // return type
            offset.intValue shouldBe -12 // offset
            a1.intValue shouldBe 0
            id shouldBe "ReadBattClock"
        }
//        run {
//            var elementClass = dataInputStream.readElementClass()
//            elementClass shouldBe ElementClass.PARAMETER_LIST
//
//            elementClass = dataInputStream.readElementClass()
//            elementClass shouldBe ElementClass.LIBCALL
//
//            val structureFormId = dataInputStream.readCompactInt()
//            structureFormId shouldBe StructureForm.LONG_INT.id  // return type
//
//            val a0 = dataInputStream.readCompactInt()
//            a0 shouldBe -12 // offset
//            val a1 = dataInputStream.readCompactInt()
//            a1 shouldBe 0
//
//            val id = dataInputStream.readId()
//            id shouldBe "ReadBattClock"
//        }

        // PROCEDURE WriteBattClock* [base,-18] ( time [0] : e.ULONG );
        assertSoftly(parseResult.value.libCalls[2]) {
            structureForm shouldBe PredefinedStructureForm.NO_TYPE
            offset.intValue shouldBe -18
            a1.intValue shouldBe 0
            id shouldBe "WriteBattClock"
        }

        // TODO zuordnung parameter list zu lib call
//        run {
//            var elementClass = dataInputStream.readElementClass()
//            elementClass shouldBe ElementClass.PARAMETER_LIST
//
//            elementClass = dataInputStream.readElementClass()
//            elementClass shouldBe ElementClass.VALUE_PARAMETER
//
//            val structureFormId = dataInputStream.readCompactInt()
//            structureFormId shouldBe StructureForm.LONG_INT.id
//
//            val a0 = dataInputStream.readCompactInt()
//            a0 shouldBe 0   // reg spec?
//
//            val id = dataInputStream.readId()
//            id shouldBe "time"
//        }

    }
}