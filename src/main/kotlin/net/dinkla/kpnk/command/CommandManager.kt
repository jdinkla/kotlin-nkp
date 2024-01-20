package net.dinkla.kpnk.command

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
