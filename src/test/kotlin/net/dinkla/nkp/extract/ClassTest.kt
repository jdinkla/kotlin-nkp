package net.dinkla.nkp.extract

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import io.kotest.matchers.shouldBe
import net.dinkla.nkp.class1
import net.dinkla.nkp.class2
import net.dinkla.nkp.class3
import net.dinkla.nkp.class4
import net.dinkla.nkp.class5
import net.dinkla.nkp.domain.kotlinlang.ClassModifier
import net.dinkla.nkp.domain.kotlinlang.ClassParameter
import net.dinkla.nkp.domain.kotlinlang.ClassSignature
import net.dinkla.nkp.domain.kotlinlang.FunctionParameter
import net.dinkla.nkp.domain.kotlinlang.FunctionSignature
import net.dinkla.nkp.domain.kotlinlang.InheritanceModifier
import net.dinkla.nkp.domain.kotlinlang.Property
import net.dinkla.nkp.domain.kotlinlang.PropertyModifier
import net.dinkla.nkp.domain.kotlinlang.Type
import net.dinkla.nkp.domain.kotlinlang.VisibilityModifier
import net.dinkla.nkp.enum1
import net.dinkla.nkp.enum2
import net.dinkla.nkp.tree
import net.dinkla.nkp.utilities.fromText
import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree

