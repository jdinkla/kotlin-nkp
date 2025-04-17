package net.dinkla.nkp.commands

import kotlinx.serialization.json.Json
import net.dinkla.nkp.analysis.inheritance
import net.dinkla.nkp.domain.kotlinlang.Project
import net.dinkla.nkp.utilities.loadFromJsonFile

class ClassInheritance : AbstractCommand("Class inheritance") {
    override fun run() {
        val project = loadFromJsonFile<Project>(model)
        echo(Json.encodeToString(project.inheritance()))
    }
}
