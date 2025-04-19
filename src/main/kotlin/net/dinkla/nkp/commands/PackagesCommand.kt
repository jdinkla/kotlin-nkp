package net.dinkla.nkp.commands

import kotlinx.serialization.json.Json
import net.dinkla.nkp.domain.kotlinlang.Project
import net.dinkla.nkp.utilities.loadFromJsonFile

class PackagesCommand : AbstractCommand("Packages report", "packages") {
    override fun run() {
        val project = loadFromJsonFile<Project>(model)
        echo(Json.encodeToString(project.packages()))
    }
}
