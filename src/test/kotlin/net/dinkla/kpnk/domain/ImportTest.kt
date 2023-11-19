package net.dinkla.kpnk.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class ImportTest : StringSpec({
    "packageName() should return the name of the package the element is contained in" {
        Import(FullyQualifiedName("kotlin.math.max")).packageName shouldBe "kotlin.math"
    }
})
