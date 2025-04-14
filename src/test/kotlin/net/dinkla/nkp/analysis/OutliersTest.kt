package net.dinkla.nkp.analysis

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import net.dinkla.nkp.domain.ClassSignature
import net.dinkla.nkp.domain.FileName
import net.dinkla.nkp.domain.FunctionSignature
import net.dinkla.nkp.domain.KotlinFile
import net.dinkla.nkp.domain.PackageName
import net.dinkla.nkp.domain.Property
import net.dinkla.nkp.domain.Type

class OutliersTest :
    StringSpec({
        "largeClasses should return the top 2 classes with most functions and properties" {
            val info1 = info(listOf(cls(1), cls(3), cls(5)))
            val info2 = info(listOf(cls(4), cls(2)))
            val largeClasses = largeClasses(listOf(info1, info2)).take(2)
            largeClasses.size shouldBe 2
            largeClasses[0] shouldBe cls(5)
            largeClasses[1] shouldBe cls(4)
        }
    })

private fun info(classSignatures: List<ClassSignature>) =
    KotlinFile(
        FileName("a"),
        PackageName("name"),
        declarations = classSignatures,
    )

private fun cls(numElems: Int) =
    ClassSignature(
        "A",
        declarations =
            (1..numElems).map {
                FunctionSignature(it.toString())
            } +
                (1..numElems).map {
                    Property(
                        it.toString(),
                        Type("String"),
                    )
                },
    )
