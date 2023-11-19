package net.dinkla.kpnk.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PropertyTest : StringSpec({
    "prettyPrint should handle internal val" {
        val property = property.copy(
            dataType = "String",
            modifier = PropertyModifier.VAL,
            visibilityModifier = VisibilityModifier.INTERNAL,
        )
        property.prettyPrint() shouldBe "internal val name : String"
    }
    "prettyPrint should handle val" {
        val property = property.copy(
            dataType = "String",
            modifier = PropertyModifier.CONST_VAL,
        )
        property.prettyPrint() shouldBe "const val name : String"
    }
    "prettyPrint should handle implicit type" {
        val property = property.copy(
            modifier = PropertyModifier.VAR,
        )
        property.prettyPrint() shouldBe "var name"
    }
    "prettyPrint should handle override" {
        val property = property.copy(
            modifier = PropertyModifier.VAR,
            memberModifier = MemberModifier.OVERRIDE,
        )
        property.prettyPrint() shouldBe "override var name"
    }
})

private val property = Property(
    "name",
)
