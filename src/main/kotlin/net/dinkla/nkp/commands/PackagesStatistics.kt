package net.dinkla.nkp.commands

import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.packagesStatistics
import net.dinkla.nkp.domain.Project
import net.dinkla.nkp.utilities.loadFromJsonFile

class PackagesStatistics : AbstractCommand("Package statistics") {
    override fun run() {
        val project = loadFromJsonFile<Project>(model)
        val stats = packagesStatistics(project)
        echo(Json.encodeToString(stats))
    }
}
