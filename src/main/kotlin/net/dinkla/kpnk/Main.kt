package net.dinkla.kpnk

fun main(args: Array<String>) {
    val directory = parseArgs(args)
    if (directory == null) {
        System.exit(-1)
    } else {
        val files = getAllKotlinFilesInDirectory(directory)
        for (fileName in files) {
            println(fileName)
            val tree = fromFile(fileName)
            val fileInfo = traverse(tree)
            println(fileInfo)
        }
    }
}

internal fun parseArgs(args: Array<String>): String? =
    if (args.size != 1) {
        println("Usage: kpnk <directory>")
        null
    } else {
        args[0]
    }
