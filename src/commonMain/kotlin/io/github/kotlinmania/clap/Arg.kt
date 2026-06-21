// port-lint: source lib.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.clap

import io.github.kotlinmania.clap.builder.PossibleValue
import io.github.kotlinmania.clap.builder.TypedValueParser
import kotlin.native.HiddenFromObjC

/**
 * Definition for one command-line argument.
 */
class Arg private constructor(
    private val idValue: String,
) {
    internal var shortName: Char? = null
    internal var longName: String? = null
    internal var actionValue: ArgAction = ArgAction.Set
    internal var helpValue: String? = null
    internal var longHelpValue: String? = null
    internal var valueNameValue: String? = null
    internal var requiredValue = false
    internal var indexValue: Int? = null
    internal var valueHintValue: ValueHint = ValueHint.Unknown
    internal val defaultValuesList = mutableListOf<String>()
    internal val visibleAliasesList = mutableListOf<String>()
    internal val visibleShortAliasesList = mutableListOf<Char>()
    internal val possibleValuesList = mutableListOf<PossibleValue>()
    internal var parserValue: TypedValueParser<*>? = null

    fun short(short: Char): Arg =
        apply {
            shortName = short
        }

    fun long(long: String): Arg =
        apply {
            longName = long
        }

    fun action(action: ArgAction): Arg =
        apply {
            actionValue = action
        }

    fun help(help: String?): Arg =
        apply {
            helpValue = help
        }

    fun longHelp(help: String?): Arg =
        apply {
            longHelpValue = help
        }

    fun valueName(name: String?): Arg =
        apply {
            valueNameValue = name
        }

    fun valueParser(values: Iterable<PossibleValue>): Arg =
        apply {
            possibleValuesList.clear()
            possibleValuesList += values
        }

    fun valueParser(values: Array<PossibleValue>): Arg = valueParser(values.asIterable())

    fun valueParserStrings(values: Iterable<String>): Arg =
        apply {
            possibleValuesList.clear()
            possibleValuesList += values.map(PossibleValue::new)
        }

    @HiddenFromObjC
    fun valueParser(parser: TypedValueParser<*>): Arg =
        apply {
            parserValue = parser
            possibleValuesList.clear()
            possibleValuesList += parser.possibleValues()
        }

    fun defaultValue(value: String?): Arg =
        apply {
            defaultValuesList.clear()
            if (value != null) {
                defaultValuesList += value
            }
        }

    fun defaultValues(values: Iterable<String>): Arg =
        apply {
            defaultValuesList.clear()
            defaultValuesList += values
        }

    fun defaultValues(values: Array<String>): Arg = defaultValues(values.asIterable())

    fun visibleAlias(alias: String): Arg =
        apply {
            visibleAliasesList += alias
        }

    fun visibleAliases(aliases: Iterable<String>): Arg =
        apply {
            visibleAliasesList += aliases
        }

    fun visibleAliases(aliases: Array<String>): Arg = visibleAliases(aliases.asIterable())

    fun visibleShortAlias(alias: Char): Arg =
        apply {
            visibleShortAliasesList += alias
        }

    fun visibleShortAliases(aliases: Iterable<Char>): Arg =
        apply {
            visibleShortAliasesList += aliases
        }

    fun visibleShortAliases(aliases: CharArray): Arg = visibleShortAliases(aliases.asIterable())

    fun required(yes: Boolean): Arg =
        apply {
            requiredValue = yes
        }

    fun index(index: Int): Arg =
        apply {
            indexValue = index
        }

    fun valueHint(valueHint: ValueHint): Arg =
        apply {
            valueHintValue = valueHint
        }

    fun getId(): String = idValue

    @HiddenFromObjC
    fun getShort(): Char? = shortName

    fun getLong(): String? = longName

    fun getAction(): ArgAction = actionValue

    fun getHelp(): String? = helpValue

    fun getLongHelp(): String? = longHelpValue

    fun getValueName(): String? = valueNameValue

    fun getDefaultValues(): List<String> = defaultValuesList.toList()

    fun getValueHint(): ValueHint = valueHintValue

    fun getPossibleValues(): List<PossibleValue> = possibleValuesList.toList()

    internal fun matchesLong(name: String): Boolean =
        longName == name || visibleAliasesList.any { it == name }

    internal fun matchesShort(name: Char): Boolean =
        shortName == name || visibleShortAliasesList.any { it == name }

    internal fun validate(value: String) {
        if (possibleValuesList.isNotEmpty() && possibleValuesList.none { it.matches(value, false) }) {
            throw Error(ErrorKind.InvalidValue, "invalid value `$value` for `$idValue`")
        }
        parserValue?.parse(value)?.getOrElse {
            throw Error(ErrorKind.InvalidValue, it.message ?: "invalid value `$value` for `$idValue`")
        }
    }

    companion object {
        fun new(id: String): Arg = Arg(id)
    }
}
