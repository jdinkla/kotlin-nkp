package net.dinkla.nkp.commands

import com.github.ajalt.clikt.testing.test
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain

class MermaidImportDiagramTest :
    StringSpec({
        "should return a result for a valid model file" {
            val result = MermaidImportDiagram().test("src/test/resources/model.json --exclude-other-libraries")
            result.statusCode shouldBe 0
            result.output shouldContain "net.dinkla.nkp.analysis --> net.dinkla.nkp.domain"
        }
    })
