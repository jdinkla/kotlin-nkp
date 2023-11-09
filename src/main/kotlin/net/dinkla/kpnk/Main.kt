package net.dinkla.kpnk

import org.jetbrains.kotlin.spec.grammar.tools.parseKotlinCode
import org.jetbrains.kotlin.spec.grammar.tools.tokenizeKotlinCode
import java.io.File

fun main(args: Array<String>) {
    val directory = parseArgs(args)
    if (directory == null) {
        System.exit(-1)
    } else {
        val files = getAllKotlinFilesInDirectory(directory)
        println(files)
        for (file in files) {
            println(file)
            val text = File(file).readText()
            val tokens = tokenizeKotlinCode(text)
            val parseTree = parseKotlinCode(tokens)
            println(parseTree)
            parseTree.type
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
