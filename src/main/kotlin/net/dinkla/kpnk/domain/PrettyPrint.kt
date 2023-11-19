package net.dinkla.kpnk.domain

import net.dinkla.kpnk.addSpaceAfter

fun TopLevel.prettyPrint(): String {
    var isLineNeeded = false
    return buildString {
        appendLine("package $packageName")
        appendLine()
        if (imports.isNotEmpty()) {
            appendLine(imports.joinToString("\n") { it.prettyPrint() })
            isLineNeeded = true
        }
        if (isLineNeeded) {
            appendLine()
            isLineNeeded = false
        }
        if (typeAliases.isNotEmpty()) {
            appendLine(typeAliases.joinToString("\n") { it.prettyPrint() })
            isLineNeeded = true
        }
        if (isLineNeeded) {
            appendLine()
            isLineNeeded = false
        }
        if (properties.isNotEmpty()) {
            appendLine(properties.joinToString("\n") { it.prettyPrint() })
            isLineNeeded = true
        }
        if (isLineNeeded) {
            appendLine()
            isLineNeeded = false
        }
        if (functions.isNotEmpty()) {
            appendLine(functions.joinToString("\n") { it.prettyPrint() })
            isLineNeeded = true
        }
        if (isLineNeeded) {
            appendLine()
            isLineNeeded = false
        }
        if (classes.isNotEmpty()) {
            appendLine(classes.joinToString("\n") { it.prettyPrint() })
            isLineNeeded = true
        }
    }
}

fun Parameter.prettyPrint(): String = "$name: $type"

fun Import.prettyPrint(): String = "import $name"

fun VisibilityModifier?.prettyPrint() = when (this) {
    null -> ""
    else -> text
}

fun ClassModifier?.prettyPrint() = when (this) {
    null -> ""
    else -> text
}

fun InheritanceModifier?.prettyPrint() = when (this) {
    null -> ""
    else -> text
}

fun ClassSignature.prettyPrint(): String {
    val inherited = if (inheritedFrom.isEmpty()) "" else " : " + inheritedFrom.joinToString(", ")
    val prettyParameters: String =
        if (parameters.isEmpty()) "" else parameters.joinToString(", ") { it.prettyPrint() }
    val visibility = addSpaceAfter(visibilityModifier.prettyPrint())
    val joined = functions.map { "    ${it.prettyPrint()}" }.joinToString("\n")
    val joined2 = if (joined.isNotEmpty()) {
        "\n$joined\n"
    } else {
        joined
    }
    val classMod = addSpaceAfter(classModifier.prettyPrint())
    return "${visibility}${classMod}${elementType.text} $name($prettyParameters)$inherited {$joined2}"
}

fun FunctionSignature.prettyPrint(): String {
    val prettyReturnType = if (returnType == null) "" else ": $returnType"
    val prettyParameters: String =
        if (parameters.isEmpty()) "" else parameters.joinToString(", ") { it.prettyPrint() }
    val ext = if (extensionOf == null) "" else "$extensionOf."
    val visibility = addSpaceAfter(visibilityModifier.prettyPrint())
    return "${visibility}fun $ext$name($prettyParameters)$prettyReturnType"
}

fun TypeAlias.prettyPrint(): String = "typealias $name = $def"

fun Property.prettyPrint(): String {
    val mod = modifier.text
    return "$mod $name : $dataType"
}
