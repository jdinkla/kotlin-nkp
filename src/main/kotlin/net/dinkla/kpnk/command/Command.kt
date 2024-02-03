package net.dinkla.kpnk.command

import net.dinkla.kpnk.domain.Files

typealias CommandId = String

interface Command {
    val description: String

    fun execute(
        args: Array<String>,
        files: Files,
    )
}
