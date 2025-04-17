package net.dinkla.nkp.commands

import net.dinkla.nkp.analysis.mermaidClassDiagram
import net.dinkla.nkp.domain.kotlinlang.Project
import net.dinkla.nkp.utilities.loadFromJsonFile

class MermaidClassDiagram : AbstractCommand("Mermaid class diagram") {
    override fun run() {
        val project = loadFromJsonFile<Project>(model)
        val diagram = mermaidClassDiagram(project)
        echo(diagram)
    }
}
