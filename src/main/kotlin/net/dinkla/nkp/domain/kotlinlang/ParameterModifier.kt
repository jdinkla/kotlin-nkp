package net.dinkla.nkp.domain.kotlinlang

import kotlinx.serialization.Serializable

@Serializable
enum class ParameterModifier(
    val text: String,
) {
    VARARG("vararg"),
    NOINLINE("noinline"),
    CROSSINLINE("crossinline"),
}
