package net.dinkla.nkp.commands

import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.ClassStatistics
import net.dinkla.nkp.domain.kotlinlang.Project
import net.dinkla.nkp.utilities.loadFromJsonFile

class ClassStatisticsCommand : AbstractCommand("Class statistics", "class-statistics") {
    override fun run() {
        val project = loadFromJsonFile<Project>(model)
        val stats = ClassStatistics.from(project)
        echo(Json.encodeToString(stats))
    }
}
