package net.dinkla.kpnk.analysis

import net.dinkla.kpnk.command.Command
import net.dinkla.kpnk.command.CommandManager
import net.dinkla.kpnk.domain.ClassParameter
import net.dinkla.kpnk.domain.ClassSignature
import net.dinkla.kpnk.domain.Files
import net.dinkla.kpnk.domain.FunctionSignature
import net.dinkla.kpnk.domain.Property
import net.dinkla.kpnk.domain.VisibilityModifier
import net.dinkla.kpnk.domain.prettyPrint
import net.dinkla.kpnk.utilities.addSpaceAfter
import java.io.File

object MermaidClassDiagram : Command {
    override val description: String =
        "Generate a mermaid class diagram to stdout or to a file if specified (.mermaid and .html are supported)"

    override fun execute(
        args: Array<String>,
        files: Files,
    ) {
        val classes = files.flatMap { it.classes }.filter { !it.name.endsWith("Test") }
        val content = generateDiagram(classes)
        if (args.isEmpty()) {
            println(content)
        } else {
            writeFile(args[0], content)
        }
    }

    private fun generateDiagram(classes: List<ClassSignature>) =
        buildString {
            append("classDiagram\n")
            append("direction LR\n")
            classes.forEach { clazz ->
                append("class ${clazz.name}")
                val isEmpty = clazz.parameters.isEmpty() && clazz.properties.isEmpty() && clazz.functions.isEmpty()
                if (!isEmpty) {
                    append(" {\n")
                    clazz.parameters.forEach { parameter ->
                        val modSign = modSign(parameter.visibilityModifier)
                        append("  $modSign ${parameter.mermaid()}\n")
                    }
                    clazz.properties.forEach { property ->
                        val modSign = modSign(property.visibilityModifier)
                        append("  $modSign ${property.mermaid()}\n")
                    }
                    clazz.functions.forEach { function ->
                        val modSign = modSign(function.visibilityModifier)
                        append("  $modSign ${function.mermaid()}\n")
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

val re = Regex("[<>]")

fun String.fix(): String = replace(re, "~").replace("-~", "->")

private fun FunctionSignature.mermaid(): String {
    val prettyReturnType = if (returnType == null) "" else ": $returnType".fix()
    val prettyParameters: String =
        if (parameters.isEmpty()) "" else parameters.joinToString(", ") { it.prettyPrint().fix() }
    val ext = if (extensionOf == null) "" else "$extensionOf."
    val memberMod = addSpaceAfter(memberModifier.prettyPrint())
    return "${memberMod}fun $ext$name($prettyParameters)$prettyReturnType"
}

private fun Property.mermaid(): String {
    val mMod = addSpaceAfter(memberModifier.map { it.prettyPrint() }.sortedDescending().joinToString(" "))
    val mod = modifier.text
    val type = if (dataType != null) " : $dataType" else ""
    return "$mMod$mod $name$type"
}

private fun ClassParameter.mermaid(): String {
    val property = addSpaceAfter(propertyModifier.prettyPrint())
    return "$property$name: $type"
}

private fun writeFile(
    filename: String,
    content: String,
) {
    if (filename.endsWith(".mermaid")) {
        File(filename).writeText(content)
    } else if (filename.endsWith(".html")) {
        saveAsHtml(filename, content)
    } else {
        println("Unknown file extension: $filename")
        CommandManager.synopsis()
    }
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
