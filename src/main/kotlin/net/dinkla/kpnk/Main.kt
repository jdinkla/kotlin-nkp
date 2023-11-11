package net.dinkla.kpnk

private const val SCREEN_WIDTH = 120

fun main(args: Array<String>) {
    val directory = parseArgs(args)
    if (directory == null) {
        System.exit(-1)
    } else {
        val infos = readFiles(directory)
        println("-".repeat(SCREEN_WIDTH))
        println("Reports")
        println("-".repeat(SCREEN_WIDTH))
        println(
            infos.groupBy { it.javaClass }.forEach {
                println("${it.key}: ${it.value.size}")
            },
        )
        println()
        val deps = dependencies(infos)
        println(deps)
    }
}

private fun readFiles(directory: String): List<FileInfo> {
    println("Directory: $directory")
    println()
    val files = getAllKotlinFilesInDirectory(directory)
    return fileInfos(files, directory)
}

private fun fileInfos(
    files: List<String>,
    directory: String,
): List<FileInfo> {
    val results = mutableListOf<FileInfo>()
    for (fileName in files) {
        try {
            println("File: " + fileNameWithoutDirectory(directory, fileName))
            val tree = fromFile(fileName)
            val fileInfo = extract(tree)
            results += FileInfo.Parsed(fileName, fileInfo)
            println(fileInfo)
        } catch (e: Exception) {
            println("ERROR: " + e.message)
            results += FileInfo.Error(fileName, e.message!!)
        }
        println("-".repeat(SCREEN_WIDTH))
    }
    return results.toList()
}

internal fun parseArgs(args: Array<String>): String? = if (args.size != 1) {
    println("Usage: kpnk <directory>")
    null
} else {
    args[0]
}
