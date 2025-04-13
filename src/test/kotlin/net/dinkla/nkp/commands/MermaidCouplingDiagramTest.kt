package net.dinkla.nkp.commands

import com.github.ajalt.clikt.testing.test
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.string.shouldContain
import io.kotest.matchers.string.shouldStartWith

class MermaidCouplingDiagramTest :
    StringSpec({

        "should return an error if no arguments are given" {
            val result = MermaidCouplingDiagram().test("")
            result.statusCode shouldNotBe 0
        }

        "should return an error if first argument does not exist" {
            val result = MermaidCouplingDiagram().test("NOT_EXISTENT_723732")
            result.statusCode shouldNotBe 0
        }

        "should return a valid Mermaid diagram for a valid model file" {
            val result = MermaidCouplingDiagram().test("src/test/resources/model.json")
            result.statusCode shouldBe 0
            
            // Verify that the output is a valid Mermaid flowchart
            result.output shouldStartWith "flowchart TD"
            
            // Verify that the diagram contains style definitions
            result.output shouldContain "classDef stable"
            result.output shouldContain "classDef balanced"
            result.output shouldContain "classDef unstable"
            
            // Verify that the diagram contains the legend
            result.output shouldContain "subgraph Legend"
            result.output shouldContain "stable_legend"
            result.output shouldContain "balanced_legend"
            result.output shouldContain "unstable_legend"
            
            // Verify that the diagram contains at least one package node
            result.output shouldContain "pkg_"
            
            // Verify that the diagram contains instability metrics
            result.output shouldContain "I="
            result.output shouldContain "Ca="
            result.output shouldContain "Ce="
        }
    })
