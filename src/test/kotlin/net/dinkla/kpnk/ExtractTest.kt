package net.dinkla.kpnk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe

class ExtractTest : StringSpec({
    "extract should return all information" {
        val file = extract(tree)
        file.packageName shouldBe FullyQualifiedName("example")
        file.imports shouldContainExactly expectedImports
        file.functions shouldContainExactlyInAnyOrder listOf(function1, function2, function3)
        file.classes shouldContainExactlyInAnyOrder listOf(class1, class2, class3, class4, class5, enum1, enum2)
    }

    "extractPackageName should return the fully qualified package name" {
        val packageName = extractPackageName(fromText("package my.example.test"))
        packageName shouldBe FullyQualifiedName("my.example.test")
    }

    "extractPackageName should return the simple package name" {
        val packageName = extractPackageName(fromText("package example"))
        packageName shouldBe FullyQualifiedName("example")
    }

    "extractImports should return all imports" {
        val imports = extractImports(fromText("package example; import a.b.c; import d.e.f"))
        imports shouldContainExactly listOf(Import(FullyQualifiedName("a.b.c")), Import(FullyQualifiedName("d.e.f")))
    }

    "extractFunctions should return all functions in example code" {
        val functions = extractFunctions(tree)
        functions shouldContainExactlyInAnyOrder listOf(function1, function2, function3)
    }

    "extractFunctions should return all functions" {
        val functions = extractFunctions(fromText("fun f(x: Int): Int = x+1; fun g() = 3"))
        functions shouldContainExactly listOf(
            FunctionSignature("f", "Int", listOf(Parameter("x", "Int"))),
            FunctionSignature("g", null, listOf()),
        )
    }

    "extractFunctions should handle function with parameters and return type" {
        val functions = extractFunctions(fromText("fun f(x: Int): Int = x+1"))
        functions shouldBe listOf(FunctionSignature("f", "Int", listOf(Parameter("x", "Int"))))
    }

    "extractFunctions should handle function with parameters without explicit simple return type" {
        val functions = extractFunctions(fromText("fun f(x: Int) = x+1"))
        functions shouldBe listOf(FunctionSignature("f", null, listOf(Parameter("x", "Int"))))
    }

    "extractFunctions should handle function without parameters but with explicit simple return type" {
        val functions = extractFunctions(fromText("fun f(): Int = 1"))
        functions shouldBe listOf(FunctionSignature("f", "Int", listOf()))
    }

    "extractFunctions should handle function without parameters and without explicit simple return type" {
        val functions = extractFunctions(fromText("fun f() = 1"))
        functions shouldBe listOf(FunctionSignature("f", null, listOf()))
    }

    "extractFunctions should handle internal function with parameters and simple return type" {
        val functions = extractFunctions(fromText("internal fun f(x: Int): Int = x+1"))
        functions shouldBe listOf(
            FunctionSignature(
                "f",
                "Int",
                listOf(Parameter("x", "Int")),
                visibility = VisibilityModifier.INTERNAL,
            ),
        )
    }

    "extractFunctions should handle private function with parameters and simple return type" {
        val functions = extractFunctions(fromText("private fun f(x: Int): Int = x+1"))
        functions shouldBe listOf(
            FunctionSignature(
                "f",
                "Int",
                listOf(Parameter("x", "Int")),
                visibility = VisibilityModifier.PRIVATE,
            ),
        )
    }

    "extractFunctions should handle operator functions like plus" {
        val functions = extractFunctions(fromText("operator fun plus(x: Int, y: Int): Int = x+y"))
        functions shouldBe listOf(
            FunctionSignature(
                "plus",
                "Int",
                listOf(Parameter("x", "Int"), Parameter("y", "Int")),
            ),
        )
    }

    "extractFunctions should handle function with Any as parameter and Any as return type" {
        val functions = extractFunctions(fromText("fun f(x: Any): Any = x"))
        functions shouldBe listOf(FunctionSignature("f", "Any", listOf(Parameter("x", "Any"))))
    }

    "extractFunctions should handle nullable parameters" {
        val functions = extractFunctions(fromText("fun f(x: Int?): Int? = x"))
        functions shouldBe listOf(FunctionSignature("f", "Int?", listOf(Parameter("x", "Int?"))))
    }

    "extractClasses should handle a data class with one constructor argument and without a body" {
        val classes = extractClasses(fromText("data class HelloWorld(val many: Int)"))
        classes shouldBe listOf(
            ClassSignature(
                "HelloWorld",
                listOf(Parameter("many", "Int")),
                listOf(),
                elementType = Type.CLASS,
                classModifier = ClassModifier.DATA,
            ),
        )
    }

    "extractClasses should handle a data class with one constructor argument with one method f(x) = x+1" {
        val classes = extractClasses(fromText("data class HelloWorld(val many: Int) { fun f(x: Int) = x+1 }"))
        classes shouldBe listOf(
            ClassSignature(
                "HelloWorld",
                listOf(Parameter("many", "Int")),
                listOf(FunctionSignature("f", null, listOf(Parameter("x", "Int")))),
                elementType = Type.CLASS,
                classModifier = ClassModifier.DATA,
            ),
        )
    }

    "extractClasses should handle a data class with one constructor argument with two methods f and g" {
        val classes =
            extractClasses(
                fromText(
                    """
                data class HelloWorld(val many: Int) { fun f() = 1; fun g() = 2; class X(val y: Int) { fun h() = 3 } }
                    """.trimIndent(),
                ),
            )
        classes shouldBe listOf(
            ClassSignature(
                "HelloWorld",
                listOf(Parameter("many", "Int")),
                listOf(FunctionSignature("f", null, listOf()), FunctionSignature("g", null, listOf())),
                elementType = Type.CLASS,
                classModifier = ClassModifier.DATA,
            ),
        )
    }

    "extractClasses should handle a class with one constructor argument many: Int without any methods" {
        val classes = extractClasses(fromText("class HelloWorld(val many: Int)"))
        classes shouldBe listOf(
            ClassSignature(
                "HelloWorld",
                listOf(Parameter("many", "Int")),
                listOf(),
                elementType = Type.CLASS,
            ),
        )
    }

    "extractClasses should handle a class with one constructor argument many: Int with one method f(x) = x+1" {
        val classes = extractClasses(fromText("class HelloWorld(val many: Int) { fun f(x: Int) = x+1 }"))
        classes shouldBe listOf(
            ClassSignature(
                "HelloWorld",
                listOf(Parameter("many", "Int")),
                listOf(FunctionSignature("f", null, listOf(Parameter("x", "Int")))),
                elementType = Type.CLASS,
            ),
        )
    }

    "extractClasses should handle a class that inherits from class A" {
        val classes = extractClasses(fromText("class HelloWorld(val many: Int): A"))
        classes shouldBe listOf(
            ClassSignature(
                "HelloWorld",
                listOf(Parameter("many", "Int")),
                listOf(),
                listOf("A"),
                elementType = Type.CLASS,
            ),
        )
    }

    "extractClasses should handle a class that inherits from classes A and B" {
        val classes = extractClasses(fromText("class HelloWorld(val many: Int): A, B"))
        classes shouldBe listOf(
            ClassSignature(
                "HelloWorld",
                listOf(Parameter("many", "Int")),
                listOf(),
                listOf("A", "B"),
                elementType = Type.CLASS,
            ),
        )
    }

    "extractClasses should handle an object" {
        val classes = extractClasses(fromText("object HelloWorld { fun f(x: Int) = x+1 }"))
        classes shouldBe listOf(
            ClassSignature(
                "HelloWorld",
                functions = listOf(FunctionSignature("f", null, listOf(Parameter("x", "Int")))),
                elementType = Type.OBJECT,
            ),
        )
    }

    "extractClasses should handle an interface" {
        val classes = extractClasses(fromText("interface Interface { fun f(x: Int): Int }"))
        classes shouldBe listOf(
            ClassSignature(
                "Interface",
                functions = listOf(FunctionSignature("f", "Int", listOf(Parameter("x", "Int")))),
                elementType = Type.INTERFACE,
            ),
        )
    }

    "extractClasses should handle a private interface" {
        val classes = extractClasses(fromText("private interface Interface { fun f(x: Int): Int }"))
        classes shouldBe listOf(
            ClassSignature(
                "Interface",
                functions = listOf(FunctionSignature("f", "Int", listOf(Parameter("x", "Int")))),
                visibilityModifier = VisibilityModifier.PRIVATE,
                elementType = Type.INTERFACE,
            ),
        )
    }

    "extractClasses should handle a public interface" {
        val classes = extractClasses(fromText("public interface Interface { fun f() }"))
        classes shouldBe listOf(
            ClassSignature(
                "Interface",
                functions = listOf(FunctionSignature("f")),
                visibilityModifier = VisibilityModifier.PUBLIC,
                elementType = Type.INTERFACE,
            ),
        )
    }

    "extractClasses should handle the enum AB" {
        val classes = extractClasses(fromText("enum class AB { A, B }"))
        classes shouldBe listOf(
            ClassSignature(
                "AB",
                elementType = Type.CLASS,
                classModifier = ClassModifier.ENUM,
            ),
        )
    }

    "extractClasses should handle the enum ABC" {
        val classes = extractClasses(fromText("enum class ABC(val i: Int) { A(1), B(2), C(3) }"))
        classes shouldBe listOf(
            ClassSignature(
                "ABC",
                listOf(Parameter("i", "Int")),
                elementType = Type.CLASS,
                classModifier = ClassModifier.ENUM,
            ),
        )
    }

    "extractClasses should handle an enum with internal constructor parameter" {
        val classes = extractClasses(fromText("enum class A(internal val i: Int) { A(1) }"))
        classes shouldBe listOf(
            ClassSignature(
                "A",
                listOf(Parameter("i", "Int")),
                listOf(),
                listOf(),
                elementType = Type.CLASS,
                classModifier = ClassModifier.ENUM,
            ),
        )
    }

    "extractClasses should handle a private class" {
        val classes = extractClasses(fromText("private class C()"))
        classes shouldBe listOf(
            ClassSignature(
                "C",
                visibilityModifier = VisibilityModifier.PRIVATE,
                elementType = Type.CLASS,
            ),
        )
    }

    "extractClasses should handle an internal class" {
        val classes = extractClasses(fromText("internal class C()"))
        classes shouldBe listOf(
            ClassSignature(
                "C",
                visibilityModifier = VisibilityModifier.INTERNAL,
                elementType = Type.CLASS,
            ),
        )
    }

    "extractClasses should handle an abstract class" {
        val classes = extractClasses(fromText("abstract class C()"))
        classes shouldBe listOf(
            ClassSignature(
                "C",
                elementType = Type.CLASS,
                inheritanceModifier = InheritanceModifier.ABSTRACT,
            ),
        )
    }

    "extractClasses should handle an internal data class" {
        val classes = extractClasses(fromText("internal data class C(val x: Int)"))
        classes shouldBe listOf(
            ClassSignature(
                "C",
                listOf(Parameter("x", "Int")),
                visibilityModifier = VisibilityModifier.INTERNAL,
                elementType = Type.CLASS,
                classModifier = ClassModifier.DATA,
            ),
        )
    }

    "extractClass should handle an open class with a proteced function" {
        val classes = extractClasses(fromText("open class C() { protected fun f(): Int = 1 }"))
        classes shouldBe listOf(
            ClassSignature(
                "C",
                listOf(),
                listOf(
                    FunctionSignature(
                        "f",
                        "Int",
                        listOf(),
                        visibility = VisibilityModifier.PROTECTED,
                    ),
                ),
                elementType = Type.CLASS,
                inheritanceModifier = InheritanceModifier.OPEN,
            ),
        )
    }

    "extractClasses should return all classes" {
        val classes = extractClasses(tree)
        classes shouldContainExactlyInAnyOrder listOf(class1, class2, class3, class4, class5, enum1, enum2)
    }
})
