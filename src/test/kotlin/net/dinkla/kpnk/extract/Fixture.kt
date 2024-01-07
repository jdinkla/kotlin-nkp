package net.dinkla.kpnk.extract

import net.dinkla.kpnk.domain.ClassModifier
import net.dinkla.kpnk.domain.ClassParameter
import net.dinkla.kpnk.domain.ClassSignature
import net.dinkla.kpnk.domain.FullyQualifiedName
import net.dinkla.kpnk.domain.FunctionSignature
import net.dinkla.kpnk.domain.Import
import net.dinkla.kpnk.domain.InheritanceModifier
import net.dinkla.kpnk.domain.MemberModifier
import net.dinkla.kpnk.domain.Parameter
import net.dinkla.kpnk.domain.Property
import net.dinkla.kpnk.domain.PropertyModifier
import net.dinkla.kpnk.domain.Type
import net.dinkla.kpnk.domain.VisibilityModifier
import net.dinkla.kpnk.utilities.fromFile

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
    extensionOf = "HelloWorld",
    visibilityModifier = VisibilityModifier.PRIVATE,
)

internal val function4 = FunctionSignature(
    "higherOrderFunction",
    "(Int) -> String",
    listOf(Parameter("f", "(Int) -> String"), Parameter("x", "Int")),
)

internal val function5 = FunctionSignature(
    "create",
    "Dictionary",
    listOf(Parameter("ls", "List")),
)

internal val class1 = ClassSignature(
    "HelloWorld",
    listOf(ClassParameter("many", "Int", propertyModifier = PropertyModifier.VAL)),
    declarations = listOf(
        FunctionSignature(
            "toString",
            "String",
            listOf(),
            memberModifier = MemberModifier.OVERRIDE,
        ),
    ),
    visibilityModifier = VisibilityModifier.INTERNAL,
    elementType = Type.CLASS,
    classModifier = ClassModifier.DATA,
)

internal val class2 = ClassSignature(
    "Gen",
    listOf(),
    declarations = listOf(FunctionSignature("gen", "String?", listOf(Parameter("n", "Int")))),
    visibilityModifier = VisibilityModifier.PRIVATE,
    elementType = Type.INTERFACE,
)

internal val class3 = ClassSignature(
    "GenImpl",
    listOf(ClassParameter("many", "Int", propertyModifier = PropertyModifier.VAL)),
    declarations = listOf(
        FunctionSignature(
            "gen",
            "String",
            listOf(Parameter("n", "Int")),
            memberModifier = MemberModifier.OVERRIDE,
        ),
    ),
    inheritedFrom = listOf("Gen"),
    elementType = Type.CLASS,
)

internal val class4 = ClassSignature(
    "MathUtils",
    declarations = listOf(
        Property("K_EPSILON", null, PropertyModifier.CONST_VAL, VisibilityModifier.PRIVATE),
        FunctionSignature(
            "isZero",
            "Boolean",
            listOf(Parameter("x", "Double")),
        ),
    ),
    elementType = Type.OBJECT,
)

internal val class5 = ClassSignature(
    "O1",
    declarations = listOf(
        FunctionSignature(
            "f",
            "String",
            listOf(Parameter("x", "Int")),
            visibilityModifier = VisibilityModifier.PROTECTED,
        ),
        ClassSignature(
            "I",
            listOf(ClassParameter("name", "String", propertyModifier = PropertyModifier.VAL)),
            elementType = Type.CLASS,
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
    listOf(ClassParameter("i", "Int", VisibilityModifier.INTERNAL, PropertyModifier.VAL)),
    elementType = Type.CLASS,
    classModifier = ClassModifier.ENUM,
)

internal val property1 = Property("myProperty", "String", PropertyModifier.VAL)
internal val property2 = Property("THE_ANSWER", "Int", PropertyModifier.CONST_VAL, VisibilityModifier.INTERNAL)
internal val property3 = Property("twentyOne", null, PropertyModifier.VAL, VisibilityModifier.PRIVATE)
