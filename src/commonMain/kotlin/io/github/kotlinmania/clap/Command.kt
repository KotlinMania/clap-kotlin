// port-lint: source lib.rs
package io.github.kotlinmania.clap

import io.github.kotlinmania.clap.builder.styling.Styles

/**
 * A command-line parser definition.
 */
class Command private constructor(
    private var nameValue: String,
) {
    private val args = mutableListOf<Arg>()
    private val subcommands = mutableListOf<Command>()
    private val groups = mutableListOf<ArgGroup>()
    private val visibleAliasesList = mutableListOf<String>()
    private val visibleLongFlagAliasesList = mutableListOf<String>()
    private val visibleShortFlagAliasesList = mutableListOf<Char>()
    private var versionValue: String? = null
    private var longVersionValue: String? = null
    private var aboutValue: String? = null
    private var longAboutValue: String? = null
    private var shortFlagValue: Char? = null
    private var longFlagValue: String? = null
    private var termWidthValue: Int? = null
    private var maxTermWidthValue: Int? = null
    private var argRequiredElseHelpValue = false
    private var trailingVarArgValue = false
    private var allowExternalSubcommandsValue = false
    private var stylesValue: Styles? = null

    fun name(name: String): Command = apply {
        nameValue = name
    }

    fun version(version: String?): Command = apply {
        versionValue = version
    }

    fun longVersion(version: String?): Command = apply {
        longVersionValue = version
    }

    fun about(about: String?): Command = apply {
        aboutValue = about
    }

    fun longAbout(about: String?): Command = apply {
        longAboutValue = about
    }

    fun termWidth(width: Int): Command = apply {
        termWidthValue = width
    }

    fun maxTermWidth(width: Int): Command = apply {
        maxTermWidthValue = width
    }

    fun argRequiredElseHelp(yes: Boolean): Command = apply {
        argRequiredElseHelpValue = yes
    }

    fun trailingVarArg(yes: Boolean): Command = apply {
        trailingVarArgValue = yes
    }

    fun allowExternalSubcommands(yes: Boolean): Command = apply {
        allowExternalSubcommandsValue = yes
    }

    fun shortFlag(flag: Char): Command = apply {
        shortFlagValue = flag
    }

    fun longFlag(flag: String): Command = apply {
        longFlagValue = flag
    }

    fun visibleAlias(alias: String): Command = apply {
        visibleAliasesList += alias
    }

    fun visibleAliases(aliases: Iterable<String>): Command = apply {
        visibleAliasesList += aliases
    }

    fun visibleAliases(aliases: Array<String>): Command = visibleAliases(aliases.asIterable())

    fun visibleLongFlagAliases(aliases: Iterable<String>): Command = apply {
        visibleLongFlagAliasesList += aliases
    }

    fun visibleLongFlagAliases(aliases: Array<String>): Command =
        visibleLongFlagAliases(aliases.asIterable())

    fun visibleShortFlagAliases(aliases: Iterable<Char>): Command = apply {
        visibleShortFlagAliasesList += aliases
    }

    fun visibleShortFlagAliases(aliases: CharArray): Command =
        visibleShortFlagAliases(aliases.asIterable())

    fun styles(styles: Styles): Command = apply {
        stylesValue = styles
    }

    fun arg(arg: Arg): Command = apply {
        args += arg
    }

    fun args(args: Iterable<Arg>): Command = apply {
        this.args += args
    }

    fun group(group: ArgGroup): Command = apply {
        groups += group
    }

    fun subcommand(command: Command): Command = apply {
        subcommands += command
    }

    fun getName(): String = nameValue

    fun getVersion(): String? = versionValue

    fun getLongVersion(): String? = longVersionValue

    fun getAbout(): String? = aboutValue

    fun getLongAbout(): String? = longAboutValue

    fun getTermWidth(): Int? = termWidthValue

    fun getMaxTermWidth(): Int? = maxTermWidthValue

    fun isArgRequiredElseHelpSet(): Boolean = argRequiredElseHelpValue

    fun isTrailingVarArgSet(): Boolean = trailingVarArgValue

    fun isAllowExternalSubcommandsSet(): Boolean = allowExternalSubcommandsValue

    fun getShortFlag(): Char? = shortFlagValue

    fun getLongFlag(): String? = longFlagValue

    fun getVisibleAliases(): List<String> = visibleAliasesList.toList()

    fun getVisibleLongFlagAliases(): List<String> = visibleLongFlagAliasesList.toList()

    fun getVisibleShortFlagAliases(): List<Char> = visibleShortFlagAliasesList.toList()

    fun getArguments(): List<Arg> = args.toList()

    fun getSubcommands(): List<Command> = subcommands.toList()

    fun getGroups(): List<ArgGroup> = groups.toList()

    fun getStyles(): Styles? = stylesValue

    fun getMatchesFrom(arguments: Iterable<String>): ArgMatches =
        tryGetMatchesFrom(arguments).getOrThrow()

    fun tryGetMatchesFrom(arguments: Iterable<String>): Result<ArgMatches> =
        runCatching { parse(arguments.toList()) }

    fun getMatches(): ArgMatches = getMatchesFrom(listOf(nameValue))

    fun tryGetMatches(): Result<ArgMatches> = tryGetMatchesFrom(listOf(nameValue))

    private fun parse(input: List<String>): ArgMatches {
        val values = linkedMapOf<String, MutableList<String>>()
        val occurrences = linkedMapOf<String, Int>()
        seedDefaults(values, occurrences)

        var position = 0
        var index = if (input.firstOrNull() == nameValue) 1 else 0
        if (argRequiredElseHelpValue && index >= input.size) {
            throw Error(ErrorKind.DisplayHelp, "display help")
        }
        var subcommand: Pair<String, ArgMatches>? = null
        while (index < input.size) {
            val token = input[index]
            when {
                token == "--" -> {
                    index += 1
                    while (index < input.size) {
                        val positional = positionalArg(position)
                        if (positional != null) {
                            pushValue(values, positional, input[index])
                        }
                        position += 1
                        index += 1
                    }
                }
                token.startsWith("--") && token.length > 2 -> {
                    val raw = token.drop(2)
                    val split = raw.indexOf('=')
                    val name = if (split >= 0) raw.take(split) else raw
                    val explicit = if (split >= 0) raw.drop(split + 1) else null
                    val arg = args.firstOrNull { it.matchesLong(name) }
                    if (arg != null) {
                        index = consumeLong(arg, explicit, input, index, values, occurrences)
                    } else {
                        val command = subcommands.firstOrNull { it.matchesLongFlag(name) }
                        if (command != null) {
                            subcommand = command.getName() to command.getMatchesFrom(input.drop(index + 1))
                            index = input.size
                        } else if (!allowExternalSubcommandsValue) {
                            throw Error(ErrorKind.UnknownArgument, "unknown argument `--$name`")
                        } else {
                            subcommand = name to ArgMatches.empty()
                            index = input.size
                        }
                    }
                }
                token.startsWith("-") && token.length > 1 -> {
                    val shorts = token.drop(1)
                    var offset = 0
                    while (offset < shorts.length) {
                        val name = shorts[offset]
                        val arg = args.firstOrNull { it.matchesShort(name) }
                        if (arg != null) {
                            val isLast = offset == shorts.lastIndex
                            val nextIndex = consumeShort(arg, isLast, input, index, values, occurrences)
                            if (nextIndex != index + 1) {
                                index = nextIndex - 1
                                offset = shorts.length
                            }
                        } else {
                            val command = subcommands.firstOrNull { it.matchesShortFlag(name) }
                            if (command != null) {
                                subcommand = command.getName() to command.getMatchesFrom(input.drop(index + 1))
                                index = input.size - 1
                                offset = shorts.length
                            } else if (!allowExternalSubcommandsValue) {
                                throw Error(ErrorKind.UnknownArgument, "unknown argument `-$name`")
                            }
                        }
                        offset += 1
                    }
                    index += 1
                }
                else -> {
                    val command = subcommands.firstOrNull { it.matchesName(token) }
                    if (command != null) {
                        subcommand = command.getName() to command.getMatchesFrom(input.drop(index))
                        index = input.size
                    } else {
                        val positional = positionalArg(position)
                        if (positional != null) {
                            pushValue(values, positional, token)
                        } else if (allowExternalSubcommandsValue) {
                            subcommand = token to ArgMatches.empty()
                            index = input.size
                        } else if (!trailingVarArgValue) {
                            throw Error(ErrorKind.UnknownArgument, "unexpected argument `$token`")
                        }
                        position += 1
                        index += 1
                    }
                }
            }
        }

        for (arg in args) {
            if (arg.requiredValue && !values.containsKey(arg.getId()) && !occurrences.containsKey(arg.getId())) {
                throw Error(ErrorKind.MissingValue, "missing required argument `${arg.getId()}`")
            }
        }

        return ArgMatches(
            values = values.mapValues { it.value.toList() },
            occurrences = occurrences.toMap(),
            subcommandValue = subcommand,
        )
    }

    private fun seedDefaults(values: MutableMap<String, MutableList<String>>, occurrences: MutableMap<String, Int>) {
        for (arg in args) {
            if (arg.defaultValuesList.isNotEmpty()) {
                values[arg.getId()] = arg.defaultValuesList.toMutableList()
            } else {
                val default = arg.actionValue.defaultValue()
                if (default != null) {
                    values[arg.getId()] = mutableListOf(default)
                }
            }
            if (arg.actionValue == ArgAction.Count) {
                occurrences[arg.getId()] = values[arg.getId()]?.lastOrNull()?.toIntOrNull() ?: 0
            }
        }
    }

    private fun consumeLong(
        arg: Arg,
        explicit: String?,
        input: List<String>,
        index: Int,
        values: MutableMap<String, MutableList<String>>,
        occurrences: MutableMap<String, Int>,
    ): Int {
        if (arg.actionValue.takesValues()) {
            val value = explicit ?: input.getOrNull(index + 1)
                ?: throw Error(ErrorKind.MissingValue, "missing value for `--${arg.getLong() ?: arg.getId()}`")
            pushValue(values, arg, value)
            return if (explicit == null) index + 2 else index + 1
        }
        val current = values[arg.getId()]?.lastOrNull()
        setFlagValue(values, occurrences, arg, arg.actionValue.presentValue(current))
        return index + 1
    }

    private fun consumeShort(
        arg: Arg,
        isLast: Boolean,
        input: List<String>,
        index: Int,
        values: MutableMap<String, MutableList<String>>,
        occurrences: MutableMap<String, Int>,
    ): Int {
        if (arg.actionValue.takesValues()) {
            if (!isLast) {
                throw Error(ErrorKind.MissingValue, "missing value for `-${arg.getShort() ?: arg.getId()}`")
            }
            val value = input.getOrNull(index + 1)
                ?: throw Error(ErrorKind.MissingValue, "missing value for `-${arg.getShort() ?: arg.getId()}`")
            pushValue(values, arg, value)
            return index + 2
        }
        val current = values[arg.getId()]?.lastOrNull()
        setFlagValue(values, occurrences, arg, arg.actionValue.presentValue(current))
        return index + 1
    }

    private fun setFlagValue(
        values: MutableMap<String, MutableList<String>>,
        occurrences: MutableMap<String, Int>,
        arg: Arg,
        value: String,
    ) {
        values[arg.getId()] = mutableListOf(value)
        occurrences[arg.getId()] = value.toIntOrNull() ?: occurrences[arg.getId()] ?: 1
    }

    private fun pushValue(values: MutableMap<String, MutableList<String>>, arg: Arg, value: String) {
        arg.validate(value)
        if (arg.actionValue == ArgAction.Append) {
            values.getOrPut(arg.getId()) { mutableListOf() } += value
        } else {
            values[arg.getId()] = mutableListOf(value)
        }
    }

    private fun positionalArg(position: Int): Arg? {
        val explicit = args.firstOrNull { it.indexValue == position + 1 }
        if (explicit != null) {
            return explicit
        }
        return args.filter { it.getShort() == null && it.getLong() == null }
            .getOrNull(position)
    }

    private fun matchesName(token: String): Boolean =
        nameValue == token || visibleAliasesList.any { it == token }

    private fun matchesLongFlag(token: String): Boolean =
        longFlagValue == token || visibleLongFlagAliasesList.any { it == token }

    private fun matchesShortFlag(token: Char): Boolean =
        shortFlagValue == token || visibleShortFlagAliasesList.any { it == token }

    companion object {
        fun new(name: String): Command = Command(name)
    }
}
