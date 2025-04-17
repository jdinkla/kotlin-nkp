package net.dinkla.nkp.analysis

import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.PackageName
import net.dinkla.nkp.domain.statistics.Coupling

fun coupling(importsList: List<Imports>): List<PackageCoupling> =
    importsList.map { imports ->
        val packageName = imports.packageName
        val efferentCoupling = imports.imports.size
        val afferentCoupling =
            importsList.count { other ->
                other.packageName != packageName && other.imports.contains(packageName)
            }
        PackageCoupling(
            packageName = packageName,
            coupling = Coupling(afferentCoupling, efferentCoupling),
        )
    }

fun combinedReport(imports: List<Imports>): CouplingReport =
    CouplingReport(
        packages = imports,
        metrics = coupling(imports),
    )

@Serializable
class PackageCoupling(
    val packageName: PackageName,
    val coupling: Coupling,
)

@Serializable
data class CouplingReport(
    val packages: List<Imports>,
    val metrics: List<PackageCoupling>,
)
