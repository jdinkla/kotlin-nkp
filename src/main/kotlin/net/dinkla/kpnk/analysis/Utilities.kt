package net.dinkla.kpnk.analysis

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File

fun String.addIndent(n: Int): String {
    val sb = StringBuilder()
    for (line in this.lines()) {
        if (line.isBlank()) {
            sb.append("\n")
        } else {
            sb.append(" ".repeat(n))
            sb.append(line)
            sb.append("\n")
        }
    }
    return sb.toString()
}

inline fun <reified T> save(file: File, items: List<T>) {
    val string = Json.encodeToString(items)
    file.writeText(string)
}
