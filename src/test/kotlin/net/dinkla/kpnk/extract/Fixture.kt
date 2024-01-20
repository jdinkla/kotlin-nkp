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

internal val expectedImports =
    listOf(
        Import(FullyQualifiedName("java.lang.Boolean.FALSE")),
        Import(FullyQualifiedName("java.lang.Boolean.TRUE")),
    )

internal val function1 =
    FunctionSignature(
        "topLevelFunction",
        Type("String"),
        listOf(Parameter("n", Type("Int")), Parameter("hw", Type("HelloWorld"))),
        visibilityModifier = VisibilityModifier.INTERNAL,
    )

internal val function2 =
    FunctionSignature(
        "main",
    )

internal val function3 =
    FunctionSignature(
        "extensionFun",
        Type("String"),
        extensionOf = "HelloWorld",
        visibilityModifier = VisibilityModifier.PRIVATE,
    )

internal val function4 =
    FunctionSignature(
        "higherOrderFunction",
        Type("(Int) -> String"),
        listOf(Parameter("f", Type("(Int) -> String")), Parameter("x", Type("Int"))),
    )

internal val function5 =
    FunctionSignature(
        "create",
        Type("Dictionary"),
        listOf(Parameter("ls", Type("List"))),
    )

internal val class1 =
    ClassSignature(
        "HelloWorld",
        listOf(ClassParameter("many", Type("Int"), propertyModifier = PropertyModifier.VAL)),
        declarations =
            listOf(
                FunctionSignature(
                    "toString",
                    Type("String"),
                    listOf(),
                    memberModifier = MemberModifier.OVERRIDE,
                ),
            ),
        visibilityModifier = VisibilityModifier.INTERNAL,
        elementType = ClassSignature.Type.CLASS,
        classModifier = ClassModifier.DATA,
    )

internal val class2 =
    ClassSignature(
        "Gen",
        listOf(),
        declarations = listOf(FunctionSignature("gen", Type("String?"), listOf(Parameter("n", Type("Int"))))),
        visibilityModifier = VisibilityModifier.PRIVATE,
        elementType = ClassSignature.Type.INTERFACE,
    )

internal val class3 =
    ClassSignature(
        "GenImpl",
        listOf(ClassParameter("many", Type("Int"), propertyModifier = PropertyModifier.VAL)),
        declarations =
            listOf(
                FunctionSignature(
                    "gen",
                    Type("String"),
                    listOf(Parameter("n", Type("Int"))),
                    memberModifier = MemberModifier.OVERRIDE,
                ),
            ),
        inheritedFrom = listOf("Gen"),
        elementType = ClassSignature.Type.CLASS,
    )

internal val class4 =
    ClassSignature(
        "MathUtils",
        declarations =
            listOf(
                Property("K_EPSILON", null, PropertyModifier.CONST_VAL, VisibilityModifier.PRIVATE),
                FunctionSignature(
                    "isZero",
                    Type("Boolean"),
                    listOf(Parameter("x", Type("Double"))),
                ),
            ),
        elementType = ClassSignature.Type.OBJECT,
    )

internal val class5 =
    ClassSignature(
        "O1",
        declarations =
            listOf(
                FunctionSignature(
                    "f",
                    Type("String"),
                    listOf(Parameter("x", Type("Int"))),
                    visibilityModifier = VisibilityModifier.PROTECTED,
                ),
                ClassSignature(
                    "I",
                    listOf(ClassParameter("name", Type("String"), propertyModifier = PropertyModifier.VAL)),
                    elementType = ClassSignature.Type.CLASS,
                ),
            ),
        elementType = ClassSignature.Type.CLASS,
        inheritanceModifier = InheritanceModifier.OPEN,
    )

internal val enum1 =
    ClassSignature(
        "AB",
        elementType = ClassSignature.Type.CLASS,
        classModifier = ClassModifier.ENUM,
    )

internal val enum2 =
    ClassSignature(
        "ABC",
        listOf(ClassParameter("i", Type("Int"), VisibilityModifier.INTERNAL, PropertyModifier.VAL)),
        elementType = ClassSignature.Type.CLASS,
        classModifier = ClassModifier.ENUM,
    )

internal val property1 = Property("myProperty", Type("String"), PropertyModifier.VAL)
internal val property2 = Property("THE_ANSWER", Type("Int"), PropertyModifier.CONST_VAL, VisibilityModifier.INTERNAL)
internal val property3 = Property("twentyOne", null, PropertyModifier.VAL, VisibilityModifier.PRIVATE)
