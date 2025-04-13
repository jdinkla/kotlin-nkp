package net.dinkla.nkp.domain

import kotlinx.serialization.Serializable

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
    enum class Type(
        val text: String,
    ) {
        CLASS("class"),
        OBJECT("object"),
        INTERFACE("interface"),
    }
}
