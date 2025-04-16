package net.dinkla.nkp.commands

import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.classStatistics
import net.dinkla.nkp.domain.Project
import net.dinkla.nkp.utilities.loadFromJsonFile

class ClassStatistics : AbstractCommand("Class statistics") {
    override fun run() {
        val project = loadFromJsonFile<Project>(model)
        val stats = classStatistics(project)
        echo(Json.encodeToString(stats))
    }
}
