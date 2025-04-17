package net.dinkla.nkp.analysis

import io.github.oshai.kotlinlogging.KotlinLogging
import net.dinkla.nkp.domain.PackageName

private val logger = KotlinLogging.logger {}

private const val METRIC_INSTABILITY_LOWER_BOUND = 0.3

private const val METRIC_INSTABILITY_UPPER_BOUND = 0.7

class MermaidCouplingDiagram(
    private val packages: List<Imports>,
    private val metrics: List<PackageCoupling>,
) {
    fun generate(): String {
        logger.debug { "Generating Mermaid coupling diagram" }

        val sb = StringBuilder()
        sb.appendLine("flowchart TD")
        sb.appendLine("    %% Package nodes with instability metrics")

        // Add style classes
        sb.appendLine("    classDef stable fill:#ccffcc,stroke:#333,stroke-width:2px")
        sb.appendLine("    classDef balanced fill:#ffffcc,stroke:#333,stroke-width:1px")
        sb.appendLine("    classDef unstable fill:#ffcccc,stroke:#333,stroke-width:1px")

        // Create nodes with coupling information
        metrics.forEach { metric ->
            val nodeId = packageToNodeId(metric.packageName)
            val coupling = metric.coupling
            val label = "${metric.packageName.name}\\nI=${formatDouble(
                coupling.instability,
            )}\\nCa=${coupling.afferentCoupling}, Ce=${coupling.efferentCoupling}"
            sb.appendLine("    $nodeId[\"$label\"]")

            // Apply styling based on instability
            val styleClass =
                when {
                    metric.coupling.instability < METRIC_INSTABILITY_LOWER_BOUND -> "stable"
                    metric.coupling.instability > METRIC_INSTABILITY_UPPER_BOUND -> "unstable"
                    else -> "balanced"
                }
            sb.appendLine("    $nodeId:::$styleClass")
        }

        // Add dependencies with varying line thickness based on importance
        sb.appendLine("\n    %% Dependencies")
        packages.forEach { pkg ->
            val sourceId = packageToNodeId(pkg.packageName)

            pkg.imports.forEach { imported ->
                // Only draw edges between packages in our metrics list
                if (metrics.any { it.packageName == imported }) {
                    val targetId = packageToNodeId(imported)
                    sb.appendLine("    $sourceId --> $targetId")
                }
            }
        }

        // Add legend
        sb.appendLine("\n    %% Legend")
        sb.appendLine("    subgraph Legend")
        sb.appendLine("        stable_legend[\"Stable (I < 0.3)\"]:::stable")
        sb.appendLine("        balanced_legend[\"Balanced\"]:::balanced")
        sb.appendLine("        unstable_legend[\"Unstable (I > 0.7)\"]:::unstable")
        sb.appendLine("    end")

        return sb.toString()
    }

    private fun packageToNodeId(packageName: PackageName): String = "pkg_" + packageName.name.replace(".", "_")

    private fun formatDouble(value: Double): String = "%.2f".format(value)
}
