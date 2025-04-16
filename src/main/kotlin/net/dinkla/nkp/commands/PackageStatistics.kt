package net.dinkla.nkp.commands

import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.packageStatistics
import net.dinkla.nkp.domain.Project
import net.dinkla.nkp.utilities.loadFromJsonFile

class PackageStatistics : AbstractCommand("Package statistics") {
    override fun run() {
        val project = loadFromJsonFile<Project>(model)
        val stats = packageStatistics(project)
        echo(Json.encodeToString(stats))
    }
}
