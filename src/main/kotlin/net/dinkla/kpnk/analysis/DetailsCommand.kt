package net.dinkla.kpnk.analysis

import net.dinkla.kpnk.command.Command
import net.dinkla.kpnk.command.CommandManager
import net.dinkla.kpnk.domain.Files
import java.io.File

object DetailsCommand : Command {
    override val description: String = "reports class details to stdout or to a file with --output <filename>"

    override fun execute(
        args: Array<String>,
        files: Files,
    ) {
        // val classDetails = "ClassDetails.from(classDetails(fileInfos!!))"
        f(files)
        val string = "Json.encodeToString(classDetails)"
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

fun f(files: Files) {
    for (file in files) {
        println(
            """
            file: ${file.packageName()}
            number of imports: ${file.imports.size}
            number of classes: ${file.classes.size}
            number of functions: ${file.functions.size}
            number of properties: ${file.properties.size}
            """.trimIndent(),
        )
        file.classes.forEach { clazz ->
            println(
                """
                class: ${clazz.name}
                number of parameters: ${clazz.parameters.size}
                number of properties: ${clazz.properties.size}
                number of functions: ${clazz.functions.size}
                """.trimIndent().addIndent(4),
            )
        }
    }
}

fun String.addIndent(n: Int): String {
    val sb = StringBuilder()
    for (line in this.lines()) {
        if (line.isBlank()) {
            sb.append("\n")
        } else {
            sb.append(" ".repeat(n))
            sb.append(line)
            sb.append("\n")
        }
    }
    return sb.toString()
}
