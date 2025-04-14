package net.dinkla.nkp.analysis

import net.dinkla.nkp.domain.ClassParameter
import net.dinkla.nkp.domain.ClassSignature
import net.dinkla.nkp.domain.FunctionSignature
import net.dinkla.nkp.domain.MemberModifier
import net.dinkla.nkp.domain.Project
import net.dinkla.nkp.domain.Property
import net.dinkla.nkp.domain.VisibilityModifier
import net.dinkla.nkp.utilities.addSpaceAfter
import net.dinkla.nkp.utilities.prettyPrint

fun mermaidClassDiagram(project: Project): String {
    val classes = project.flatMap { it.classes }.filter { !it.name.endsWith("Test") }
    return generateDiagram(classes)
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
