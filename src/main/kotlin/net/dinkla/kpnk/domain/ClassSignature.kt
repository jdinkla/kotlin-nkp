package net.dinkla.kpnk.domain

import kotlinx.serialization.Serializable

@Serializable
data class ClassSignature(
    val name: String,
    val parameters: List<Parameter> = listOf(),
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
}

@Serializable
enum class Type(val text: String) {
    CLASS("class"),
    OBJECT("object"),
    INTERFACE("interface"),
}

@Serializable
enum class ClassModifier(val text: String) {
    DATA("data"),
    ENUM("enum"),
    VALUE("value "),
}

@Serializable
enum class InheritanceModifier(val text: String) {
    OPEN("open"),
    ABSTRACT("abstract"),
}
