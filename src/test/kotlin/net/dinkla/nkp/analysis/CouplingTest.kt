package net.dinkla.nkp.analysis

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import net.dinkla.nkp.domain.PackageName

class CouplingTest :
    StringSpec({

        "should calculate correct afferent coupling (Ca)" {
            // Arrange
            val packages =
                listOf(
                    Imports(PackageName("pkg.a"), setOf(PackageName("pkg.b"), PackageName("pkg.c"))),
                    Imports(PackageName("pkg.b"), setOf(PackageName("pkg.c"))),
                    Imports(PackageName("pkg.c"), setOf()),
                )

            // Act
            val metrics = coupling(packages)

            // Assert
            val pkgA = metrics.find { it.packageName.name == "pkg.a" }!!
            val pkgB = metrics.find { it.packageName.name == "pkg.b" }!!
            val pkgC = metrics.find { it.packageName.name == "pkg.c" }!!

            pkgA.afferentCoupling shouldBe 0 // No packages depend on A
            pkgB.afferentCoupling shouldBe 1 // A depends on B
            pkgC.afferentCoupling shouldBe 2 // A and B depend on C
        }

        "should calculate correct efferent coupling (Ce)" {
            // Arrange
            val packages =
                listOf(
                    Imports(PackageName("pkg.a"), setOf(PackageName("pkg.b"), PackageName("pkg.c"))),
                    Imports(PackageName("pkg.b"), setOf(PackageName("pkg.c"))),
                    Imports(PackageName("pkg.c"), setOf()),
                )

            // Act
            val metrics = coupling(packages)

            // Assert
            val pkgA = metrics.find { it.packageName.name == "pkg.a" }!!
            val pkgB = metrics.find { it.packageName.name == "pkg.b" }!!
            val pkgC = metrics.find { it.packageName.name == "pkg.c" }!!

            pkgA.efferentCoupling shouldBe 2 // A depends on B and C
            pkgB.efferentCoupling shouldBe 1 // B depends on C
            pkgC.efferentCoupling shouldBe 0 // C depends on nothing
        }

        "should calculate correct instability (I)" {
            // Arrange
            val packages =
                listOf(
                    Imports(PackageName("pkg.a"), setOf(PackageName("pkg.b"), PackageName("pkg.c"))),
                    Imports(PackageName("pkg.b"), setOf(PackageName("pkg.c"))),
                    Imports(PackageName("pkg.c"), setOf()),
                )

            // Act
            val metrics = coupling(packages)

            // Assert
            val pkgA = metrics.find { it.packageName.name == "pkg.a" }!!
            val pkgB = metrics.find { it.packageName.name == "pkg.b" }!!
            val pkgC = metrics.find { it.packageName.name == "pkg.c" }!!

            // A: Ce=2, Ca=0, I=2/(2+0)=1.0
            pkgA.instability shouldBe 1.0

            // B: Ce=1, Ca=1, I=1/(1+1)=0.5
            pkgB.instability shouldBe 0.5

            // C: Ce=0, Ca=2, I=0/(0+2)=0.0
            pkgC.instability shouldBe 0.0
        }

        "should handle empty imports correctly" {
            // Arrange
            val packages =
                listOf(
                    Imports(PackageName("pkg.a"), setOf()),
                    Imports(PackageName("pkg.b"), setOf()),
                )

            // Act
            val metrics = coupling(packages)

            // Assert
            metrics.size shouldBe 2
            metrics.forEach {
                it.efferentCoupling shouldBe 0
                it.afferentCoupling shouldBe 0
                it.instability shouldBe 0.0
            }
        }

        "should handle self-references correctly" {
            // Arrange - package referring to itself shouldn't count in coupling
            val packages =
                listOf(
                    Imports(PackageName("pkg.a"), setOf(PackageName("pkg.a"))),
                )

            // Act
            val metrics = coupling(packages)

            // Assert
            metrics.size shouldBe 1
            metrics[0].efferentCoupling shouldBe 1 // Shows self-reference in imports list
            metrics[0].afferentCoupling shouldBe 0 // But not in afferent coupling from others
            metrics[0].instability shouldBe 1.0
        }
    })
