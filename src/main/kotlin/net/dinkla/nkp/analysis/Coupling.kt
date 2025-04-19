package net.dinkla.nkp.analysis

import kotlinx.serialization.Serializable
import net.dinkla.nkp.domain.kotlinlang.PackageName
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

fun combinedReport(imports: List<Imports>): CouplingReport {
    val couplings = coupling(imports)
    val reportItems =
        imports.map { import ->
            val matchingCoupling = couplings.find { it.packageName == import.packageName }
            check(matchingCoupling != null) { "No coupling found for package ${import.packageName}" }
            CouplingReportItem(
                imports = import,
                coupling = matchingCoupling,
            )
        }
    return CouplingReport(reportItems)
}

@Serializable
class PackageCoupling(
    val packageName: PackageName,
    val coupling: Coupling,
)

@Serializable
data class CouplingReportItem(
    val imports: Imports,
    val coupling: PackageCoupling,
)

@Serializable
data class CouplingReport(
    val items: List<CouplingReportItem>,
)
