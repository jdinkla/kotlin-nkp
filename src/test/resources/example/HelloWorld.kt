package example

import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE

internal data class HelloWorld(val many: Int) {
    override fun toString(): String = "many hello worlds $many"
}

private interface Gen {
    fun gen(n: Int): String
}

enum class AB { A, B }
enum class ABC(internal val i: Int) { A(1), B(2), C(3) }

class GenImpl(val many: Int) : Gen {
    override fun gen(n: Int): String = "many hello worlds ${many * n}"
}

object MathUtils {
    private const val K_EPSILON = 0.01
    fun isZero(x: Double): Boolean = x > -K_EPSILON && x < K_EPSILON
}

internal fun topLevelFunction(n: Int, hw: HelloWorld): String {
    return FALSE.toString() + TRUE.toString() + hw.toString().repeat(n)
}

private fun HelloWorld.extensionFun(): String = this.many.toString()

fun main() {
    println("has no args and returns Unit implicitly")
    MathUtils.isZero(2.3)
    val hw = HelloWorld(3)
    topLevelFunction(3, hw)
    GenImpl(1)
    AB.A
    AB.B
    ABC.A
    ABC.B
    ABC.C
    hw.extensionFun()
}
