package net.dinkla.kpnk.analysis

import net.dinkla.kpnk.command.Command
import net.dinkla.kpnk.command.CommandManager
import net.dinkla.kpnk.domain.ClassSignature
import net.dinkla.kpnk.domain.FileInfos
import net.dinkla.kpnk.domain.prettyPrint
import net.dinkla.kpnk.logger

object Search : Command {
    override val description: String = "searches for a class <classname>"

    override fun execute(
        args: Array<String>,
        fileInfos: FileInfos,
    ) {
        if (args.size == 1) {
            reportSearch(fileInfos!!, args[0])
        } else {
            CommandManager.synopsis()
        }
    }
}

fun reportSearch(
    infos: FileInfos,
    clazz: String,
) {
    logger.info("*** searchClass ***")
    val found = infos.searchClass(clazz)
    found.forEach { println(it.prettyPrint()) }

    logger.info("*** searchHierarchy ***")
    val hier = infos.searchHierarchy(clazz)
    hier.forEach { println(it.prettyPrint()) }

    logger.info("*** searchImplementers ***")
    val impls = infos.searchImplementers(clazz)
    impls.forEach { println(it.prettyPrint()) }
}

fun FileInfos.searchClass(className: String): List<ClassSignature> =
    flatMap { fileInfo -> fileInfo.classes }
        .filter { clazz -> clazz.name == className }

fun FileInfos.searchHierarchy(className: String): List<ClassSignature> {
    val cls = searchClass(className)
    return cls +
        cls.flatMap { clazz -> clazz.inheritedFrom }
            .flatMap { this.searchHierarchy(it) }
}

fun FileInfos.searchImplementers(className: String): List<ClassSignature> {
    return flatMap { fileInfo -> fileInfo.classes }
        .filter { clazz -> clazz.inheritedFrom.contains(className) }
}
