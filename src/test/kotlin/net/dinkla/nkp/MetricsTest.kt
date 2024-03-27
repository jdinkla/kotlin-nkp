package net.dinkla.nkp

import com.tngtech.archunit.core.domain.JavaClass
import com.tngtech.archunit.core.domain.JavaClasses
import com.tngtech.archunit.core.domain.JavaPackage
import com.tngtech.archunit.core.importer.ImportOption.DoNotIncludeTests
import com.tngtech.archunit.junit.AnalyzeClasses
import com.tngtech.archunit.junit.ArchTest
import com.tngtech.archunit.library.metrics.ArchitectureMetrics
import com.tngtech.archunit.library.metrics.MetricsComponents

// see https://www.archunit.org/userguide/html/000_Index.html#_software_architecture_metrics

@AnalyzeClasses(
    packagesOf = [MetricsTest::class],
    importOptions = [DoNotIncludeTests::class],
)
@Suppress("ktlint:standard:function-naming")
class MetricsTest {
    private val base = "net.dinkla.nkp"

    @ArchTest
    fun `Cumulative Dependency Metrics by John Lakos`(importedClasses: JavaClasses) {
        val packages: Set<JavaPackage> = importedClasses.getPackage(base).getSubpackages()
        val components: MetricsComponents<JavaClass> = MetricsComponents.fromPackages(packages)
        val metrics = ArchitectureMetrics.lakosMetrics(components)
        println("Cumulative Dependency Metrics by John Lakos")
        println("CCD: " + metrics.getCumulativeComponentDependency())
        println("ACD: " + metrics.getAverageComponentDependency())
        println("RACD: " + metrics.getRelativeAverageComponentDependency())
        println("NCCD: " + metrics.getNormalizedCumulativeComponentDependency())
    }

    @ArchTest
    fun `Component Dependency Metrics by Robert C Martin`(importedClasses: JavaClasses) {
        val packages: Set<JavaPackage> = importedClasses.getPackage(base).getSubpackages()
        val components: MetricsComponents<JavaClass> = MetricsComponents.fromPackages(packages)
        val metrics = ArchitectureMetrics.componentDependencyMetrics(components)
        println("Component Dependency Metrics by Robert C Martin")
        for (component in packages.map { it.relativeName }) {
            val fqComponent = "$base.$component"
            println("Component: $component")
            println("Ce: " + metrics.getEfferentCoupling(fqComponent))
            println("Ca: " + metrics.getAfferentCoupling(fqComponent))
            println("I: " + metrics.getInstability(fqComponent))
            println("A: " + metrics.getAbstractness(fqComponent))
            println("D: " + metrics.getNormalizedDistanceFromMainSequence(fqComponent))
        }
    }

    @ArchTest
    fun `Visibility Metrics by Herbert Dowalil`(importedClasses: JavaClasses) {
        val packages: Set<JavaPackage> = importedClasses.getPackage(base).getSubpackages()
        val components: MetricsComponents<JavaClass> = MetricsComponents.fromPackages(packages)
        val metrics = ArchitectureMetrics.visibilityMetrics(components)
        println("Visibility Metrics by Herbert Dowalil")
        for (component in packages.map { it.relativeName }) {
            val fqComponent = "$base.$component"
            println("Component: $component")
            println("RV : " + metrics.getRelativeVisibility(fqComponent))
        }
        println("ARV: " + metrics.getAverageRelativeVisibility())
        println("GRV: " + metrics.getGlobalRelativeVisibility())
    }
}
