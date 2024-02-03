package net.dinkla.kpnk.domain

import kotlinx.serialization.Serializable

@Serializable
class Files(private val items: List<AnalysedFile>) : List<AnalysedFile> by items {
    companion object {
        // Factory method
        fun from(vararg elements: AnalysedFile): Files {
            return Files(elements.toList())
        }
    }
}
