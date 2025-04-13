package net.dinkla.nkp.commands

import com.github.ajalt.clikt.testing.test
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain

class MermaidImportDiagramTest :
    StringSpec({

        "should return an error if no arguments are given" {
            val result = MermaidImportDiagram().test("")
            result.statusCode shouldNotBe 0
        }

        "should return an error if first argument does not exist" {
            val result = MermaidImportDiagram().test("NOT_EXISTENT_723732")
            result.statusCode shouldNotBe 0
        }

        "should return a result for a valid model file" {
            val result = MermaidImportDiagram().test("src/test/resources/model.json --exclude-other-libraries")
            result.statusCode shouldBe 0
            result.output shouldContain "net.dinkla.nkp.analysis --> net.dinkla.nkp.domain"
        }
    })
