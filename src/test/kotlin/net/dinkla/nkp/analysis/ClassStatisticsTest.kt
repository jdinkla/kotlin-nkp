package net.dinkla.nkp.analysis

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import net.dinkla.nkp.domain.ClassModifier
import net.dinkla.nkp.domain.ClassParameter
import net.dinkla.nkp.domain.ClassSignature
import net.dinkla.nkp.domain.FunctionSignature
import net.dinkla.nkp.domain.InheritanceModifier
import net.dinkla.nkp.domain.PackageName
import net.dinkla.nkp.domain.Property
import net.dinkla.nkp.domain.Type
import net.dinkla.nkp.domain.TypeAlias
import net.dinkla.nkp.domain.VisibilityModifier

class ClassStatisticsTest :
    StringSpec({
        "should return the statistics for a class" {
            val classSignature =
                ClassSignature(
                    "C",
                    listOf(ClassParameter("p", Type("Int")), ClassParameter("q", Type("Int"))),
                    listOf("Interface1", "Interface2", "Interface3"),
                    VisibilityModifier.INTERNAL,
                    classModifier = ClassModifier.DATA,
                    inheritanceModifier = InheritanceModifier.OPEN,
                    declarations =
                        listOf(
                            FunctionSignature("toString", Type("String"), listOf()),
                            FunctionSignature("hashCode", Type("Int"), listOf()),
                            Property("p", Type("Int")),
                            TypeAlias("T", Type("Int")),
                        ),
                    elementType = ClassSignature.Type.CLASS,
                )
            val packageName = PackageName("P")
            val stats = ClassStatistic.from(packageName, classSignature)
            stats shouldBe
                ClassStatistic(
                    "C",
                    packageName,
                    2,
                    3,
                    VisibilityModifier.INTERNAL,
                    ClassSignature.Type.CLASS,
                    ClassModifier.DATA,
                    InheritanceModifier.OPEN,
                    4,
                    0,
                    2,
                    1,
                    1,
                )
        }
    })
