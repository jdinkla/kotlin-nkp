package net.dinkla.kpnk.analysis

fun String.addIndent(n: Int): String {
    val sb = StringBuilder()
    for (line in this.lines()) {
        if (line.isBlank()) {
            sb.append("\n")
        } else {
            sb.append(" ".repeat(n))
            sb.append(line)
            sb.append("\n")
        }
    }
    return sb.toString()
}
