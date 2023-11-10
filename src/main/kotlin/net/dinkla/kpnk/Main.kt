package net.dinkla.kpnk

fun main(args: Array<String>) {
    val directory = parseArgs(args)
    if (directory == null) {
        System.exit(-1)
    } else {
        val files = getAllKotlinFilesInDirectory(directory)
        for (file in files) {
            println(file)
            val tree = fromFile(file)
            walk(tree, 0)
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
