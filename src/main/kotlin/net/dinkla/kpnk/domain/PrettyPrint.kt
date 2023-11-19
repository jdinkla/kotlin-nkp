package net.dinkla.kpnk.domain

import net.dinkla.kpnk.utilities.addSpaceAfter

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

fun FunctionSignature.prettyPrint(): String {
    val prettyReturnType = if (returnType == null) "" else ": $returnType"
    val prettyParameters: String =
        if (parameters.isEmpty()) "" else parameters.joinToString(", ") { it.prettyPrint() }
    val ext = if (extensionOf == null) "" else "$extensionOf."
    val visibility = addSpaceAfter(visibilityModifier.prettyPrint())
    return "${visibility}fun $ext$name($prettyParameters)$prettyReturnType"
}

fun TypeAlias.prettyPrint(): String = "typealias $name = $def"
