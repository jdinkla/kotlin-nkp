package net.dinkla.kpnk.extract

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import net.dinkla.kpnk.domain.MemberModifier
import net.dinkla.kpnk.domain.Property
import net.dinkla.kpnk.domain.PropertyModifier
import net.dinkla.kpnk.domain.Type
import net.dinkla.kpnk.domain.VisibilityModifier
import net.dinkla.kpnk.utilities.fromText
import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree

class ExtractPropertyTest : StringSpec({

    "extractProperty should handle one val property" {
        val properties = extractProperties(fromText("val x: Int = 1"))
        properties shouldContainExactlyInAnyOrder listOf(Property("x", Type("Int"), PropertyModifier.VAL))
    }

    "extractProperty should handle one var property" {
        val properties = extractProperties(fromText("var x: Int = 1"))
        properties shouldContainExactlyInAnyOrder listOf(Property("x", Type("Int"), PropertyModifier.VAR))
    }

    "extractProperty should handle a const val property" {
        val properties = extractProperties(fromText("const val x: Int = 1"))
        properties shouldContainExactlyInAnyOrder listOf(Property("x", Type("Int"), PropertyModifier.CONST_VAL))
    }

    "extractProperty should handle a constructor call with explicit type" {
        val properties = extractProperties(fromText("val x : C = C(1)"))
        properties shouldContainExactlyInAnyOrder listOf(Property("x", Type("C"), PropertyModifier.VAL))
    }

    "extractProperty should handle a constructor call with implicit type" {
        val properties = extractProperties(fromText("val x = C(1)"))
        properties shouldContainExactlyInAnyOrder listOf(Property("x", null, PropertyModifier.VAL))
    }

    "extractProperty should handle visibility modifier private" {
        val properties = extractProperties(fromText("private val x: Int = 1"))
        properties shouldContainExactly
            listOf(
                Property(
                    "x",
                    Type("Int"),
                    PropertyModifier.VAL,
                    VisibilityModifier.PRIVATE,
                ),
            )
    }

    "extractProperty should handle overriden property" {
        val properties = extractProperties(fromText("override val x: Int = 1"))
        properties shouldContainExactly
            listOf(
                Property(
                    "x",
                    Type("Int"),
                    PropertyModifier.VAL,
                    memberModifier = listOf(MemberModifier.OVERRIDE),
                ),
            )
    }

    "extractProperty should handle lateinit" {
        val properties = extractProperties(fromText("lateinit var x: Int"))
        properties shouldContainExactly
            listOf(
                Property(
                    "x",
                    Type("Int"),
                    PropertyModifier.VAR,
                    memberModifier = listOf(MemberModifier.LATE_INIT),
                ),
            )
    }

    "extractProperties should extract all properties" {
        val properties = extractProperties(fromText("val x: Int = 1; var y: String = \"2\""))
        properties shouldContainExactlyInAnyOrder
            listOf(
                Property("x", Type("Int"), PropertyModifier.VAL),
                Property("y", Type("String"), PropertyModifier.VAR),
            )
    }

    "extractProperty should handle generic type List<String>" {
        val properties = extractProperties(fromText("val x: List<String> = listOf()"))
        properties shouldContainExactlyInAnyOrder
            listOf(
                Property(
                    "x",
                    Type("List<String>"),
                    PropertyModifier.VAL,
                ),
            )
    }

    "extractProperty should handle generic type with multiple arguments" {
        val properties = extractProperties(fromText("val x: Map<String, Int>"))
        properties shouldContainExactlyInAnyOrder
            listOf(
                Property(
                    "x",
                    Type("Map<String,Int>"),
                    PropertyModifier.VAL,
                ),
            )
    }

    "extractProperty should handle nested generic types" {
        val properties = extractProperties(fromText("val x: List<Map<String, List<Int>>> = listOf()"))
        properties shouldContainExactlyInAnyOrder
            listOf(
                Property(
                    "x",
                    Type("List<Map<String,List<Int>>>"),
                    PropertyModifier.VAL,
                ),
            )
    }
})

internal fun extractProperties(tree: KotlinParseTree): List<Property> =
    getDeclarations(tree)
        .filter { it.name == "propertyDeclaration" }
        .map { extractProperty(it) }
