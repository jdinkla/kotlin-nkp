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

    "fileNameWithoutDirectory should return the file name without the directory" {
        fileNameWithoutDirectory("a", "a/b/c") shouldBe "b/c"
    }

    "fileNameWithoutDirectory should return the file name without the directory 2" {
        fileNameWithoutDirectory("a/b/c", "a/b/c/d") shouldBe "d"
    }
})
