package net.dinkla.kpnk.elements

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class PropertyModifierTest : StringSpec({
    "create should create the correct modifier" {
        PropertyModifier.create(false, false) shouldBe PropertyModifier.VAL
        PropertyModifier.create(false, true) shouldBe PropertyModifier.VAR
        PropertyModifier.create(true, false) shouldBe PropertyModifier.CONST_VAL
    }
})
