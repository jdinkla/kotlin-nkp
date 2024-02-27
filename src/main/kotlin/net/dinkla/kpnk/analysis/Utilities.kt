package net.dinkla.kpnk.analysis

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

inline fun <reified T> save(file: File, items: List<T>) {
    val string = Json.encodeToString(items)
    file.writeText(string)
}
