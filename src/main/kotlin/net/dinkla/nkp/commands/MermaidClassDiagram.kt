package net.dinkla.nkp.commands

import net.dinkla.nkp.analysis.mermaidClassDiagram

class MermaidClassDiagram : AbstractCommand("Mermaid class diagram") {
    override fun run() {
        val project = loadProject()
        val diagram = mermaidClassDiagram(project)
        echo(diagram)
    }
}
