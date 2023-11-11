package example

import example.MathUtils.isZero
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE

data class HelloWorld(val many: Int) {
    override fun toString(): String = "many hello worlds $many"
}

interface Gen {
    fun gen(n: Int): String
}

data class GenImpl(val many: Int) : Gen {
    override fun gen(n: Int): String = "many hello worlds ${many * n}"
}

object MathUtils {
    private const val K_EPSILON = 0.01
    fun isZero(x: Double): Boolean = x > -K_EPSILON && x < K_EPSILON
}

fun topLevelFunction(n: Int, hw: HelloWorld): String {
    return FALSE.toString() + TRUE.toString() + hw.toString().repeat(n)
}

fun main() {
    println("has no args and returns Unit implicitly")
    isZero(2.3)
    topLevelFunction(3, HelloWorld(3))
    GenImpl(1)
}
