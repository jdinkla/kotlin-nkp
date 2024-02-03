package net.dinkla.kpnk.command

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import net.dinkla.kpnk.domain.FileInfos

class CommandManagerTest : StringSpec({
    "get should return a command after it was inserted" {
        CommandManager.get("dummy") shouldBe DummyCommand
    }
    "get return null if the command is not present" {
        CommandManager.get("not present") shouldBe null
    }
}) {
    init {
        CommandManager.add("dummy", DummyCommand)
    }
}

private object DummyCommand : Command {
    override val description: String = "dummy command"

    override fun execute(
        args: Array<String>,
        fileInfos: FileInfos,
    ) = Unit
}
