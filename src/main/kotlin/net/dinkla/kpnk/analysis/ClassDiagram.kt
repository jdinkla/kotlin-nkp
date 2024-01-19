package net.dinkla.kpnk.analysis

import net.dinkla.kpnk.Command
import net.dinkla.kpnk.CommandManager
import net.dinkla.kpnk.domain.ClassSignature
import net.dinkla.kpnk.domain.FileInfos
import net.dinkla.kpnk.domain.VisibilityModifier
import net.dinkla.kpnk.domain.prettyPrint
import java.io.File

object ClassDiagram : Command {
    override val description: String =
        "Generate a mermaid class diagram to stdout or to a file if specified (.mermaid and .html are supported)"

    override fun execute(
        args: Array<String>,
        fileInfos: FileInfos?,
    ) {
        val classes =
            fileInfos!!.flatMap { fileInfo ->
                fileInfo.topLevel.classes
            }
        val content = generateDiagram(classes)
        if (args.isEmpty()) {
            println(content)
        } else {
            val filename = args[0]
            if (filename.endsWith(".mermaid")) {
                File(filename).writeText(content)
            } else if (filename.endsWith(".html")) {
                saveAsHtml(filename, content)
            } else {
                println("Unknown file extension: $filename")
                CommandManager.synopsis()
            }
        }
    }

    private fun generateDiagram(classes: List<ClassSignature>) =
        buildString {
            append("classDiagram\n")
            append("direction LR\n")
            classes.forEach { clazz ->
                append("class ${clazz.name}")
                if (clazz.declarations.isNotEmpty() || clazz.parameters.isNotEmpty()) {
                    append(" {\n")
                    clazz.parameters.forEach { parameter ->
                        val modSign = modSign(parameter.visibilityModifier)
                        append("  $modSign ${parameter.prettyPrint()}\n")
                    }
                    clazz.properties.forEach { property ->
                        val modSign = modSign(property.visibilityModifier)
                        append("  $modSign ${property.prettyPrint()}\n")
                    }
                    clazz.functions.forEach { function ->
                        val modSign = modSign(function.visibilityModifier)
                        append("  $modSign ${function.prettyPrint()}\n")
                    }
                    append("}")
                }
                append("\n")
                clazz.inheritedFrom.forEach { inheritedFrom ->
                    append("$inheritedFrom <|-- ${clazz.name}\n")
                }
            }
        }
}

private fun modSign(visibilityModifier: VisibilityModifier?) =
    when (visibilityModifier) {
        VisibilityModifier.PUBLIC -> "+"
        VisibilityModifier.PRIVATE -> "-"
        VisibilityModifier.PROTECTED -> "#"
        VisibilityModifier.INTERNAL -> "~"
        null -> "+" // Kotlin has public as default
    }

private fun saveAsHtml(
    filename: String,
    content: String,
) {
    File(filename).writeText(
        """
        <html>
        <head>
          <script src="https://cdn.jsdelivr.net/npm/mermaid/dist/mermaid.min.js"></script>
          <script>mermaid.initialize({startOnLoad:true});</script>
        </head>
        <body>
        <div class="mermaid">
        $content
        </div>
        </body>
        </html>
        """.trimIndent(),
    )
}
