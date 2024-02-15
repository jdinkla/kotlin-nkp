package net.dinkla.kpnk.domain

import kotlinx.serialization.Serializable
import net.dinkla.kpnk.utilities.addSpaceAfter

@Serializable
data class ClassSignature(
    val name: String,
    val parameters: List<ClassParameter> = listOf(),
    val inheritedFrom: List<String> = listOf(),
    val visibilityModifier: VisibilityModifier? = null,
    val elementType: Type = Type.CLASS,
    val classModifier: ClassModifier? = null,
    val inheritanceModifier: InheritanceModifier? = null,
    val declarations: List<Defined> = listOf(),
) : Defined {
    val functions: List<FunctionSignature>
        get() = declarations.filterIsInstance<FunctionSignature>()

    val properties: List<Property>
        get() = declarations.filterIsInstance<Property>()

    val classes: List<ClassSignature>
        get() = declarations.filterIsInstance<ClassSignature>()

    val aliases: List<TypeAlias>
        get() = declarations.filterIsInstance<TypeAlias>()

    @Serializable
    enum class Type(val text: String) {
        CLASS("class"),
        OBJECT("object"),
        INTERFACE("interface"),
    }
}

fun ClassSignature.prettyPrint(): String {
    val visMod = addSpaceAfter(visibilityModifier.prettyPrint())
    val classMod = addSpaceAfter(classModifier.prettyPrint())
    val inhMod = addSpaceAfter(inheritanceModifier.prettyPrint())
    val type = elementType.text
    val prettyParameters: String =
        if (parameters.isEmpty()) "" else parameters.joinToString(", ") { it.prettyPrint() }
    val inherited = if (inheritedFrom.isEmpty()) "" else " : " + inheritedFrom.joinToString(", ")

    val joined =
        declarations.map {
            val text =
                when (it) {
                    is FunctionSignature -> it.prettyPrint()
                    is Property -> it.prettyPrint()
                    is ClassSignature -> it.prettyPrint()
                    else -> ""
                }
            "    $text"
        }.joinToString("\n")
    val joined2 =
        if (joined.isNotEmpty()) {
            "\n$joined\n"
        } else {
            joined
        }
    return "$visMod$classMod$inhMod$type $name($prettyParameters)$inherited {$joined2}"
}
