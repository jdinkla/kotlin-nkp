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
    override val declarations: List<Defined> = listOf(),
) : Defined,
    HasDeclarations {
    @Serializable
    enum class Type(
        val text: String,
    ) {
        CLASS("class"),
        OBJECT("object"),
        INTERFACE("interface"),
    }
}
