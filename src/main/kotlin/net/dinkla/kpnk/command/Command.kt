package net.dinkla.kpnk.command

import net.dinkla.kpnk.domain.FileInfos

typealias CommandId = String

interface Command {
    val description: String

    fun execute(
        args: Array<String>,
        fileInfos: FileInfos?,
    )
}
