package net.dinkla.kpnk.analysis

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import net.dinkla.kpnk.domain.ClassModifier
import net.dinkla.kpnk.domain.ClassParameter
import net.dinkla.kpnk.domain.ClassSignature
import net.dinkla.kpnk.domain.FunctionSignature
import net.dinkla.kpnk.domain.InheritanceModifier
import net.dinkla.kpnk.domain.PackageName
import net.dinkla.kpnk.domain.Property
import net.dinkla.kpnk.domain.Type
import net.dinkla.kpnk.domain.TypeAlias
import net.dinkla.kpnk.domain.VisibilityModifier

class ClassStatisticsTest : StringSpec({
    "should return the statistics for a class" {
        val classSignature = ClassSignature(
            "C",
            listOf(ClassParameter("p", Type("Int")), ClassParameter("q", Type("Int"))),
            listOf("Interface1", "Interface2", "Interface3"),
            VisibilityModifier.INTERNAL,
            classModifier = ClassModifier.DATA,
            inheritanceModifier = InheritanceModifier.OPEN,
            declarations = listOf(
                FunctionSignature("toString", Type("String"), listOf()),
                FunctionSignature("hashCode", Type("Int"), listOf()),
                Property("p", Type("Int")),
                TypeAlias("T", Type("Int")),
            ),
            elementType = ClassSignature.Type.CLASS,
        )
        val packageName = PackageName("P")
        val stats = ClassStatistics.from(packageName, classSignature)
        stats shouldBe ClassStatistics(
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
