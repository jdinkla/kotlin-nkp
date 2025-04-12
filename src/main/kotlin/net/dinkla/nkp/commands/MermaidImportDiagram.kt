package net.dinkla.nkp.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import net.dinkla.nkp.analysis.mermaidImportsFlowDiagram
import net.dinkla.nkp.analysis.save
import net.dinkla.nkp.domain.Files
import net.dinkla.nkp.utilities.loadFromJsonFile

class MermaidImportDiagram : CliktCommand() {
    override fun help(context: Context) = "Inheritance report"

    private val model by argument(
        help = "Path to the model file",
    ).file(mustExist = true, canBeDir = false, canBeFile = true)

    private val target by argument(
        help = "The output file",
    ).convert { java.io.File(it) }
        .optional()

    private val excludeOtherLibraries by option(help = "exclude other libraries").flag(default = false)

    override fun run() {
        val files: Files = Files.loadFromJsonFile(model.absolutePath)
        val diagram = mermaidImportsFlowDiagram(files, excludeOtherLibraries)
        if (target != null) {
            diagram.save(target!!)
        } else {
            echo(diagram)
        }
    }
}
