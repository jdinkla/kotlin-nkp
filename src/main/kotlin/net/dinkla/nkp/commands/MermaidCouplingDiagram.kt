package net.dinkla.nkp.commands

import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import net.dinkla.nkp.analysis.MermaidCouplingDiagram
import net.dinkla.nkp.analysis.allImports
import net.dinkla.nkp.analysis.combinedReport
import net.dinkla.nkp.analysis.filteredImports
import net.dinkla.nkp.domain.Project
import net.dinkla.nkp.utilities.loadFromJsonFile

class MermaidCouplingDiagram : AbstractCommand("Generate a Mermaid coupling diagram from code analysis") {
    private val excludeOtherLibraries by option(help = "exclude other libraries").flag(default = false)

    override fun run() {
        val project = loadFromJsonFile<Project>(model)
        val imports =
            if (excludeOtherLibraries) {
                filteredImports(project)
            } else {
                allImports(project)
            }
        val report = combinedReport(imports)
        val diagram =
            MermaidCouplingDiagram(
                packages = report.packages,
                metrics = report.metrics,
            )
        echo(diagram.generate())
    }
}
