package net.dinkla.nkp.gradle

import net.dinkla.nkp.gradle.tasks.NkpAnalyzeTask
import net.dinkla.nkp.gradle.tasks.NkpParseTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.register

/**
 * Gradle plugin for kotlin-nkp static analysis.
 *
 * Provides tasks for parsing Kotlin source code and generating
 * various analysis reports and diagrams.
 */
class NkpPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // Create main extension
        val extension = project.extensions.create<NkpExtension>("nkp")

        // Configure defaults
        extension.sourceDirs.convention(
            project.provider {
                val defaultSrc = project.file("src/main/kotlin")
                if (defaultSrc.exists()) listOf(defaultSrc) else emptyList()
            },
        )
        extension.outputDir.convention(project.layout.buildDirectory.dir("nkp"))

        // Configure report defaults
        configureReportDefaults(extension.reports)

        // Register parse task
        val parseTask =
            project.tasks.register<NkpParseTask>("nkpParse") {
                group = TASK_GROUP
                description = "Parse Kotlin source files and generate analysis model"
                sourceDirs.set(extension.sourceDirs)
                outputFile.set(extension.outputDir.file("model.json"))
            }

        // Register analyze task
        project.tasks.register<NkpAnalyzeTask>("nkpAnalyze") {
            group = TASK_GROUP
            description = "Run all configured NKP analyses"
            dependsOn(parseTask)
            modelFile.set(parseTask.flatMap { it.outputFile })
            outputDir.set(extension.outputDir)
            // Connect report properties from extension to task
            reports.classStatistics.set(extension.reports.classStatistics)
            reports.fileStatistics.set(extension.reports.fileStatistics)
            reports.packageStatistics.set(extension.reports.packageStatistics)
            reports.packageCoupling.set(extension.reports.packageCoupling)
            reports.packages.set(extension.reports.packages)
            reports.mermaidClassDiagram.set(extension.reports.mermaidClassDiagram)
            reports.mermaidImportDiagram.set(extension.reports.mermaidImportDiagram)
            reports.mermaidCouplingDiagram.set(extension.reports.mermaidCouplingDiagram)
            reports.includeAllLibraries.set(extension.reports.includeAllLibraries)
            reports.includePrivateDeclarations.set(extension.reports.includePrivateDeclarations)
        }

        // Register aggregate task
        project.tasks.register("nkp") {
            group = TASK_GROUP
            description = "Run NKP parsing and analysis"
            dependsOn("nkpAnalyze")
        }
    }

    private fun configureReportDefaults(reports: NkpReportExtension) {
        reports.classStatistics.convention(true)
        reports.fileStatistics.convention(true)
        reports.packageStatistics.convention(true)
        reports.packageCoupling.convention(true)
        reports.packages.convention(false)
        reports.mermaidClassDiagram.convention(true)
        reports.mermaidImportDiagram.convention(true)
        reports.mermaidCouplingDiagram.convention(true)
        reports.includeAllLibraries.convention(false)
        reports.includePrivateDeclarations.convention(false)
    }

    companion object {
        const val TASK_GROUP = "nkp analysis"
    }
}
