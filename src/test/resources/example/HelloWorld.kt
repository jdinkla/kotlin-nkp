package example

import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE

data class HelloWorld(val many: Int) {
    override fun toString(): String = "many hello worlds $many"
}

object MathUtils {
    const val K_EPSILON = 0.01
    fun isZero(x: Double): Boolean = x > -K_EPSILON && x < K_EPSILON
}

fun topLevelFunction(n: Int, hw: HelloWorld): String {
    return FALSE.toString() + TRUE.toString() + hw.toString().repeat(n)
}

fun main() {
    println("has no args and returns Unit implicitly")
}
