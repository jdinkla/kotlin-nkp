package net.dinkla.kpnk

import net.dinkla.kpnk.elements.ClassModifier
import net.dinkla.kpnk.elements.ClassSignature
import net.dinkla.kpnk.elements.FullyQualifiedName
import net.dinkla.kpnk.elements.FunctionSignature
import net.dinkla.kpnk.elements.Import
import net.dinkla.kpnk.elements.InheritanceModifier
import net.dinkla.kpnk.elements.Parameter
import net.dinkla.kpnk.elements.Type
import net.dinkla.kpnk.elements.VisibilityModifier

private const val KOTLIN_EXAMPLE_FILE = "src/test/resources/example/HelloWorld.kt"

internal val tree = fromFile(KOTLIN_EXAMPLE_FILE)

internal val expectedImports = listOf(
    Import(FullyQualifiedName("java.lang.Boolean.FALSE")),
    Import(FullyQualifiedName("java.lang.Boolean.TRUE")),
)

internal val function1 = FunctionSignature(
    "topLevelFunction",
    "String",
    listOf(Parameter("n", "Int"), Parameter("hw", "HelloWorld")),
    visibilityModifier = VisibilityModifier.INTERNAL,
)

internal val function2 = FunctionSignature(
    "main",
)

internal val function3 = FunctionSignature(
    "extensionFun",
    "String",
    listOf(),
    "HelloWorld",
    visibilityModifier = VisibilityModifier.PRIVATE,
)

internal val class1 = ClassSignature(
    "HelloWorld",
    listOf(Parameter("many", "Int")),
    listOf(FunctionSignature("toString", "String", listOf())),
    visibilityModifier = VisibilityModifier.INTERNAL,
    elementType = Type.CLASS,
    classModifier = ClassModifier.DATA,
)

internal val class2 = ClassSignature(
    "Gen",
    listOf(),
    listOf(FunctionSignature("gen", "String?", listOf(Parameter("n", "Int")))),
    visibilityModifier = VisibilityModifier.PRIVATE,
    elementType = Type.INTERFACE,
)

internal val class3 = ClassSignature(
    "GenImpl",
    listOf(Parameter("many", "Int")),
    listOf(FunctionSignature("gen", "String", listOf(Parameter("n", "Int")))),
    listOf("Gen"),
    elementType = Type.CLASS,
)

internal val class4 = ClassSignature(
    "MathUtils",
    functions = listOf(FunctionSignature("isZero", "Boolean", listOf(Parameter("x", "Double")))),
    elementType = Type.OBJECT,
)

internal val class5 = ClassSignature(
    "O1",
    functions = listOf(
        FunctionSignature(
            "f",
            "String",
            listOf(Parameter("x", "Int")),
            visibilityModifier = VisibilityModifier.PROTECTED,
        ),
    ),
    elementType = Type.CLASS,
    inheritanceModifier = InheritanceModifier.OPEN,
)

internal val enum1 = ClassSignature(
    "AB",
    elementType = Type.CLASS,
    classModifier = ClassModifier.ENUM,
)

internal val enum2 = ClassSignature(
    "ABC",
    listOf(Parameter("i", "Int")),
    elementType = Type.CLASS,
    classModifier = ClassModifier.ENUM,
)
