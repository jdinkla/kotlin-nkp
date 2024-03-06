package net.dinkla.nkp.domain

import kotlinx.serialization.Serializable
import kotlin.math.max

@Serializable
@JvmInline
value class FileName(val name: String) {
    val basename: String
        get() {
            val index = max(name.lastIndexOf("/"), name.lastIndexOf("\\"))
            return if (index >= 0) {
                name.substring(index + 1)
            } else {
                name
            }
        }

    fun withoutDirectory(directory: String): String {
        return name.substring(directory.length + 1)
    }
}
