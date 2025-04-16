package net.dinkla.nkp.commands

import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.clikt.parameters.types.int
import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.outliers
import net.dinkla.nkp.domain.Project
import net.dinkla.nkp.utilities.loadFromJsonFile

private const val DEFAULT_NUMBER_OF_OUTLIERS = 10

class OutlierReport : AbstractCommand("Outliers report") {
    private val n by option(
        help = "Number of outliers to show",
    ).int().default(DEFAULT_NUMBER_OF_OUTLIERS)

    override fun run() {
        val project = loadFromJsonFile<Project>(model)
        echo(Json.encodeToString(outliers(project).take(n)))
    }
}
