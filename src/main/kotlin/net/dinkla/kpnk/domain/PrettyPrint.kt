package net.dinkla.kpnk.domain

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

fun TypeAlias.prettyPrint(): String = "typealias $name = $def"
