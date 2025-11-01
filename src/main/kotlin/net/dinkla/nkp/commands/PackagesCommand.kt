package net.dinkla.nkp.commands

import kotlinx.serialization.json.Json

class PackagesCommand : AbstractCommand("Packages report", "packages") {
    override fun run() {
        val project = loadProject()
        echo(Json.encodeToString(project.packages()))
    }
}
