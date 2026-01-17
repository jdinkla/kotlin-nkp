package net.dinkla.nkp.benchmark

import io.kotest.core.annotation.Tags
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.longs.shouldBeLessThan
import io.kotest.matchers.shouldBe
import net.dinkla.nkp.parser.GrammarToolsParser
import net.dinkla.nkp.parser.PsiParser
import net.dinkla.nkp.utilities.getAllKotlinFiles
import java.io.File
import java.util.Locale
import kotlin.math.roundToLong

/**
 * Benchmark test comparing PSI parser vs Grammar-tools parser performance.
 * Run with: ./gradlew test --tests "net.dinkla.nkp.benchmark.ParserBenchmark"
 */
@Tags("benchmark")
class ParserBenchmark :
    StringSpec({
        val testDir = File("src/main/kotlin")
        val warmupIterations = 3
        val measureIterations = 5

        "PSI parser should be faster than Grammar-tools parser".config(enabled = testDir.exists()) {
            // Given
            val files = getAllKotlinFiles(testDir)
            require(files.isNotEmpty()) { "No Kotlin files found in $testDir" }

            val psiParser = PsiParser()
            val grammarParser = GrammarToolsParser()

            // Warmup PSI parser
            repeat(warmupIterations) {
                files.forEach { filePath ->
                    psiParser.parseFile(filePath, testDir.absolutePath)
                }
            }

            // Warmup Grammar parser
            repeat(warmupIterations) {
                files.forEach { filePath ->
                    grammarParser.parseFile(filePath, testDir.absolutePath)
                }
            }

            // Measure PSI parser
            val psiTimes = mutableListOf<Long>()
            repeat(measureIterations) {
                val start = System.currentTimeMillis()
                files.forEach { filePath ->
                    psiParser.parseFile(filePath, testDir.absolutePath)
                }
                psiTimes.add(System.currentTimeMillis() - start)
            }

            // Measure Grammar parser
            val grammarTimes = mutableListOf<Long>()
            repeat(measureIterations) {
                val start = System.currentTimeMillis()
                files.forEach { filePath ->
                    grammarParser.parseFile(filePath, testDir.absolutePath)
                }
                grammarTimes.add(System.currentTimeMillis() - start)
            }

            val avgPsiTime = psiTimes.average().roundToLong()
            val avgGrammarTime = grammarTimes.average().roundToLong()
            val speedup = avgGrammarTime.toDouble() / avgPsiTime.toDouble()

            println("============================================================")
            println("PARSER BENCHMARK RESULTS")
            println("============================================================")
            println("Files parsed: ${files.size}")
            println("PSI Parser:     ${avgPsiTime}ms (avg of $measureIterations iterations)")
            println("Grammar Parser: ${avgGrammarTime}ms (avg of $measureIterations iterations)")
            println("Speedup:        ${String.format(Locale.US, "%.1f", speedup)}x")
            println("============================================================")

            // Then: PSI should be faster
            avgPsiTime shouldBeLessThan avgGrammarTime
        }

        "benchmark single file parsing times".config(
            enabled = File("src/examples/kotlin/examples/HelloWorld.kt").exists(),
        ) {
            // Given
            val testFile = "src/examples/kotlin/examples/HelloWorld.kt"
            val psiParser = PsiParser()
            val grammarParser = GrammarToolsParser()
            val prefix = "src/examples/kotlin"

            // Warmup
            repeat(warmupIterations) {
                psiParser.parseFile(testFile, prefix)
                grammarParser.parseFile(testFile, prefix)
            }

            // Measure
            val psiTimes = mutableListOf<Long>()
            val grammarTimes = mutableListOf<Long>()

            repeat(measureIterations * 2) {
                var start = System.currentTimeMillis()
                psiParser.parseFile(testFile, prefix)
                psiTimes.add(System.currentTimeMillis() - start)

                start = System.currentTimeMillis()
                grammarParser.parseFile(testFile, prefix)
                grammarTimes.add(System.currentTimeMillis() - start)
            }

            val avgPsiTime = psiTimes.average().roundToLong()
            val avgGrammarTime = grammarTimes.average().roundToLong()
            val speedup =
                if (avgPsiTime > 0) avgGrammarTime.toDouble() / avgPsiTime.toDouble() else Double.MAX_VALUE

            println("============================================================")
            println("SINGLE FILE BENCHMARK ($testFile)")
            println("============================================================")
            println("PSI Parser:     ${avgPsiTime}ms")
            println("Grammar Parser: ${avgGrammarTime}ms")
            println("Speedup:        ${String.format(Locale.US, "%.1f", speedup)}x")
            println("============================================================")
        }

        "both parsers should successfully parse all files".config(enabled = testDir.exists()) {
            // Given
            val files = getAllKotlinFiles(testDir)
            val psiParser = PsiParser()
            val grammarParser = GrammarToolsParser()

            // When
            val psiResults = files.map { psiParser.parseFile(it, testDir.absolutePath) }
            val grammarResults = files.map { grammarParser.parseFile(it, testDir.absolutePath) }

            // Then
            val psiSuccessCount = psiResults.count { it.isSuccess }
            val grammarSuccessCount = grammarResults.count { it.isSuccess }

            println("PSI Parser:     $psiSuccessCount/${files.size} files parsed successfully")
            println("Grammar Parser: $grammarSuccessCount/${files.size} files parsed successfully")

            psiSuccessCount shouldBe files.size
            grammarSuccessCount shouldBe files.size
        }
    })
