package net.dinkla.nkp.parser

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe

/**
 * Parity tests to ensure PSI parser produces the same domain models as Grammar-tools parser.
 */
class ParserParityTest :
    StringSpec({
        val testFile = "src/examples/kotlin/examples/HelloWorld.kt"
        val prefix = "src/examples/kotlin"
        val psiParser = PsiParser()
        val grammarParser = GrammarToolsParser()

        "both parsers should extract the same package name" {
            // When
            val psiResult = psiParser.parseFile(testFile, prefix).getOrThrow()
            val grammarResult = grammarParser.parseFile(testFile, prefix).getOrThrow()

            // Then
            psiResult.packageName shouldBe grammarResult.packageName
        }

        "both parsers should extract the same imports" {
            // When
            val psiResult = psiParser.parseFile(testFile, prefix).getOrThrow()
            val grammarResult = grammarParser.parseFile(testFile, prefix).getOrThrow()

            // Then
            psiResult.imports.map { it.name.name } shouldContainExactlyInAnyOrder
                grammarResult.imports.map { it.name.name }
        }

        "both parsers should extract the same class names" {
            // When
            val psiResult = psiParser.parseFile(testFile, prefix).getOrThrow()
            val grammarResult = grammarParser.parseFile(testFile, prefix).getOrThrow()

            // Then
            val psiClasses = psiResult.classes.map { it.name }
            val grammarClasses = grammarResult.classes.map { it.name }
            psiClasses shouldContainExactlyInAnyOrder grammarClasses
        }

        "both parsers should extract the same function names" {
            // When
            val psiResult = psiParser.parseFile(testFile, prefix).getOrThrow()
            val grammarResult = grammarParser.parseFile(testFile, prefix).getOrThrow()

            // Then
            val psiFunctions = psiResult.functions.map { it.name }
            val grammarFunctions = grammarResult.functions.map { it.name }
            psiFunctions shouldContainExactlyInAnyOrder grammarFunctions
        }

        "both parsers should extract the same property names" {
            // When
            val psiResult = psiParser.parseFile(testFile, prefix).getOrThrow()
            val grammarResult = grammarParser.parseFile(testFile, prefix).getOrThrow()

            // Then
            val psiProperties = psiResult.properties.map { it.name }
            val grammarProperties = grammarResult.properties.map { it.name }
            psiProperties shouldContainExactlyInAnyOrder grammarProperties
        }

        "both parsers should extract the same type alias names" {
            // When
            val psiResult = psiParser.parseFile(testFile, prefix).getOrThrow()
            val grammarResult = grammarParser.parseFile(testFile, prefix).getOrThrow()

            // Then
            val psiTypeAliases = psiResult.typeAliases.map { it.name }
            val grammarTypeAliases = grammarResult.typeAliases.map { it.name }
            psiTypeAliases shouldContainExactlyInAnyOrder grammarTypeAliases
        }

        "both parsers should extract the same class details for HelloWorld" {
            // When
            val psiResult = psiParser.parseFile(testFile, prefix).getOrThrow()
            val grammarResult = grammarParser.parseFile(testFile, prefix).getOrThrow()

            // Then
            val psiClass = psiResult.classes.find { it.name == "HelloWorld" }!!
            val grammarClass = grammarResult.classes.find { it.name == "HelloWorld" }!!

            psiClass.elementType shouldBe grammarClass.elementType
            psiClass.visibilityModifier shouldBe grammarClass.visibilityModifier
            psiClass.classModifier shouldBe grammarClass.classModifier
            psiClass.parameters.map { it.name } shouldBe grammarClass.parameters.map { it.name }
            psiClass.parameters.map { it.type } shouldBe grammarClass.parameters.map { it.type }
        }

        "both parsers should extract the same interface details for Gen" {
            // When
            val psiResult = psiParser.parseFile(testFile, prefix).getOrThrow()
            val grammarResult = grammarParser.parseFile(testFile, prefix).getOrThrow()

            // Then
            val psiInterface = psiResult.classes.find { it.name == "Gen" }!!
            val grammarInterface = grammarResult.classes.find { it.name == "Gen" }!!

            psiInterface.elementType shouldBe grammarInterface.elementType
            psiInterface.visibilityModifier shouldBe grammarInterface.visibilityModifier
        }

        "both parsers should extract the same object details for MathUtils" {
            // When
            val psiResult = psiParser.parseFile(testFile, prefix).getOrThrow()
            val grammarResult = grammarParser.parseFile(testFile, prefix).getOrThrow()

            // Then
            val psiObject = psiResult.classes.find { it.name == "MathUtils" }!!
            val grammarObject = grammarResult.classes.find { it.name == "MathUtils" }!!

            psiObject.elementType shouldBe grammarObject.elementType
            psiObject.declarations.size shouldBe grammarObject.declarations.size
        }

        "both parsers should extract the same function details for topLevelFunction" {
            // When
            val psiResult = psiParser.parseFile(testFile, prefix).getOrThrow()
            val grammarResult = grammarParser.parseFile(testFile, prefix).getOrThrow()

            // Then
            val psiFunction = psiResult.functions.find { it.name == "topLevelFunction" }!!
            val grammarFunction = grammarResult.functions.find { it.name == "topLevelFunction" }!!

            psiFunction.returnType shouldBe grammarFunction.returnType
            psiFunction.visibilityModifier shouldBe grammarFunction.visibilityModifier
            psiFunction.parameters.size shouldBe grammarFunction.parameters.size
            psiFunction.parameters.map { it.name } shouldBe grammarFunction.parameters.map { it.name }
            psiFunction.parameters.map { it.type } shouldBe grammarFunction.parameters.map { it.type }
        }

        "both parsers should extract the same extension function details" {
            // When
            val psiResult = psiParser.parseFile(testFile, prefix).getOrThrow()
            val grammarResult = grammarParser.parseFile(testFile, prefix).getOrThrow()

            // Then
            val psiExtFun = psiResult.functions.find { it.name == "extensionFun" }!!
            val grammarExtFun = grammarResult.functions.find { it.name == "extensionFun" }!!

            psiExtFun.extensionOf shouldBe grammarExtFun.extensionOf
            psiExtFun.returnType shouldBe grammarExtFun.returnType
            psiExtFun.visibilityModifier shouldBe grammarExtFun.visibilityModifier
        }

        "both parsers should extract the same property details" {
            // When
            val psiResult = psiParser.parseFile(testFile, prefix).getOrThrow()
            val grammarResult = grammarParser.parseFile(testFile, prefix).getOrThrow()

            // Then
            val psiProperty = psiResult.properties.find { it.name == "myProperty" }!!
            val grammarProperty = grammarResult.properties.find { it.name == "myProperty" }!!

            psiProperty.dataType shouldBe grammarProperty.dataType
            psiProperty.modifier shouldBe grammarProperty.modifier
        }

        "both parsers should extract const property correctly" {
            // When
            val psiResult = psiParser.parseFile(testFile, prefix).getOrThrow()
            val grammarResult = grammarParser.parseFile(testFile, prefix).getOrThrow()

            // Then
            val psiConst = psiResult.properties.find { it.name == "THE_ANSWER" }!!
            val grammarConst = grammarResult.properties.find { it.name == "THE_ANSWER" }!!

            psiConst.modifier shouldBe grammarConst.modifier
            psiConst.visibilityModifier shouldBe grammarConst.visibilityModifier
        }

        "both parsers should extract enum class correctly" {
            // When
            val psiResult = psiParser.parseFile(testFile, prefix).getOrThrow()
            val grammarResult = grammarParser.parseFile(testFile, prefix).getOrThrow()

            // Then
            val psiEnum = psiResult.classes.find { it.name == "AB" }!!
            val grammarEnum = grammarResult.classes.find { it.name == "AB" }!!

            psiEnum.classModifier shouldBe grammarEnum.classModifier
            psiEnum.elementType shouldBe grammarEnum.elementType
        }

        "both parsers should extract open class with inheritance modifier" {
            // When
            val psiResult = psiParser.parseFile(testFile, prefix).getOrThrow()
            val grammarResult = grammarParser.parseFile(testFile, prefix).getOrThrow()

            // Then
            val psiOpen = psiResult.classes.find { it.name == "O1" }!!
            val grammarOpen = grammarResult.classes.find { it.name == "O1" }!!

            psiOpen.inheritanceModifier shouldBe grammarOpen.inheritanceModifier
        }

        "both parsers should extract class with supertype" {
            // When
            val psiResult = psiParser.parseFile(testFile, prefix).getOrThrow()
            val grammarResult = grammarParser.parseFile(testFile, prefix).getOrThrow()

            // Then
            val psiGenImpl = psiResult.classes.find { it.name == "GenImpl" }!!
            val grammarGenImpl = grammarResult.classes.find { it.name == "GenImpl" }!!

            psiGenImpl.superTypes shouldBe grammarGenImpl.superTypes
        }

        "both parsers should extract higher-order function types" {
            // When
            val psiResult = psiParser.parseFile(testFile, prefix).getOrThrow()
            val grammarResult = grammarParser.parseFile(testFile, prefix).getOrThrow()

            // Then
            val psiHof = psiResult.functions.find { it.name == "higherOrderFunction" }!!
            val grammarHof = grammarResult.functions.find { it.name == "higherOrderFunction" }!!

            psiHof.returnType shouldBe grammarHof.returnType
            psiHof.parameters.size shouldBe grammarHof.parameters.size
        }

        "both parsers should extract typealias correctly" {
            // When
            val psiResult = psiParser.parseFile(testFile, prefix).getOrThrow()
            val grammarResult = grammarParser.parseFile(testFile, prefix).getOrThrow()

            // Then
            val psiTypeAlias = psiResult.typeAliases.find { it.name == "Dictionary" }!!
            val grammarTypeAlias = grammarResult.typeAliases.find { it.name == "Dictionary" }!!

            psiTypeAlias.def shouldBe grammarTypeAlias.def
        }
    })
