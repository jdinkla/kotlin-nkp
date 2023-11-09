package example

data class HelloWorld(val many: Int) {
    override fun toString(): String = "many hello worlds $many"
}
