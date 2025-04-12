package net.dinkla.nkp.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.Context
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.file
import com.github.ajalt.clikt.parameters.types.int
import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.outliers
import net.dinkla.nkp.domain.Files
import net.dinkla.nkp.utilities.loadFromJsonFile

private const val DEFAULT_NUMBER_OF_OUTLIERS = 10

class Outliers : CliktCommand() {
    override fun help(context: Context) = "Inheritance report"

    private val model by argument(
        help = "Path to the model file",
    ).file(mustExist = true, canBeDir = false, canBeFile = true)

    private val n by option(
        help = "Number of outliers to show",
    ).int().default(DEFAULT_NUMBER_OF_OUTLIERS)

    override fun run() {
        val files: Files = Files.loadFromJsonFile(model.absolutePath)
        echo(Json.encodeToString(outliers(files).take(n)))
    }
}
