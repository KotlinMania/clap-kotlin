// port-lint: source lib.rs
package io.github.kotlinmania.clap.builder

/**
 * Parses a string argument value into a Kotlin value.
 */
fun interface TypedValueParser<T> {
    fun parse(value: String): Result<T>

    fun possibleValues(): List<PossibleValue> = emptyList()

    fun <R> map(transform: (T) -> R): TypedValueParser<R> = TypedValueParser { value ->
        parse(value).map(transform)
    }
}

class StringValueParser : TypedValueParser<String> {
    override fun parse(value: String): Result<String> = Result.success(value)
}

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

fun stringValueParser(): StringValueParser = StringValueParser()

fun possibleValuesParser(values: Iterable<PossibleValue>): PossibleValuesParser =
    PossibleValuesParser.new(values)
