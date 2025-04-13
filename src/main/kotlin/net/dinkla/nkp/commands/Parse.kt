package net.dinkla.nkp.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.convert
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.types.file
import kotlinx.serialization.json.Json
import net.dinkla.nkp.domain.Files
import net.dinkla.nkp.utilities.readFromDirectory

class Parse : CliktCommand(name = "parse") {
    override fun help(context: Context) = "Parse a source directory and generate a model file."

    private val source by argument(
        help = "Path to the source directory to analyze",
    ).file(mustExist = true, canBeDir = true, canBeFile = false)

    private val target by argument(
        help = "The output file",
    ).convert { java.io.File(it) }
        .optional()

    override fun run() {
        val files: Files = Files.readFromDirectory(source)
        val json = Json.encodeToString(files)
        if (target != null) {
            target!!.writeText(json)
        } else {
            echo(json)
        }
    }
}
