package net.dinkla.kpnk.analysis

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import net.dinkla.kpnk.domain.ClassParameter
import net.dinkla.kpnk.domain.FunctionParameter
import net.dinkla.kpnk.domain.FunctionSignature
import net.dinkla.kpnk.domain.MemberModifier
import net.dinkla.kpnk.domain.Property
import net.dinkla.kpnk.domain.PropertyModifier
import net.dinkla.kpnk.domain.Type
import net.dinkla.kpnk.domain.VisibilityModifier

class MermaidClassDiagramKtTest : StringSpec({
    "a function signature in mermaid format" {
        val functionSignature = FunctionSignature(
            name = "foo",
            returnType = Type("List<Int>"),
            parameters = listOf(
                FunctionParameter("a", Type("Int")),
                FunctionParameter("b", Type("String"))
            ),
            visibilityModifier = VisibilityModifier.PUBLIC,
            memberModifier = MemberModifier.OVERRIDE
        )
        functionSignature.mermaid() shouldBe Pair("+", "foo(a: Int, b: String): List‹Int› «override»")
    }

    "a property in mermaid format" {
        val property = Property(
            name = "bar",
            dataType = Type("String"),
            visibilityModifier = VisibilityModifier.PRIVATE,
        )
        property.mermaid() shouldBe Pair("-", "bar: String «val»")
    }

    "a parameter in mermaid format" {
        val parameter = ClassParameter(
            name = "a",
            type = Type("List<Int>"),
            propertyModifier = PropertyModifier.VAL
        )
        parameter.mermaid() shouldBe Pair("+", "a: List‹Int› «val»")
    }
})
