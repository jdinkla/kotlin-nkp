package net.dinkla.kpnk.analysis

import net.dinkla.kpnk.Command
import net.dinkla.kpnk.domain.ClassSignature
import net.dinkla.kpnk.domain.FileInfo
import net.dinkla.kpnk.domain.FileInfos
import net.dinkla.kpnk.logger

object Outliers : Command {
    override val description: String = "reports outliers"

    override fun execute(
        args: Array<String>,
        fileInfos: FileInfos?,
    ) {
        reportLargeClasses(fileInfos!!)
    }
}

internal fun reportLargeClasses(
    infos: List<FileInfo>,
    topN: Int = 10,
) {
    logger.info("*** Large Classes ***")
    largeClasses(infos, topN).forEach { c ->
        println(
            """
            ${c.name} has ${c.classes.size} classes, ${c.functions.size} functions and ${c.properties.size} properties (total ${c.declarations.size})
            """.trimIndent(),
        )
    }
}

internal fun largeClasses(
    infos: List<FileInfo>,
    topN: Int,
): List<ClassSignature> {
    val allClasses = infos.flatMap { it.topLevel.classes }
    return allClasses.sortedByDescending { it.declarations.size }.take(topN)
}
