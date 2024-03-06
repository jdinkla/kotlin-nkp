package net.dinkla.nkp

import com.github.ajalt.clikt.testing.test
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe

class MainTest : StringSpec({
    "main command should return a statuscode not zero if no arguments are given" {
        val result = Main().test("")
        result.statusCode shouldNotBe 0
    }

    "main command should return a statuscode zero if a source file is given" {
        val result = Main().test("src/test/resources")
        result.statusCode shouldBe 0
    }

    "main should save json file" {
        val tempFile = kotlin.io.path.createTempFile("nkp", ".json")
        val result = Main().test("src/test/resources --save ${tempFile.toAbsolutePath()}")
        result.statusCode shouldBe 0
        try {
            tempFile.toFile().delete()
        } finally {
            // ignore
        }
    }
})
