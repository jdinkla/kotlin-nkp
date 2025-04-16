package net.dinkla.nkp.commands

import com.github.ajalt.clikt.testing.test
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveAtLeastSize
import io.kotest.matchers.shouldBe
import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.ClassInheritance

class ClassInheritanceTest :
    StringSpec({
        "should return a result for a valid model file" {
            val result = ClassInheritance().test("src/test/resources/model.json")
            result.statusCode shouldBe 0
            val classInheritance = Json.decodeFromString<List<ClassInheritance>>(result.output)
            classInheritance shouldHaveAtLeastSize 1
        }
    })
