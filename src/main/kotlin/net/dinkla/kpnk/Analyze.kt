package net.dinkla.kpnk

fun dependencies(infos: List<FileInfo>): Map<String, Set<String>> {
    val dependencies = mutableMapOf<String, MutableSet<String>>()
    val parsed = infos.filterIsInstance<FileInfo.Parsed>()
    for (info in parsed) {
        val name = info.basename()
        for (imp in info.elements.imports) {
            val packageName = imp.packageName()
            val set = dependencies.getOrDefault(name, mutableSetOf())
            set += packageName
            dependencies[name] = set
        }
    }
    return dependencies.mapValues { it.value.toSet() }
}
