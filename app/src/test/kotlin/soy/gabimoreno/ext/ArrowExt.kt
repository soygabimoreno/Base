package soy.gabimoreno.ext

import arrow.core.Either

fun <T> right(value: T): Either<Throwable, T> = Either.Right(value)
fun <T> left(error: Throwable): Either<Throwable, T> = Either.Left(error)
