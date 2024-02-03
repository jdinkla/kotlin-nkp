package net.dinkla.kpnk.command

import net.dinkla.kpnk.domain.FileInfo
import net.dinkla.kpnk.domain.Files

object SaveCommand : Command {
    override val description: String = "saves to <filename>"

    override fun execute(
        args: Array<String>,
        files: Files,
    ) {
        if (args.size == 1) {
            FileInfo.saveToJsonFile(files, args[0])
        } else {
            CommandManager.synopsis()
        }
    }
}
