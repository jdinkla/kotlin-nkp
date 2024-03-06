package net.dinkla.nkp.domain

import kotlinx.serialization.Serializable
import net.dinkla.nkp.utilities.addSpaceAfter

@Serializable
data class ClassParameter(
    val name: String,
    val type: Type,
    val visibilityModifier: VisibilityModifier? = null,
    val propertyModifier: PropertyModifier? = null,
)

fun ClassParameter.prettyPrint(): String {
    val visibility = addSpaceAfter(visibilityModifier.prettyPrint())
    val property = addSpaceAfter(propertyModifier.prettyPrint())
    return "$visibility$property$name: $type"
}
