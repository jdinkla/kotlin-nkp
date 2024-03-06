package net.dinkla.nkp.utilities

import org.jetbrains.kotlin.spec.grammar.tools.KotlinParseTree
import org.jetbrains.kotlin.spec.grammar.tools.parseKotlinCode
import org.jetbrains.kotlin.spec.grammar.tools.tokenizeKotlinCode
import java.io.File

internal fun fromText(text: String): KotlinParseTree = parseKotlinCode(tokenizeKotlinCode(text))

internal fun fromFile(file: String): KotlinParseTree = fromText(File(file).readText())
