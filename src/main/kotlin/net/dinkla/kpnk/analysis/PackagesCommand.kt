package net.dinkla.kpnk.analysis

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.dinkla.kpnk.command.Command
import net.dinkla.kpnk.command.CommandManager
import net.dinkla.kpnk.domain.Files
import java.io.File

object PackagesCommand : Command {
    override val description: String = "reports the packages"

    override fun execute(
        args: Array<String>,
        files: Files,
    ) {
        val packages = files.packages()
        if (args.size == 2 && args[0] == "--output") {
            val filename = args[1]
            val string = Json.encodeToString(packages)
            File(filename).writeText(string)
        } else if (args.isEmpty()) {
            println(packages.joinToString("\n"))
        } else {
            CommandManager.synopsis()
        }
    }
}
