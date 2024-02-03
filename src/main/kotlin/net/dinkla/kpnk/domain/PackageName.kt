package net.dinkla.kpnk.domain

import kotlinx.serialization.Serializable

@JvmInline
@Serializable
value class PackageName(val name: String) {
    infix fun isSubPackageOf(other: PackageName): Boolean {
        return name.startsWith("${other.name}.")
    }

    infix fun isSuperPackage(other: PackageName): Boolean {
        return other isSubPackageOf this
    }

    infix fun isSidePackage(other: PackageName): Boolean {
        return !isSubPackageOf(other) && !isSuperPackage(other) &&
            name.commonPrefixWith(other.name).isNotEmpty()
    }

    infix fun isOtherPackage(other: PackageName): Boolean {
        return name.commonPrefixWith(other.name).isEmpty()
    }
}
