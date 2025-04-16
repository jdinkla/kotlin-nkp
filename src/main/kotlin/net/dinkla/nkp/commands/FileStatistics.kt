package net.dinkla.nkp.commands

import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.fileStatistics
import net.dinkla.nkp.domain.Project
import net.dinkla.nkp.utilities.loadFromJsonFile

class FileStatistics : AbstractCommand("File statistics") {
    override fun run() {
        val project = loadFromJsonFile<Project>(model)
        val stats = fileStatistics(project)
        echo(Json.encodeToString(stats))
    }
}
