package net.dinkla.nkp.report

/**
 * Configuration options for HTML report generation.
 */
data class ReportOptions(
    val title: String = "Kotlin Analysis Report",
    val includeAllLibraries: Boolean = false,
    val includePrivateDeclarations: Boolean = false,
)
