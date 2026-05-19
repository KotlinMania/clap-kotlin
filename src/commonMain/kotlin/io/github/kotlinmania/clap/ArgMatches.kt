// port-lint: source lib.rs
package io.github.kotlinmania.clap

/**
 * Values captured from a parsed command line.
 */
class ArgMatches internal constructor(
    private val values: Map<String, List<String>>,
    private val occurrences: Map<String, Int>,
    private val subcommandValue: Pair<String, ArgMatches>?,
) {
    fun containsId(id: String): Boolean =
        values.containsKey(id) || occurrences.containsKey(id)

    fun getOneString(id: String): String? = values[id]?.lastOrNull()

    inline fun <reified T> getOne(id: String): T? =
        getOneString(id)?.let { convertValue<T>(it) }

    fun getManyStrings(id: String): List<String>? = values[id]

    inline fun <reified T> getMany(id: String): List<T>? =
        getManyStrings(id)?.mapNotNull { convertValue<T>(it) }

    fun getFlag(id: String): Boolean =
        values[id]?.lastOrNull()?.toBooleanStrictOrNull()
            ?: ((occurrences[id] ?: 0) > 0)

    fun getCount(id: String): Int =
        values[id]?.lastOrNull()?.toIntOrNull() ?: occurrences[id] ?: 0

    fun subcommand(): Pair<String, ArgMatches>? = subcommandValue

    companion object {
        fun empty(): ArgMatches = ArgMatches(emptyMap(), emptyMap(), null)
    }
}

inline fun <reified T> convertValue(value: String): T? = when (T::class) {
    String::class -> value as T
    Boolean::class -> value.toBooleanStrictOrNull() as? T
    Byte::class -> value.toByteOrNull() as? T
    Short::class -> value.toShortOrNull() as? T
    Int::class -> value.toIntOrNull() as? T
    Long::class -> value.toLongOrNull() as? T
    Float::class -> value.toFloatOrNull() as? T
    Double::class -> value.toDoubleOrNull() as? T
    else -> null
}
