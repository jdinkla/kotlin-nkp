package net.dinkla.nkp

import net.dinkla.nkp.domain.AnalysedFile
import net.dinkla.nkp.domain.ClassModifier
import net.dinkla.nkp.domain.ClassParameter
import net.dinkla.nkp.domain.ClassSignature
import net.dinkla.nkp.domain.FileName
import net.dinkla.nkp.domain.Files
import net.dinkla.nkp.domain.FunctionParameter
import net.dinkla.nkp.domain.FunctionSignature
import net.dinkla.nkp.domain.Import
import net.dinkla.nkp.domain.ImportedElement
import net.dinkla.nkp.domain.InheritanceModifier
import net.dinkla.nkp.domain.MemberModifier
import net.dinkla.nkp.domain.Package
import net.dinkla.nkp.domain.PackageName
import net.dinkla.nkp.domain.Property
import net.dinkla.nkp.domain.PropertyModifier
import net.dinkla.nkp.domain.Type
import net.dinkla.nkp.domain.TypeAlias
import net.dinkla.nkp.domain.VisibilityModifier
import net.dinkla.nkp.utilities.fromFile
import java.io.File

private const val KOTLIN_EXAMPLE_FILE = "src/examples/kotlin/examples/HelloWorld.kt"

val SOURCE_DIRECTORY = File("src/examples/kotlin/")

internal val tree = fromFile(KOTLIN_EXAMPLE_FILE)

internal val expectedImports =
    listOf(
        Import(ImportedElement("java.lang.Boolean.FALSE")),
        Import(ImportedElement("java.lang.Boolean.TRUE")),
    )

internal val function1 =
    FunctionSignature(
        "topLevelFunction",
        Type("String"),
        listOf(FunctionParameter("n", Type("Int")), FunctionParameter("hw", Type("HelloWorld"))),
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
        listOf(FunctionParameter("f", Type("(Int) -> String")), FunctionParameter("x", Type("Int"))),
    )

internal val function5 =
    FunctionSignature(
        "create",
        Type("Dictionary"),
        listOf(FunctionParameter("ls", Type("List<String>"))),
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
        declarations = listOf(FunctionSignature("gen", Type("String?"), listOf(FunctionParameter("n", Type("Int"))))),
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
                    listOf(FunctionParameter("n", Type("Int"))),
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
                    listOf(FunctionParameter("x", Type("Double"))),
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
                    listOf(FunctionParameter("x", Type("Int"))),
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

internal val f1 = FunctionSignature("f1")
internal val f2 = FunctionSignature("f2")
internal val c1 = ClassSignature("C")
internal val ta1 = TypeAlias("TA", Type("Int"))
internal val p1 = Property("p1", Type("Int"))

internal val analysedFile =
    AnalysedFile(
        FileName("/base/ExampleFile.kt"),
        PackageName("net.dinkla.nkp"),
        imports = expectedImports,
        declarations = listOf(f1, f2, c1, ta1, p1),
    )

internal val analysedFile1 =
    AnalysedFile(
        FileName("/base/ExampleFile1.kt"),
        PackageName("net.dinkla.nkp"),
        imports = expectedImports,
        declarations = listOf(f1, c1, ta1),
    )

internal val analysedFile2 =
    AnalysedFile(
        FileName("/base/ExampleFile2.kt"),
        PackageName("net.dinkla.nkp"),
        imports = expectedImports + listOf(Import(ImportedElement("net.dinkla.nkp.some"))),
        declarations = listOf(f2, p1),
    )

val examplePackage = Package(PackageName("net.dinkla.nkp"), listOf(analysedFile1, analysedFile2))

val exampleFiles = Files("/base", listOf(analysedFile, analysedFile1, analysedFile2))
