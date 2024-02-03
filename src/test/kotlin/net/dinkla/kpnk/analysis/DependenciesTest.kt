package net.dinkla.kpnk.analysis

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import net.dinkla.kpnk.domain.FileInfo
import net.dinkla.kpnk.domain.FileName
import net.dinkla.kpnk.domain.FullyQualifiedName
import net.dinkla.kpnk.domain.Import
import net.dinkla.kpnk.domain.TopLevel

class DependenciesTest : StringSpec({
    "dependencies should return the packages and their imports" {
        val fileName1 = FileName("src/test/resources/example/net/dinkla/kpnk/HelloWorld.kt")
        val fileName2 = FileName("src/test/resources/example/net/dinkla/kpnk/HelloWorld2.kt")
        val infos =
            listOf(
                FileInfo(
                    fileName1,
                    TopLevel(
                        fileName1,
                        FullyQualifiedName("net.dinkla.kpnk"),
                        listOf(
                            Import(FullyQualifiedName("kotlin.math.max")),
                            Import(FullyQualifiedName("kotlin.math.min")),
                            Import(FullyQualifiedName("net.dinkla.kpnk.HelloWorld2")),
                        ),
                    ),
                ),
                FileInfo(
                    fileName2,
                    TopLevel(
                        fileName2,
                        FullyQualifiedName("net.dinkla.kpnk"),
                        listOf(Import(FullyQualifiedName("kotlin.math.min"))),
                    ),
                ),
            )
        val deps = dependencies(infos)
        deps.size shouldBe 2
        println(deps)
        deps["net.dinkla.kpnk.HelloWorld"] shouldContainExactly setOf("kotlin.math", "net.dinkla.kpnk")
        deps["net.dinkla.kpnk.HelloWorld2"] shouldBe setOf("kotlin.math")
    }
})
