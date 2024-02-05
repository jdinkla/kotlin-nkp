package net.dinkla.kpnk.analysis

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.dinkla.kpnk.domain.Files
import java.io.File

fun packages(
    files: Files,
    file: File
) {
    val packages = files.packages()
    val string = Json.encodeToString(packages)
    file.writeText(string)
}
