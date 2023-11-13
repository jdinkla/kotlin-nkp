package net.dinkla.kpnk.elements

import net.dinkla.kpnk.addSpaceAfter

fun Elements.prettyPrint(): String = """
package $packageName
${imports.joinToString("\n") { it.prettyPrint() }}
${typeAliases.joinToString("\n") { it.prettyPrint() }}
${properties.joinToString("\n") { it.prettyPrint() }}
${functions.joinToString("\n") { it.prettyPrint() }}
${classes.joinToString("\n") { it.prettyPrint() }}
""".trimIndent()

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
    val classMod = addSpaceAfter(classModifier.prettyPrint())
    return "${visibility}${classMod}${elementType.text} $name($prettyParameters)$inherited {\n$joined\n}"
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
    return "${mod} ${name} : ${type}"
}
