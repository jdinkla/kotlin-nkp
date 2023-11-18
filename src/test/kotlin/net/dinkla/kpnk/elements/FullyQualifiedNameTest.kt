package net.dinkla.kpnk.elements

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class FullyQualifiedNameTest : StringSpec({
    "packageName should return the name of the package without the file" {
        FullyQualifiedName("kotlin.math.max").packageName shouldBe "kotlin.math"
    }
})
