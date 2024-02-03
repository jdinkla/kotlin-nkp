package net.dinkla.kpnk.analysis

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import net.dinkla.kpnk.domain.AnalysedFile
import net.dinkla.kpnk.domain.FileName
import net.dinkla.kpnk.domain.Files
import net.dinkla.kpnk.domain.Import
import net.dinkla.kpnk.domain.ImportedElement
import net.dinkla.kpnk.domain.PackageName

val elementMax = ImportedElement("kotlin.math.max")
val elementMin = ImportedElement("kotlin.math.min")
val elementHello = ImportedElement("net.dinkla.kpnk.HelloWorld2")
const val PATH1 = "src/test/resources/example/net/kpnk"
const val PATH2 = "src/test/resources/example/net/xyz"

class DependenciesTest : StringSpec({
    "dependencies should return the packages and their imports" {
        val infos =
            Files(
                listOf(
                    AnalysedFile(
                        FileName("$PATH1/HelloWorld.kt"),
                        PackageName("net.dinkla.kpnk"),
                        listOf(
                            Import(elementMax),
                            Import(elementMin),
                        ),
                    ),
                    AnalysedFile(
                        FileName("$PATH1/HelloWorld2.kt"),
                        PackageName("net.dinkla.kpnk"),
                        listOf(
                            Import(elementHello),
                        ),
                    ),
                    AnalysedFile(
                        FileName("$PATH2/HelloWorld3.kt"),
                        PackageName("net.dinkla.xyz"),
                        listOf(
                            Import(elementHello),
                        ),
                    ),
                ),
            )
        val dependencies = Dependencies.from(infos)
        dependencies.dependencies.size shouldBe 2
        dependencies.dependencies shouldContain
            Dependency(
                PackageName("net.dinkla.kpnk"),
                setOf(elementMax, elementMin, elementHello),
            )
        dependencies.dependencies shouldContain
            Dependency(
                PackageName("net.dinkla.xyz"),
                setOf(elementHello),
            )
    }
})
