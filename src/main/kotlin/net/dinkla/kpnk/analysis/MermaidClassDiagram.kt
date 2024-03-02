package net.dinkla.kpnk.analysis

import net.dinkla.kpnk.domain.ClassParameter
import net.dinkla.kpnk.domain.ClassSignature
import net.dinkla.kpnk.domain.Files
import net.dinkla.kpnk.domain.FunctionSignature
import net.dinkla.kpnk.domain.MemberModifier
import net.dinkla.kpnk.domain.Property
import net.dinkla.kpnk.domain.VisibilityModifier
import net.dinkla.kpnk.domain.prettyPrint
import net.dinkla.kpnk.utilities.addSpaceAfter
import java.io.File

fun mermaidClassDiagram(
    files: Files,
    outputFile: File,
) {
    val classes = files.flatMap { it.classes }.filter { !it.name.endsWith("Test") }
    val content = generateDiagram(classes)
    save(outputFile, content)
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
                    appendLine(formatLine(parameter.mermaid()))
                }
                clazz.properties.forEach { property ->
                    appendLine(formatLine(property.mermaid()))
                }
                clazz.functions.forEach { function ->
                    appendLine(formatLine(function.mermaid()))
                }
                append("}")
            }
            append("\n")
            clazz.inheritedFrom.forEach { inheritedFrom ->
                append("$inheritedFrom <|-- ${clazz.name}\n")
            }
        }
    }

private fun formatLine(pair: Pair<String, String>) = "  ${pair.first} ${pair.second}"

private fun modSign(visibilityModifier: VisibilityModifier?) =
    when (visibilityModifier) {
        VisibilityModifier.PUBLIC -> "+"
        VisibilityModifier.PRIVATE -> "-"
        VisibilityModifier.PROTECTED -> "#"
        VisibilityModifier.INTERNAL -> "~"
        null -> "+" // Kotlin has public as default
    }

internal fun FunctionSignature.mermaid(): Pair<String, String> {
    val ext = if (extensionOf == null) "" else "$extensionOf."
    val prettyParameters: String =
        if (parameters.isEmpty()) "" else parameters.joinToString(", ") { it.prettyPrint().fixMermaidBug() }
    val prettyReturnType = if (returnType == null) "" else ": ${returnType.toString().fixMermaidBug()}"
    val memberMod = if (memberModifier == MemberModifier.OVERRIDE) " «override»" else ""
    return Pair(modSign(visibilityModifier), "$ext$name($prettyParameters)$prettyReturnType$memberMod")
}

internal fun Property.mermaid(): Pair<String, String> {
    val mMod = addSpaceAfter(memberModifier.map { it.prettyPrint() }.sortedDescending().joinToString(" "))
    val mod = modifier.text
    val type = if (dataType != null) ": $dataType" else ""
    return Pair(modSign(visibilityModifier), "$mMod$name$type «$mod»")
}

internal fun ClassParameter.mermaid(): Pair<String, String> {
    val mod = if (propertyModifier == null) "" else " «${propertyModifier.prettyPrint()}»"
    return Pair(modSign(visibilityModifier), "$name: ${type.toString().fixMermaidBug()}$mod")
}

private fun String.fixMermaidBug(): String = replace("<", "‹").replace(">", "›")
