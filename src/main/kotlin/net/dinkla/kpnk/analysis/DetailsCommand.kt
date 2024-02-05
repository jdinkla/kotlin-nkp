package net.dinkla.kpnk.analysis

import net.dinkla.kpnk.domain.Files

object DetailsCommand{
    fun execute(
        files: Files,
    ) {
        details(files)
    }
}

private fun details(files: Files) {
    for (file in files) {
        println(
            """
            file: ${file.packageName()}
            number of imports: ${file.imports.size}
            number of classes: ${file.classes.size}
            number of functions: ${file.functions.size}
            number of properties: ${file.properties.size}
            """.trimIndent(),
        )
        file.classes.forEach { clazz ->
            println(
                """
                class: ${clazz.name}
                number of parameters: ${clazz.parameters.size}
                number of properties: ${clazz.properties.size}
                number of functions: ${clazz.functions.size}
                """.trimIndent().addIndent(4),
            )
        }
    }
}
