package net.dinkla.kpnk

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
    visibility = Visibility.INTERNAL,
)

internal val function2 = FunctionSignature(
    "main",
)

internal val function3 = FunctionSignature(
    "extensionFun",
    "String",
    listOf(),
    "HelloWorld",
    visibility = Visibility.PRIVATE,
)

internal val class1 = ClassSignature(
    "HelloWorld",
    listOf(Parameter("many", "Int")),
    listOf(FunctionSignature("toString", "String", listOf())),
    visibility = Visibility.INTERNAL,
    type = ObjectType.DATA_CLASS,
)

internal val class2 = ClassSignature(
    "Gen",
    listOf(),
    listOf(FunctionSignature("gen", "String", listOf(Parameter("n", "Int")))),
    visibility = Visibility.PRIVATE,
    type = ObjectType.INTERFACE,
)

internal val class3 = ClassSignature(
    "GenImpl",
    listOf(Parameter("many", "Int")),
    listOf(FunctionSignature("gen", "String", listOf(Parameter("n", "Int")))),
    listOf("Gen"),
    visibility = Visibility.PUBLIC,
    type = ObjectType.CLASS,
)

internal val class4 = ClassSignature(
    "MathUtils",
    functions = listOf(FunctionSignature("isZero", "Boolean", listOf(Parameter("x", "Double")))),
    type = ObjectType.OBJECT,
)

internal val enum1 = ClassSignature(
    "AB",
    type = ObjectType.ENUM,
)

internal val enum2 = ClassSignature(
    "ABC",
    listOf(Parameter("i", "Int")),
    type = ObjectType.ENUM,
)
