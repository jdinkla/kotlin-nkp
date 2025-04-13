package net.dinkla.nkp.analysis

import io.github.oshai.kotlinlogging.KotlinLogging
import kotlinx.serialization.json.Json
import java.io.File

inline fun <reified T> save(
    file: File,
    items: List<T>,
) {
    val string = Json.encodeToString(items)
    file.writeText(string)
}

internal fun String.save(file: File) {
    val isMermaid = file.name.endsWith(".mermaid")
    val isHtml = file.name.endsWith(".html")
    require(isMermaid || isHtml)
    logger.info { "Writing mermaid class diagram to ${file.absolutePath}" }
    when {
        isMermaid -> file.writeText(this)
        else -> file.saveAsHtml(this)
    }
}

private fun File.saveAsHtml(content: String) {
    writeText(
        """
        <html>
        <head>
          <script src="https://cdn.jsdelivr.net/npm/mermaid/dist/mermaid.min.js"></script>
          <script>mermaid.initialize({startOnLoad:true});</script>
        </head>
        <body>
        <div class="mermaid">
        ${content.escapeForHtml()}
        </div>
        </body>
        </html>
        """.trimIndent(),
    )
}

private fun String.escapeForHtml() =
    replace("‹", "&lsaquo;")
        .replace("›", "&rsaquo;")
        .replace("«", "&laquo;")
        .replace("»", "&raquo;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")

private val logger = KotlinLogging.logger {}
