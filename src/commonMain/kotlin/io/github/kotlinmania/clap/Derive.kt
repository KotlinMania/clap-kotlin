// port-lint: source lib.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.clap

import io.github.kotlinmania.clap.builder.PossibleValue
import kotlin.native.HiddenFromObjC

/**
 * Parses command-line arguments into a user-defined value.
 */
@HiddenFromObjC
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
@HiddenFromObjC
interface CommandFactory<T> {
    fun command(): Command

    fun commandForUpdate(): Command = command()
}

/**
 * Converts parsed matches into a user-defined value.
 */
@HiddenFromObjC
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
@HiddenFromObjC
interface Args<T> : FromArgMatches<T> {
    fun groupId(): String? = null

    fun augmentArgs(command: Command): Command

    fun augmentArgsForUpdate(command: Command): Command = augmentArgs(command)
}

/**
 * Appends and parses subcommands for a user-defined value.
 */
@HiddenFromObjC
interface Subcommand<T> : FromArgMatches<T> {
    fun augmentSubcommands(command: Command): Command

    fun augmentSubcommandsForUpdate(command: Command): Command = augmentSubcommands(command)

    fun hasSubcommand(name: String): Boolean
}

/**
 * Parses an enum-like Kotlin value from one of its command-line spellings.
 */
@HiddenFromObjC
interface ValueEnum<T> {
    fun valueVariants(): List<T>

    fun toPossibleValue(value: T): PossibleValue?

    fun fromString(input: String, ignoreCase: Boolean = false): T? =
        valueVariants().firstOrNull { variant ->
            toPossibleValue(variant)?.matches(input, ignoreCase) == true
        }
}
