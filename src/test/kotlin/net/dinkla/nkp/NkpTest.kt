package net.dinkla.nkp

import com.github.ajalt.clikt.testing.test
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class NkpTest :
    StringSpec({
        "Nkp command without subcommand should do nothing" {
            val result = Nkp().test("")
            result.statusCode shouldBe 0
        }

        "should show version with --version flag" {
            val result = Nkp().test("--version")
            result.statusCode shouldBe 0
            result.output shouldContain "0.1"
        }

        "should show version with -v flag" {
            val result = Nkp().test("-v")
            result.statusCode shouldBe 0
            result.output shouldContain "0.1"
        }
    })
