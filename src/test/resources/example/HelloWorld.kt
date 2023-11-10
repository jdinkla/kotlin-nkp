package example

import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE

data class HelloWorld(val many: Int) {
    override fun toString(): String = "many hello worlds $many"
}

fun topLevelFunction(i: Int, hw: HelloWorld): String {
    return FALSE.toString() + TRUE.toString() + hw.toString().repeat(i)
}
