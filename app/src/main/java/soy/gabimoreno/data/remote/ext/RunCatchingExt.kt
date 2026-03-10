package soy.gabimoreno.data.remote.ext

import arrow.core.Either
import arrow.core.left
import arrow.core.right

inline fun <T> runCatchingEither(block: () -> T): Either<Throwable, T> =
    runCatching(block).fold({ it.right() }, { it.left() })
