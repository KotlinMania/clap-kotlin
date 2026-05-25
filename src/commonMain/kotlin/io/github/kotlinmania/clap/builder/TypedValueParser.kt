// port-lint: source lib.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.clap.builder

import kotlin.native.HiddenFromObjC

/**
 * Parses a string argument value into a Kotlin value.
 */
@HiddenFromObjC
fun interface TypedValueParser<T> {
    fun parse(value: String): Result<T>

    fun possibleValues(): List<PossibleValue> = emptyList()

    fun <R> map(transform: (T) -> R): TypedValueParser<R> = TypedValueParser { value ->
        parse(value).map(transform)
    }
}

@HiddenFromObjC
class StringValueParser : TypedValueParser<String> {
    override fun parse(value: String): Result<String> = Result.success(value)
}

@HiddenFromObjC
class PossibleValuesParser private constructor(
    private val values: List<PossibleValue>,
) : TypedValueParser<String> {
    override fun parse(value: String): Result<String> =
        if (values.any { it.matches(value, ignoreCase = false) }) {
            Result.success(value)
        } else {
            Result.failure(IllegalArgumentException("invalid value: $value"))
        }

    override fun possibleValues(): List<PossibleValue> = values

    companion object {
        fun new(values: Iterable<PossibleValue>): PossibleValuesParser =
            PossibleValuesParser(values.toList())
    }
}

@HiddenFromObjC
fun stringValueParser(): StringValueParser = StringValueParser()

@HiddenFromObjC
fun possibleValuesParser(values: Iterable<PossibleValue>): PossibleValuesParser =
    PossibleValuesParser.new(values)
