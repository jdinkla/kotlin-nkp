package net.dinkla.kpnk.analysis

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import net.dinkla.kpnk.domain.PackageName
import net.dinkla.kpnk.exampleFiles
import net.dinkla.kpnk.examplePackage

class ImportStatisticsTest : StringSpec({
    "importStatistics should return statistics for one package" {
        val ls: AnalyzedPackage = AnalyzedPackage.from(examplePackage)
        ls.packageName shouldBe PackageName("net.dinkla.kpnk")
        ls.importedElements.size shouldBe 2
        ls.importStatistics.total shouldBe 2
        ls.importStatistics.distinct shouldBe 1
    }

    "importStatistics should return statistics from Files" {
        val ls: List<AnalyzedPackage> = AnalyzedPackage.from(exampleFiles)
        ls.size shouldBe 1
        ls[0].importedElements.size shouldBe 2
        ls[0].importStatistics.total shouldBe 2
        ls[0].importStatistics.distinct shouldBe 1
    }
})