class ClassTest :
    StringSpec({
        "extractClasses should handle a data class with one constructor argument and without a body" {
            val classes = extractClassesAndObjects(fromText("data class HelloWorld(val many: Int)"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "HelloWorld",
                        listOf(ClassParameter("many", Type("Int"), propertyModifier = PropertyModifier.VAL)),
                        listOf(),
                        elementType = ClassSignature.Type.CLASS,
                        classModifier = ClassModifier.DATA,
                    ),
                )
        }

        "extractClasses should handle a data class with one private constructor argument and without a body" {
            val classes = extractClassesAndObjects(fromText("class HelloWorld(private val many: Int)"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "HelloWorld",
                        listOf(ClassParameter("many", Type("Int"), VisibilityModifier.PRIVATE, PropertyModifier.VAL)),
                        listOf(),
                        elementType = ClassSignature.Type.CLASS,
                    ),
                )
        }

        "extractClasses should handle a data class with one constructor argument with one method f(x) = x+1" {
            val classes =
                extractClassesAndObjects(fromText("data class HelloWorld(val many: Int) { fun f(x: Int) = x+1 }"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "HelloWorld",
                        listOf(ClassParameter("many", Type("Int"), propertyModifier = PropertyModifier.VAL)),
                        elementType = ClassSignature.Type.CLASS,
                        classModifier = ClassModifier.DATA,
                        declarations =
                            listOf(
                                FunctionSignature(
                                    "f",
                                    null,
                                    listOf(FunctionParameter("x", Type("Int"))),
                                ),
                            ),
                    ),
                )
        }

        "extractClasses should handle a data class with one constructor argument with two methods f and g" {
            val classes =
                extractClassesAndObjects(
                    fromText("data class HelloWorld(val many: Int) { fun f() = 1; fun g() = 2 }"),
                )
            classes shouldBe
                listOf(
                    ClassSignature(
                        "HelloWorld",
                        listOf(ClassParameter("many", Type("Int"), propertyModifier = PropertyModifier.VAL)),
                        elementType = ClassSignature.Type.CLASS,
                        classModifier = ClassModifier.DATA,
                        declarations =
                            listOf(
                                FunctionSignature("f", null, listOf()),
                                FunctionSignature("g", null, listOf()),
                            ),
                    ),
                )
        }

        "extractClasses should handle a class with one constructor argument many: Int without any methods" {
            val classes = extractClassesAndObjects(fromText("class HelloWorld(val many: Int)"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "HelloWorld",
                        listOf(ClassParameter("many", Type("Int"), propertyModifier = PropertyModifier.VAL)),
                        listOf(),
                        elementType = ClassSignature.Type.CLASS,
                    ),
                )
        }

        "extractClasses should handle a class with one constructor argument many: Int with one method f(x) = x+1" {
            val classes = extractClassesAndObjects(fromText("class HelloWorld(val many: Int) { fun f(x: Int) = x+1 }"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "HelloWorld",
                        listOf(ClassParameter("many", Type("Int"), propertyModifier = PropertyModifier.VAL)),
                        elementType = ClassSignature.Type.CLASS,
                        declarations =
                            listOf(
                                FunctionSignature(
                                    "f",
                                    null,
                                    listOf(FunctionParameter("x", Type("Int"))),
                                ),
                            ),
                    ),
                )
        }

        "extractClasses should handle a class that inherits from interface A" {
            val classes = extractClassesAndObjects(fromText("class HelloWorld(val many: Int): A"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "HelloWorld",
                        listOf(ClassParameter("many", Type("Int"), propertyModifier = PropertyModifier.VAL)),
                        listOf("A"),
                        elementType = ClassSignature.Type.CLASS,
                    ),
                )
        }

        "extractClasses should handle a class that inherits from interfaces A and B" {
            val classes = extractClassesAndObjects(fromText("class HelloWorld(val many: Int): A, B"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "HelloWorld",
                        listOf(ClassParameter("many", Type("Int"), propertyModifier = PropertyModifier.VAL)),
                        listOf("A", "B"),
                        elementType = ClassSignature.Type.CLASS,
                    ),
                )
        }

        "extractClasses should handle a class that inherits from class A" {
            val classes = extractClassesAndObjects(fromText("class HelloWorld(val many: Int): A()"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "HelloWorld",
                        listOf(ClassParameter("many", Type("Int"), propertyModifier = PropertyModifier.VAL)),
                        listOf("A"),
                        elementType = ClassSignature.Type.CLASS,
                    ),
                )
        }

        "extractClasses should handle a class that inherits from class A and pass an argument" {
            val classes = extractClassesAndObjects(fromText("class HelloWorld(val many: Int): A(many)"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "HelloWorld",
                        listOf(ClassParameter("many", Type("Int"), propertyModifier = PropertyModifier.VAL)),
                        listOf("A"),
                        elementType = ClassSignature.Type.CLASS,
                    ),
                )
        }

        "extractClasses should handle an object" {
            val classes = extractClassesAndObjects(fromText("object HelloWorld { fun f(x: Int) = x+1 }"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "HelloWorld",
                        declarations =
                            listOf(
                                FunctionSignature(
                                    "f",
                                    null,
                                    listOf(FunctionParameter("x", Type("Int"))),
                                ),
                            ),
                        elementType = ClassSignature.Type.OBJECT,
                    ),
                )
        }

        "extractClasses should handle an interface" {
            val classes = extractClassesAndObjects(fromText("interface Interface { fun f(x: Int): Int }"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "Interface",
                        declarations =
                            listOf(
                                FunctionSignature(
                                    "f",
                                    Type("Int"),
                                    listOf(FunctionParameter("x", Type("Int"))),
                                ),
                            ),
                        elementType = ClassSignature.Type.INTERFACE,
                    ),
                )
        }

        "extractClasses should handle a private interface" {
            val classes = extractClassesAndObjects(fromText("private interface Interface { fun f(x: Int): Int }"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "Interface",
                        declarations =
                            listOf(
                                FunctionSignature(
                                    "f",
                                    Type("Int"),
                                    listOf(FunctionParameter("x", Type("Int"))),
                                ),
                            ),
                        visibilityModifier = VisibilityModifier.PRIVATE,
                        elementType = ClassSignature.Type.INTERFACE,
                    ),
                )
        }

        "extractClasses should handle a public interface" {
            val classes = extractClassesAndObjects(fromText("public interface Interface { fun f() }"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "Interface",
                        declarations = listOf(FunctionSignature("f")),
                        visibilityModifier = VisibilityModifier.PUBLIC,
                        elementType = ClassSignature.Type.INTERFACE,
                    ),
                )
        }

        "extractClasses should handle the enum AB" {
            val classes = extractClassesAndObjects(fromText("enum class AB { A, B }"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "AB",
                        elementType = ClassSignature.Type.CLASS,
                        classModifier = ClassModifier.ENUM,
                    ),
                )
        }

        "extractClasses should handle the enum ABC" {
            val classes = extractClassesAndObjects(fromText("enum class ABC(val i: Int) { A(1), B(2), C(3) }"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "ABC",
                        listOf(ClassParameter("i", Type("Int"), propertyModifier = PropertyModifier.VAL)),
                        elementType = ClassSignature.Type.CLASS,
                        classModifier = ClassModifier.ENUM,
                    ),
                )
        }

        "extractClasses should handle an enum with internal constructor parameter" {
            val classes = extractClassesAndObjects(fromText("enum class A(internal val i: Int) { A(1) }"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "A",
                        listOf(ClassParameter("i", Type("Int"), VisibilityModifier.INTERNAL, PropertyModifier.VAL)),
                        elementType = ClassSignature.Type.CLASS,
                        classModifier = ClassModifier.ENUM,
                    ),
                )
        }

        "extractClasses should handle a private class" {
            val classes = extractClassesAndObjects(fromText("private class C()"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "C",
                        visibilityModifier = VisibilityModifier.PRIVATE,
                        elementType = ClassSignature.Type.CLASS,
                    ),
                )
        }

        "extractClasses should handle an internal class" {
            val classes = extractClassesAndObjects(fromText("internal class C()"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "C",
                        visibilityModifier = VisibilityModifier.INTERNAL,
                        elementType = ClassSignature.Type.CLASS,
                    ),
                )
        }

        "extractClasses should handle an abstract class" {
            val classes = extractClassesAndObjects(fromText("abstract class C()"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "C",
                        elementType = ClassSignature.Type.CLASS,
                        inheritanceModifier = InheritanceModifier.ABSTRACT,
                    ),
                )
        }

        "extractClasses should handle an internal data class" {
            val classes = extractClassesAndObjects(fromText("internal data class C(val x: Int)"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "C",
                        listOf(ClassParameter("x", Type("Int"), propertyModifier = PropertyModifier.VAL)),
                        visibilityModifier = VisibilityModifier.INTERNAL,
                        elementType = ClassSignature.Type.CLASS,
                        classModifier = ClassModifier.DATA,
                    ),
                )
        }

        "extractClass should handle an open class with a protected function" {
            val classes = extractClassesAndObjects(fromText("open class C() { protected fun f(): Int = 1 }"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "C",
                        declarations =
                            listOf(
                                FunctionSignature(
                                    "f",
                                    Type("Int"),
                                    listOf(),
                                    visibilityModifier = VisibilityModifier.PROTECTED,
                                ),
                            ),
                        elementType = ClassSignature.Type.CLASS,
                        inheritanceModifier = InheritanceModifier.OPEN,
                    ),
                )
        }

        "extractClass should handle a value class" {
            val classes = extractClassesAndObjects(fromText("value class C(val x: Int)"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "C",
                        listOf(ClassParameter("x", Type("Int"), propertyModifier = PropertyModifier.VAL)),
                        elementType = ClassSignature.Type.CLASS,
                        classModifier = ClassModifier.VALUE,
                    ),
                )
        }

        "extractClass should handle a property inside a class" {
            val classes = extractClassesAndObjects(fromText("class C() { val y: Int = 1 }"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "C",
                        elementType = ClassSignature.Type.CLASS,
                        declarations = listOf(Property("y", Type("Int"))),
                    ),
                )
        }

        "extractClass should handle a property inside an object" {
            val classes = extractClassesAndObjects(fromText("object O { val y: Int = 1 }"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "O",
                        elementType = ClassSignature.Type.OBJECT,
                        declarations = listOf(Property("y", Type("Int"))),
                    ),
                )
        }

        "extractClass should handle a class inside a class" {
            val classes = extractClassesAndObjects(fromText("class C() { class D() }"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "C",
                        elementType = ClassSignature.Type.CLASS,
                        declarations =
                            listOf(
                                ClassSignature(
                                    "D",
                                    elementType = ClassSignature.Type.CLASS,
                                ),
                            ),
                    ),
                )
        }

        "extractClass should handle an inner class inside a class" {
            val classes = extractClassesAndObjects(fromText("class C() { inner class D() }"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "C",
                        elementType = ClassSignature.Type.CLASS,
                        declarations =
                            listOf(
                                ClassSignature(
                                    "D",
                                    elementType = ClassSignature.Type.CLASS,
                                    classModifier = ClassModifier.INNER,
                                ),
                            ),
                    ),
                )
        }

        "extractClass should handle a class inside an object" {
            val classes = extractClassesAndObjects(fromText("object O { class D() }"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "O",
                        elementType = ClassSignature.Type.OBJECT,
                        declarations =
                            listOf(
                                ClassSignature(
                                    "D",
                                    elementType = ClassSignature.Type.CLASS,
                                ),
                            ),
                    ),
                )
        }

        "extractClass should handle an object inside an object" {
            val classes = extractClassesAndObjects(fromText("object O { object P { fun f() = 1 } }"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "O",
                        elementType = ClassSignature.Type.OBJECT,
                        declarations =
                            listOf(
                                ClassSignature(
                                    "P",
                                    elementType = ClassSignature.Type.OBJECT,
                                    declarations = listOf(FunctionSignature("f")),
                                ),
                            ),
                    ),
                )
        }

        "extractClass should handle an object inside a class" {
            val classes = extractClassesAndObjects(fromText("class C { object P { fun f() = 1 } }"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "C",
                        elementType = ClassSignature.Type.CLASS,
                        declarations =
                            listOf(
                                ClassSignature(
                                    "P",
                                    elementType = ClassSignature.Type.OBJECT,
                                    declarations = listOf(FunctionSignature("f")),
                                ),
                            ),
                    ),
                )
        }

        "extractClass should handle a sealed interface" {
            val classes =
                extractClassesAndObjects(fromText("sealed interface SI { data class DC(val name: String) : SI }"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "SI",
                        elementType = ClassSignature.Type.INTERFACE,
                        classModifier = ClassModifier.SEALED,
                        declarations =
                            listOf(
                                ClassSignature(
                                    "DC",
                                    listOf(
                                        ClassParameter("name", Type("String"), propertyModifier = PropertyModifier.VAL),
                                    ),
                                    elementType = ClassSignature.Type.CLASS,
                                    classModifier = ClassModifier.DATA,
                                    superTypes = listOf("SI"),
                                ),
                            ),
                    ),
                )
        }

        "extractClass should handle a sealed class" {
            val classes =
                extractClassesAndObjects(fromText("sealed class V { object O : V() { const val name = \"x\" }}"))
            classes shouldBe
                listOf(
                    ClassSignature(
                        "V",
                        elementType = ClassSignature.Type.CLASS,
                        classModifier = ClassModifier.SEALED,
                        declarations =
                            listOf(
                                ClassSignature(
                                    "O",
                                    elementType = ClassSignature.Type.OBJECT,
                                    superTypes = listOf("V"),
                                    declarations = listOf(Property("name", null, PropertyModifier.CONST_VAL)),
                                ),
                            ),
                    ),
                )
        }

        "extractClasses should return all classes" {
            val classes = extractClassesAndObjects(tree)
            classes shouldContainExactlyInAnyOrder listOf(class1, class2, class3, class4, class5, enum1, enum2)
        }
    })

private fun extractClassesAndObjects(tree: KotlinParseTree): List<ClassSignature> {
    val result = mutableListOf<ClassSignature>()
    for (declaration in getDeclarations(tree)) {
        if (declaration.name == "classDeclaration") {
            result += extractClass(declaration)
        }
        if (declaration.name == "objectDeclaration") {
            result += extractObject(declaration)
        }
    }
    return result
}
