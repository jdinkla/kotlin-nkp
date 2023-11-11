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
)

internal val function2 = FunctionSignature(
    "main",
    null,
    listOf(),
)

internal val class1 = ClassSignature(
    "HelloWorld",
    listOf(Parameter("many", "Int")),
    listOf(FunctionSignature("toString", "String", listOf())),
)

internal val class2 = ClassSignature("Gen", listOf(), listOf())

internal val class3 = ClassSignature(
    "GenImpl",
    listOf(Parameter("many", "Int")),
    listOf(),
    listOf("Gen"),
)
