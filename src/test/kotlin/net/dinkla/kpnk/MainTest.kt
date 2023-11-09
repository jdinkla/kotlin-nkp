package net.dinkla.kpnk

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class MainTest : StringSpec({
    "parseArgs should return null if no argument present" {
        parseArgs(arrayOf()) shouldBe null
    }

    "parseArgs should return null if more than one argument" {
        parseArgs(arrayOf("a", "b")) shouldBe null
    }

    "parseArgs should return the singleton argument" {
        parseArgs(arrayOf("a")) shouldBe "a"
    }
})
