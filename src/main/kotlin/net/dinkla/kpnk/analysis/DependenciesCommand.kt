package net.dinkla.kpnk.analysis

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.dinkla.kpnk.command.Command
import net.dinkla.kpnk.command.CommandManager
import net.dinkla.kpnk.domain.Files
import java.io.File

object DependenciesCommand : Command {
    override val description: String = "reports dependencies to stdout or to a file with --output <filename>"

    override fun execute(
        args: Array<String>,
        files: Files,
    ) {
        val dependencies = Dependencies.from(files)
        val string = Json.encodeToString(dependencies)
        if (args.size == 2 && args[0] == "--output") {
            val filename = args[1]
            File(filename).writeText(string)
        } else if (args.isEmpty()) {
            println(string)
        } else {
            CommandManager.synopsis()
        }
    }
}
