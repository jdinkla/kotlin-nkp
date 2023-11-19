package net.dinkla.kpnk.extract

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import net.dinkla.kpnk.domain.ClassModifier
import net.dinkla.kpnk.domain.ClassSignature
import net.dinkla.kpnk.domain.FunctionSignature
import net.dinkla.kpnk.domain.InheritanceModifier
import net.dinkla.kpnk.domain.Parameter
import net.dinkla.kpnk.domain.Property
import net.dinkla.kpnk.domain.Type
import net.dinkla.kpnk.domain.VisibilityModifier
import net.dinkla.kpnk.fromText

class ExtractClassesTest : StringSpec({
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
                elementType = Type.CLASS,
                classModifier = ClassModifier.DATA,
                declarations = listOf(FunctionSignature("f", null, listOf(Parameter("x", "Int")))),
            ),
        )
    }

    "extractClasses should handle a data class with one constructor argument with two methods f and g" {
        val classes = extractClasses(
            fromText("data class HelloWorld(val many: Int) { fun f() = 1; fun g() = 2 }"),
        )
        classes shouldBe listOf(
            ClassSignature(
                "HelloWorld",
                listOf(Parameter("many", "Int")),
                elementType = Type.CLASS,
                classModifier = ClassModifier.DATA,
                declarations = listOf(
                    FunctionSignature("f", null, listOf()),
                    FunctionSignature("g", null, listOf()),
                ),
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
                elementType = Type.CLASS,
                declarations = listOf(FunctionSignature("f", null, listOf(Parameter("x", "Int")))),
            ),
        )
    }

    "extractClasses should handle a class that inherits from interface A" {
        val classes = extractClasses(fromText("class HelloWorld(val many: Int): A"))
        classes shouldBe listOf(
            ClassSignature(
                "HelloWorld",
                listOf(Parameter("many", "Int")),
                listOf("A"),
                elementType = Type.CLASS,
            ),
        )
    }

    "extractClasses should handle a class that inherits from interfaces A and B" {
        val classes = extractClasses(fromText("class HelloWorld(val many: Int): A, B"))
        classes shouldBe listOf(
            ClassSignature(
                "HelloWorld",
                listOf(Parameter("many", "Int")),
                listOf("A", "B"),
                elementType = Type.CLASS,
            ),
        )
    }

    "extractClasses should handle a class that inherits from class A" {
        val classes = extractClasses(fromText("class HelloWorld(val many: Int): A()"))
        classes shouldBe listOf(
            ClassSignature(
                "HelloWorld",
                listOf(Parameter("many", "Int")),
                listOf("A"),
                elementType = Type.CLASS,
            ),
        )
    }

    "extractClasses should handle a class that inherits from class A and pass an argument" {
        val classes = extractClasses(fromText("class HelloWorld(val many: Int): A(many)"))
        classes shouldBe listOf(
            ClassSignature(
                "HelloWorld",
                listOf(Parameter("many", "Int")),
                listOf("A"),
                elementType = Type.CLASS,
            ),
        )
    }

    "extractClasses should handle an object" {
        val classes = extractClasses(fromText("object HelloWorld { fun f(x: Int) = x+1 }"))
        classes shouldBe listOf(
            ClassSignature(
                "HelloWorld",
                declarations = listOf(FunctionSignature("f", null, listOf(Parameter("x", "Int")))),
                elementType = Type.OBJECT,
            ),
        )
    }

    "extractClasses should handle an interface" {
        val classes = extractClasses(fromText("interface Interface { fun f(x: Int): Int }"))
        classes shouldBe listOf(
            ClassSignature(
                "Interface",
                declarations = listOf(FunctionSignature("f", "Int", listOf(Parameter("x", "Int")))),
                elementType = Type.INTERFACE,
            ),
        )
    }

    "extractClasses should handle a private interface" {
        val classes = extractClasses(fromText("private interface Interface { fun f(x: Int): Int }"))
        classes shouldBe listOf(
            ClassSignature(
                "Interface",
                declarations = listOf(FunctionSignature("f", "Int", listOf(Parameter("x", "Int")))),
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
                declarations = listOf(FunctionSignature("f")),
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

    "extractClass should handle an open class with a protected function" {
        val classes = extractClasses(fromText("open class C() { protected fun f(): Int = 1 }"))
        classes shouldBe listOf(
            ClassSignature(
                "C",
                listOf(),
                declarations = listOf(
                    FunctionSignature(
                        "f",
                        "Int",
                        listOf(),
                        visibilityModifier = VisibilityModifier.PROTECTED,
                    ),
                ),
                elementType = Type.CLASS,
                inheritanceModifier = InheritanceModifier.OPEN,
            ),
        )
    }

    "extractClass should handle a value class" {
        val classes = extractClasses(fromText("value class C(val x: Int)"))
        classes shouldBe listOf(
            ClassSignature(
                "C",
                listOf(Parameter("x", "Int")),
                elementType = Type.CLASS,
                classModifier = ClassModifier.VALUE,
            ),
        )
    }

    "extractClass should handle a property inside a class" {
        val classes = extractClasses(fromText("class C() { val y: Int = 1 }"))
        classes shouldBe listOf(
            ClassSignature(
                "C",
                elementType = Type.CLASS,
                declarations = listOf(Property("y", "Int")),
            ),
        )
    }

    "extractClass should handle a property inside an object" {
        val classes = extractClasses(fromText("object O { val y: Int = 1 }"))
        classes shouldBe listOf(
            ClassSignature(
                "O",
                elementType = Type.OBJECT,
                declarations = listOf(Property("y", "Int")),
            ),
        )
    }

    "extractClass should handle a class inside a class" {
        val classes = extractClasses(fromText("class C() { class D() }"))
        classes shouldBe listOf(
            ClassSignature(
                "C",
                elementType = Type.CLASS,
                declarations = listOf(
                    ClassSignature(
                        "D",
                        elementType = Type.CLASS,
                    ),
                ),
            ),
        )
    }

    "extractClasses should return all classes" {
        val classes = extractClasses(tree)
        classes shouldContainExactlyInAnyOrder listOf(class1, class2, class3, class4, class5, enum1, enum2)
    }
})
