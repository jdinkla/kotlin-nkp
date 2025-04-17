package net.dinkla.nkp.commands

import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.allImports
import net.dinkla.nkp.analysis.combinedReport
import net.dinkla.nkp.analysis.filteredImports
import net.dinkla.nkp.domain.kotlinlang.Project
import net.dinkla.nkp.utilities.loadFromJsonFile

class CouplingReport : AbstractCommand("Generate package coupling metrics") {
    private val includeAllLibraries by option(help = "include all libraries").flag(default = false)

    override fun run() {
        val project = loadFromJsonFile<Project>(model)
        val imports =
            if (!includeAllLibraries) {
                filteredImports(project)
            } else {
                allImports(project)
            }
        val report = combinedReport(imports)
        echo(Json.encodeToString(report))
    }
}
