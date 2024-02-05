package net.dinkla.kpnk.analysis

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import net.dinkla.kpnk.domain.Files
import net.dinkla.kpnk.logger
import java.io.File

object DependenciesCommand {
    fun execute(
        files: Files,
        file: File
    ) {
        val dependencies = Dependencies.from(files)
        logger.info("Writing dependencies to ${file.absolutePath}")
        val string = Json.encodeToString(dependencies)
        file.writeText(string)
    }
}
