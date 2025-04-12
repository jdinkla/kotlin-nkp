package net.dinkla.nkp.analysis

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import net.dinkla.nkp.domain.ClassParameter
import net.dinkla.nkp.domain.FunctionParameter
import net.dinkla.nkp.domain.FunctionSignature
import net.dinkla.nkp.domain.MemberModifier
import net.dinkla.nkp.domain.Property
import net.dinkla.nkp.domain.PropertyModifier
import net.dinkla.nkp.domain.Type
import net.dinkla.nkp.domain.VisibilityModifier

class MermaidClassDiagramKtTest :
    StringSpec({
        "a function signature in mermaid format" {
            val functionSignature =
                FunctionSignature(
                    name = "foo",
                    returnType = Type("List<Int>"),
                    parameters =
                        listOf(
                            FunctionParameter("a", Type("Int")),
                            FunctionParameter("b", Type("String")),
                        ),
                    visibilityModifier = VisibilityModifier.PUBLIC,
                    memberModifier = MemberModifier.OVERRIDE,
                )
            functionSignature.mermaid() shouldBe Pair("+", "foo(a: Int, b: String): List‹Int› «override»")
        }

        "a property in mermaid format" {
            val property =
                Property(
                    name = "bar",
                    dataType = Type("String"),
                    visibilityModifier = VisibilityModifier.PRIVATE,
                )
            property.mermaid() shouldBe Pair("-", "bar: String «val»")
        }

        "a parameter in mermaid format" {
            val parameter =
                ClassParameter(
                    name = "a",
                    type = Type("List<Int>"),
                    propertyModifier = PropertyModifier.VAL,
                )
            parameter.mermaid() shouldBe Pair("+", "a: List‹Int› «val»")
        }
    })
