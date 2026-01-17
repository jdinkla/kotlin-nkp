package net.dinkla.nkp.parser

import net.dinkla.nkp.domain.kotlinlang.KotlinFile

/**
 * Abstraction for Kotlin parsers that extract domain models from source code.
 */
interface KotlinParser {
    /**
     * Parse Kotlin source code from a file and extract a KotlinFile domain model.
     *
     * @param filePath The path to the Kotlin source file
     * @param prefix The directory prefix to strip from the file path
     * @return A Result containing either the extracted KotlinFile or an error
     */
    fun parseFile(
        filePath: String,
        prefix: String,
    ): Result<KotlinFile>
}

/**
 * Enum representing the available parser implementations.
 */
enum class ParserType {
    /** ANTLR-based parser using kotlin-grammar-tools */
    GRAMMAR,

    /** JetBrains PSI-based parser using kotlin-compiler-embeddable */
    PSI,
}
