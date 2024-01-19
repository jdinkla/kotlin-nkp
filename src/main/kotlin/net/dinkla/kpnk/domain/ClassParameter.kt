package net.dinkla.kpnk.domain

import kotlinx.serialization.Serializable
import net.dinkla.kpnk.utilities.addSpaceAfter

@Serializable
data class ClassParameter(
    val name: String,
    val type: String,
    val visibilityModifier: VisibilityModifier? = null,
    val propertyModifier: PropertyModifier? = null,
)

fun ClassParameter.prettyPrint(): String {
    val visibility = addSpaceAfter(visibilityModifier.prettyPrint())
    val property = addSpaceAfter(propertyModifier.prettyPrint())
    return "$visibility$property$name: $type"
}
