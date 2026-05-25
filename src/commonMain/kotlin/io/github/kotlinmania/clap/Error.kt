// port-lint: source lib.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.clap

import kotlin.native.HiddenFromObjC

/**
 * Kinds of command-line parsing errors that can be reported by this port.
 */
enum class ErrorKind {
    UnknownArgument,
    MissingValue,
    InvalidValue,
    DisplayHelp,
    DisplayVersion,
    MissingSubcommand,
    ArgumentConflict,
}

/**
 * A command-line parsing error.
 */
@HiddenFromObjC
class Error(
    val kind: ErrorKind,
    message: String,
) : RuntimeException(message) {
    fun kind(): ErrorKind = kind

    fun format(command: Command): Error {
        command.getName()
        return this
    }

    fun exit(): Nothing = throw this

    companion object {
        fun raw(kind: ErrorKind, message: String): Error = Error(kind, message)
    }
}
