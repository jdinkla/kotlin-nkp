package net.dinkla.kpnk.command

import net.dinkla.kpnk.domain.FileInfo
import net.dinkla.kpnk.domain.FileInfos

object SaveCommand : Command {
    override val description: String = "saves to <filename>"

    override fun execute(
        args: Array<String>,
        fileInfos: FileInfos?,
    ) {
        if (args.size == 1) {
            FileInfo.saveToJsonFile(fileInfos!!, args[0])
        } else {
            CommandManager.synopsis()
        }
    }
}
