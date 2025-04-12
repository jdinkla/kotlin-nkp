package net.dinkla.nkp

import com.github.ajalt.clikt.testing.test
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

class NkpTest :
    StringSpec({
        "Nkp command without subcommand does nothing" {
            val result = Nkp().test("")
            result.statusCode shouldBe 0
        }
    })
