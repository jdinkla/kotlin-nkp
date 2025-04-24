package net.dinkla.nkp.commands

import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.packagesStatistics
import net.dinkla.nkp.domain.kotlinlang.Project
import net.dinkla.nkp.utilities.loadJson

class PackageStatisticsCommand : AbstractCommand("Package statistics", "package-statistics") {
    override fun run() {
        val project = model.loadJson<Project>()
        val stats = packagesStatistics(project)
        echo(Json.encodeToString(stats))
    }
}
