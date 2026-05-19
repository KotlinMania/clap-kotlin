// port-lint: source lib.rs
package io.github.kotlinmania.clap

/**
 * Hints for shells when completing an argument.
 */
enum class ValueHint {
    Unknown,
    Other,
    AnyPath,
    FilePath,
    DirPath,
    ExecutablePath,
    CommandName,
    CommandString,
    CommandWithArguments,
    Username,
    Hostname,
    Url,
    EmailAddress,
    ;

    companion object {
        fun fromString(value: String): ValueHint = when (value.lowercase()) {
            "unknown" -> Unknown
            "other" -> Other
            "anypath" -> AnyPath
            "filepath" -> FilePath
            "dirpath" -> DirPath
            "executablepath" -> ExecutablePath
            "commandname" -> CommandName
            "commandstring" -> CommandString
            "commandwitharguments" -> CommandWithArguments
            "username" -> Username
            "hostname" -> Hostname
            "url" -> Url
            "emailaddress" -> EmailAddress
            else -> throw Error(ErrorKind.InvalidValue, "unknown ValueHint: `$value`")
        }
    }
}
