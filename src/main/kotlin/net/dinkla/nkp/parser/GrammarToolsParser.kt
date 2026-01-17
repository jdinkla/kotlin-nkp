package net.dinkla.nkp.parser

import net.dinkla.nkp.domain.FilePath
import net.dinkla.nkp.domain.kotlinlang.KotlinFile
import net.dinkla.nkp.extract.extract
import net.dinkla.nkp.utilities.fromFile
import java.io.File

/**
 * Parser implementation using kotlin-grammar-tools (ANTLR-based).
 * This is the original parser implementation.
 */
class GrammarToolsParser : KotlinParser {
    override fun parseFile(
        filePath: String,
        prefix: String,
    ): Result<KotlinFile> =
        try {
            val file = File(filePath)
            val withoutPrefix = filePath.removePrefix(prefix)
            val tree = fromFile(filePath)
            val kotlinFile =
                extract(
                    FilePath(withoutPrefix),
                    tree,
                    lastModified = file.lastModified(),
                    fileSize = file.length(),
                )
            Result.success(kotlinFile)
        } catch (e: Exception) {
            Result.failure(Error("parsing '$filePath' yields ${e.message}", e))
        }
}
