// port-lint: source lib.rs
package io.github.kotlinmania.clap

import io.github.kotlinmania.clap.builder.PossibleValue

/**
 * Parses command-line arguments into a user-defined value.
 */
interface Parser<T> : FromArgMatches<T>, CommandFactory<T> {
    fun parseFrom(arguments: Iterable<String>): T {
        val matches = command().getMatchesFrom(arguments)
        return fromArgMatches(matches)
    }

    fun tryParseFrom(arguments: Iterable<String>): Result<T> =
        command().tryGetMatchesFrom(arguments).mapCatching(::fromArgMatches)

    fun updateFrom(value: T, arguments: Iterable<String>): T {
        val matches = commandForUpdate().getMatchesFrom(arguments)
        return updateFromArgMatches(value, matches)
    }

    fun tryUpdateFrom(value: T, arguments: Iterable<String>): Result<T> =
        commandForUpdate().tryGetMatchesFrom(arguments).mapCatching { updateFromArgMatches(value, it) }
}

/**
 * Creates a command definition for a user-defined value.
 */
interface CommandFactory<T> {
    fun command(): Command

    fun commandForUpdate(): Command = command()
}

/**
 * Converts parsed matches into a user-defined value.
 */
interface FromArgMatches<T> {
    fun fromArgMatches(matches: ArgMatches): T

    fun updateFromArgMatches(value: T, matches: ArgMatches): T {
        value.hashCode()
        return fromArgMatches(matches)
    }
}

/**
 * Appends reusable arguments to a command.
 */
interface Args<T> : FromArgMatches<T> {
    fun groupId(): String? = null

    fun augmentArgs(command: Command): Command

    fun augmentArgsForUpdate(command: Command): Command = augmentArgs(command)
}

/**
 * Appends and parses subcommands for a user-defined value.
 */
interface Subcommand<T> : FromArgMatches<T> {
    fun augmentSubcommands(command: Command): Command

    fun augmentSubcommandsForUpdate(command: Command): Command = augmentSubcommands(command)

    fun hasSubcommand(name: String): Boolean
}

/**
 * Parses an enum-like Kotlin value from one of its command-line spellings.
 */
interface ValueEnum<T> {
    fun valueVariants(): List<T>

    fun toPossibleValue(value: T): PossibleValue?

    fun fromString(input: String, ignoreCase: Boolean = false): T? =
        valueVariants().firstOrNull { variant ->
            toPossibleValue(variant)?.matches(input, ignoreCase) == true
        }
}
