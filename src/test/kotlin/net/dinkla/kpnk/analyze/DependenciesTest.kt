package net.dinkla.kpnk.analyze

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.shouldBe
import net.dinkla.kpnk.FileName
import net.dinkla.kpnk.elements.FileInfo
import net.dinkla.kpnk.elements.FullyQualifiedName
import net.dinkla.kpnk.elements.Import
import net.dinkla.kpnk.elements.TopLevel

class DependenciesTest : StringSpec({
    "dependencies should return the packages and their imports" {
        val infos = listOf(
            FileInfo(
                FileName("src/test/resources/example/net/dinkla/kpnk/HelloWorld.kt"),
                TopLevel(
                    FullyQualifiedName("net.dinkla.kpnk"),
                    listOf(
                        Import(FullyQualifiedName("kotlin.math.max")),
                        Import(FullyQualifiedName("kotlin.math.min")),
                        Import(FullyQualifiedName("net.dinkla.kpnk.HelloWorld2")),
                    ),
                ),
            ),
            FileInfo(
                FileName("src/test/resources/example/net/dinkla/kpnk/HelloWorld2.kt"),
                TopLevel(
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
