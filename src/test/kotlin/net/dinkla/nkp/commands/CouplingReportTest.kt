package net.dinkla.nkp.commands

import com.github.ajalt.clikt.testing.test
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.CouplingReport
import kotlin.math.absoluteValue

class CouplingReportTest :
    StringSpec({
        "should return a result for a valid model file" {
            val result = CouplingReport().test("src/test/resources/model.json")
            result.statusCode shouldBe 0
            val report = Json.decodeFromString<CouplingReport>(result.output)
            report.items shouldHaveAtLeastSize 1
        }

        "all packages should have corresponding metrics" {
            val result = CouplingReport().test("src/test/resources/model.json")
            result.statusCode shouldBe 0
            val report = Json.decodeFromString<CouplingReport>(result.output)

            report.items.forEach { item ->
                item.coupling.packageName shouldBe item.imports.packageName
            }
        }

        "efferent coupling values should match imports count" {
            val result = CouplingReport().test("src/test/resources/model.json")
            result.statusCode shouldBe 0
            val report = Json.decodeFromString<CouplingReport>(result.output)

            report.items.forEach { item ->
                item.coupling.coupling.efferentCoupling shouldBe item.imports.imports.size
            }
        }

        "instability calculation should be correct" {
            val result = CouplingReport().test("src/test/resources/model.json")
            result.statusCode shouldBe 0
            val report = Json.decodeFromString<CouplingReport>(result.output)

            report.items.forEach { item ->
                val metric = item.coupling
                val totalCoupling = metric.coupling.afferentCoupling + metric.coupling.efferentCoupling
                if (totalCoupling > 0) {
                    val expectedInstability = metric.coupling.efferentCoupling.toDouble() / totalCoupling
                    (metric.coupling.instability - expectedInstability).absoluteValue shouldBe 0.0
                } else {
                    metric.coupling.instability shouldBe 0.0
                }
            }
        }
    })
