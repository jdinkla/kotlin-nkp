package net.dinkla.nkp.commands

import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import net.dinkla.nkp.analysis.MermaidCouplingDiagram
import net.dinkla.nkp.analysis.PackageImports
import net.dinkla.nkp.analysis.combinedReport
import net.dinkla.nkp.domain.kotlinlang.Project
import net.dinkla.nkp.utilities.loadJson

class MermaidCouplingDiagram : AbstractCommand("Generate a Mermaid coupling diagram from code analysis") {
    private val includeAllLibraries by option(help = "include all libraries").flag(default = false)

    override fun run() {
        val project = model.loadJson<Project>()
        val imports =
            if (!includeAllLibraries) {
                PackageImports.filteredImports(project)
            } else {
                PackageImports.allImports(project)
            }
        val reportItems = combinedReport(imports)
        val diagram = MermaidCouplingDiagram(items = reportItems)
        echo(diagram.generate())
    }
}
