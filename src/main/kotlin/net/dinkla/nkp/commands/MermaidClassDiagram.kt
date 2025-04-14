package net.dinkla.nkp.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.types.file
import net.dinkla.nkp.analysis.mermaidClassDiagram
import net.dinkla.nkp.analysis.save
import net.dinkla.nkp.domain.Project
import net.dinkla.nkp.utilities.loadFromJsonFile

class MermaidClassDiagram : CliktCommand() {
    override fun help(context: Context) = "Mermaid class diagram (.mermaid or .html)"

    private val model by argument(
        help = "Path to the model file",
    ).file(mustExist = true, canBeDir = false, canBeFile = true)

    private val target by argument(
        help = "The output file",
    ).convert { java.io.File(it) }
        .optional()

    override fun run() {
        val project = loadFromJsonFile<Project>(model.absolutePath)
        val diagram = mermaidClassDiagram(project)
        if (target != null) {
            diagram.save(target!!)
        } else {
            echo(diagram)
        }
    }
}
