package net.dinkla.kpnk.extract

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import net.dinkla.kpnk.domain.MemberModifier
import net.dinkla.kpnk.domain.Property
import net.dinkla.kpnk.domain.PropertyModifier
import net.dinkla.kpnk.domain.VisibilityModifier
import net.dinkla.kpnk.utilities.fromText
import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree

class ExtractPropertyTest : StringSpec({

    "extractProperty should handle one val property" {
        val properties = extractProperties(fromText("val x: Int = 1"))
        properties shouldContainExactlyInAnyOrder listOf(Property("x", "Int", PropertyModifier.VAL))
    }

    "extractProperty should handle one var property" {
        val properties = extractProperties(fromText("var x: Int = 1"))
        properties shouldContainExactlyInAnyOrder listOf(Property("x", "Int", PropertyModifier.VAR))
    }

    "extractProperty should handle a const val property" {
        val properties = extractProperties(fromText("const val x: Int = 1"))
        properties shouldContainExactlyInAnyOrder listOf(Property("x", "Int", PropertyModifier.CONST_VAL))
    }

    "extractProperty should handle a constructor call with explicit type" {
        val properties = extractProperties(fromText("val x : C = C(1)"))
        properties shouldContainExactlyInAnyOrder listOf(Property("x", "C", PropertyModifier.VAL))
    }

    "extractProperty should handle a constructor call with implicit type" {
        val properties = extractProperties(fromText("val x = C(1)"))
        properties shouldContainExactlyInAnyOrder listOf(Property("x", null, PropertyModifier.VAL))
    }

    "extractProperty should handle visibility modifier private" {
        val properties = extractProperties(fromText("private val x: Int = 1"))
        properties shouldContainExactly listOf(
            Property(
                "x",
                "Int",
                PropertyModifier.VAL,
                VisibilityModifier.PRIVATE,
            ),
        )
    }
    "extractProperty should handle overriden property" {
        val properties = extractProperties(fromText("override val x: Int = 1"))
        properties shouldContainExactly listOf(
            Property(
                "x",
                "Int",
                PropertyModifier.VAL,
                memberModifier = MemberModifier.OVERRIDE,
            ),
        )
    }

    "extractProperties should extract all properties" {
        val properties = extractProperties(fromText("val x: Int = 1; var y: String = \"2\""))
        properties shouldContainExactlyInAnyOrder listOf(
            Property("x", "Int", PropertyModifier.VAL),
            Property("y", "String", PropertyModifier.VAR),
        )
    }
})

internal fun extractProperties(tree: KotlinParseTree): List<Property> =
    getDeclarations(tree)
        .filter { it.name == "propertyDeclaration" }
        .map { extractProperty(it) }
