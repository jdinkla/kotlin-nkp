package net.dinkla.nkp.utilities

import io.github.oshai.kotlinlogging.KotlinLogging
import net.dinkla.nkp.domain.AnalysedFile

internal fun reportErrors(infos: List<Result<AnalysedFile>>) {
    infos.groupBy { it.isSuccess }.forEach {
        logger.info { "${if (it.key) "Successful" else "With error"}: ${it.value.size}" }
    }
}

private val logger = KotlinLogging.logger {}

