package example

class HelloWorld2(
    val hw: HelloWorld,
)

fun main() {
    val h = HelloWorld2(HelloWorld(3))
    println(h.hw.toString())
}
