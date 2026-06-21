// port-lint: source lib.rs
package io.github.kotlinmania.clap

/**
 * Behavior of arguments when they are encountered while parsing.
 */
enum class ArgAction {
    Set,
    Append,
    SetTrue,
    SetFalse,
    Count,
    Help,
    HelpShort,
    HelpLong,
    Version,
    ;

    fun takesValues(): Boolean =
        when (this) {
            Set, Append -> true
            SetTrue, SetFalse, Count, Help, HelpShort, HelpLong, Version -> false
        }

    internal fun defaultValue(): String? =
        when (this) {
            Set, Append, Help, HelpShort, HelpLong, Version -> null
            SetTrue -> "false"
            SetFalse -> "true"
            Count -> "0"
        }

    internal fun presentValue(current: String?): String =
        when (this) {
            SetTrue -> "true"
            SetFalse -> "false"
            Count -> ((current?.toIntOrNull() ?: 0) + 1).toString()
            Help, HelpShort, HelpLong -> throw Error(ErrorKind.DisplayHelp, "display help")
            Version -> throw Error(ErrorKind.DisplayVersion, "display version")
            Set, Append -> current ?: ""
        }
}
