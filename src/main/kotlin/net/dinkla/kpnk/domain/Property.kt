package net.dinkla.kpnk.domain

import kotlinx.serialization.Serializable
import net.dinkla.kpnk.utilities.addSpaceAfter

@Serializable
data class Property(
    val name: String,
    val dataType: String? = null,
    val modifier: PropertyModifier = PropertyModifier.VAL,
    val visibilityModifier: VisibilityModifier? = null,
    val memberModifier: MemberModifier? = null,
) : Defined

fun Property.prettyPrint(): String {
    val mMod = addSpaceAfter(memberModifier.prettyPrint())
    val vMod = addSpaceAfter(visibilityModifier.prettyPrint())
    val mod = modifier.text
    val type = if (dataType != null) " : $dataType" else ""
    return "$mMod$vMod$mod $name$type"
}
