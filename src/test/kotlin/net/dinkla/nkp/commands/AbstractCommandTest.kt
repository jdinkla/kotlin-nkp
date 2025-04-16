package net.dinkla.nkp.commands

import com.github.ajalt.clikt.testing.test
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldStartWith

private class Command : AbstractCommand("some help") {
    override fun run() {
        echo("test")
    }
}

class AbstractCommandTest :
    StringSpec({
        "should return an error if no arguments are given" {
            val result = Command().test("")
            result.statusCode shouldNotBe 0
        }

        "should return an error if first argument does not exist" {
            val result = Command().test("NOT_EXISTENT_723732")
            result.statusCode shouldNotBe 0
        }

        "should return a result for a valid model file" {
            val result = Command().test("src/test/resources/model.json")
            result.statusCode shouldBe 0
            result.output shouldStartWith "test"
        }
    })
