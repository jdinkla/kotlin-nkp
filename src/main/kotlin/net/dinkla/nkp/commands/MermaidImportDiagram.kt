package net.dinkla.nkp.commands

import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import net.dinkla.nkp.analysis.mermaidImportsFlowDiagram
import net.dinkla.nkp.domain.Project
import net.dinkla.nkp.utilities.loadFromJsonFile

class MermaidImportDiagram : AbstractCommand("Mermaid import diagram") {
    private val includeAllLibraries by option(help = "include all libraries").flag(default = false)

    override fun run() {
        val project = loadFromJsonFile<Project>(model)
        val diagram = mermaidImportsFlowDiagram(project, !includeAllLibraries)
        echo(diagram)
    }
}
