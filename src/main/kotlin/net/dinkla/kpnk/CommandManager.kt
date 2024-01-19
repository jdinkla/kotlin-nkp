package net.dinkla.kpnk

import net.dinkla.kpnk.domain.FileInfos

typealias CommandId = String

interface Command {
    val description: String

    fun execute(
        args: Array<String>,
        fileInfos: FileInfos?,
    )
}

object CommandManager {
    private val commands = mutableMapOf<CommandId, Command>()

    fun add(
        id: CommandId,
        command: Command,
    ) {
        assert(!commands.containsKey(id))
        commands[id] = command
    }

    fun get(str: CommandId): Command? = commands[str]

    fun synopsis() {
        println("Usage: kpnk (directory|jsonfile) <command> [args of command]")
        println("where <command> is one of:")
        commands.forEach {
            println("  ${it.key} - ${it.value.description}")
        }
    }
}
