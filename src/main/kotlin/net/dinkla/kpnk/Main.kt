package net.dinkla.kpnk

fun main(args: Array<String>) {
    val directory = parseArgs(args)
    if (directory == null) {
        System.exit(-1)
    } else {
        println("Directory: $directory")
        println()
        val files = getAllKotlinFilesInDirectory(directory)
        for (fileName in files) {
            try {
                println("File: " + fileNameWithoutDirectory(directory, fileName))
                val tree = fromFile(fileName)
                val fileInfo = traverse(tree)
                println(fileInfo)
            } catch (e: Exception) {
                println("ERROR: " + e.message)
            }
            println()
        }
    }
}


internal fun parseArgs(args: Array<String>): String? = if (args.size != 1) {
    println("Usage: kpnk <directory>")
    null
} else {
    args[0]
}
