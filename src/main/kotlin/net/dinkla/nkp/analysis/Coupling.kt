package net.dinkla.nkp.analysis

import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.Files
import net.dinkla.nkp.domain.PackageName

fun coupling(importsList: List<Imports>): List<PackageCoupling> =
    importsList.map { imports ->
        val packageName = imports.packageName
        val efferentCoupling = imports.imports.size
        val afferentCoupling =
            importsList.count { other ->
                other.packageName != packageName && other.imports.contains(packageName)
            }

        val totalCoupling = efferentCoupling + afferentCoupling
        val instability = if (totalCoupling > 0) efferentCoupling.toDouble() / totalCoupling else 0.0

        PackageCoupling(
            packageName = packageName,
            afferentCoupling = afferentCoupling,
            efferentCoupling = efferentCoupling,
            instability = instability,
        )
    }

fun combinedReport(
    files: Files,
    excludeOtherLibraries: Boolean,
): CouplingReport {
    val importsList = imports(files, excludeOtherLibraries)
    val couplingMetrics = coupling(importsList)

    return CouplingReport(
        packages = importsList,
        metrics = couplingMetrics,
    )
}

@Serializable
data class PackageCoupling(
    val packageName: PackageName,
    // Ca - packages depending on this package
    val afferentCoupling: Int,
    // Ce - packages this package depends on
    val efferentCoupling: Int,
    // I = Ce / (Ce + Ca)
    val instability: Double,
)

@Serializable
data class CouplingReport(
    val packages: List<Imports>,
    val metrics: List<PackageCoupling>,
)
