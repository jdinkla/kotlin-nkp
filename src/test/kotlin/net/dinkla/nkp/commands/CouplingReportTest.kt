package net.dinkla.nkp.commands

import com.github.ajalt.clikt.testing.test
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.CouplingReport
import kotlin.math.absoluteValue

class CouplingReportTest :
    StringSpec({
        "should return a result for a valid model file" {
            val result = CouplingReport().test("src/test/resources/model.json")
            result.statusCode shouldBe 0
            val report = Json.decodeFromString<CouplingReport>(result.output)
            report.packages shouldHaveAtLeastSize 1
            report.metrics shouldHaveAtLeastSize 1
        }

        "should return valid coupling metrics" {
            val result = CouplingReport().test("src/test/resources/model.json")
            result.statusCode shouldBe 0
            val report = Json.decodeFromString<CouplingReport>(result.output)

            // Verify that all packages have corresponding metrics
            report.packages.forEach { imports ->
                val metric = report.metrics.find { it.packageName == imports.packageName }
                metric shouldNotBe null
            }

            // Verify that efferent coupling values match imports count
            report.metrics.forEach { metric ->
                val imports = report.packages.find { it.packageName == metric.packageName }
                imports shouldNotBe null
                metric.efferentCoupling shouldBe imports!!.imports.size
            }

            // Verify instability calculation is correct
            report.metrics.forEach { metric ->
                val totalCoupling = metric.afferentCoupling + metric.efferentCoupling
                if (totalCoupling > 0) {
                    val expectedInstability = metric.efferentCoupling.toDouble() / totalCoupling
                    (metric.instability - expectedInstability).absoluteValue shouldBe 0.0
                } else {
                    metric.instability shouldBe 0.0
                }
            }
        }
    })
