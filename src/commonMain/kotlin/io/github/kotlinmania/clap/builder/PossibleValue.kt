// port-lint: source lib.rs
package io.github.kotlinmania.clap.builder

/**
 * A possible value of an argument.
 */
class PossibleValue private constructor(
    private val name: String,
) {
    private var helpValue: StyledStr? = null
    private val aliasValues = mutableListOf<String>()
    private var hidden = false

    fun help(help: String?): PossibleValue = apply {
        helpValue = help?.let(::StyledStr)
    }

    fun help(help: StyledStr?): PossibleValue = apply {
        helpValue = help
    }

    fun hide(yes: Boolean): PossibleValue = apply {
        hidden = yes
    }

    fun alias(name: String?): PossibleValue = apply {
        if (name == null) {
            aliasValues.clear()
        } else {
            aliasValues += name
        }
    }

    fun aliases(names: Iterable<String>): PossibleValue = apply {
        aliasValues += names
    }

    fun getName(): String = name

    fun getHelp(): StyledStr? = helpValue

    fun isHideSet(): Boolean = hidden

    fun shouldShowHelp(): Boolean = !hidden && helpValue != null

    fun getNameAndAliases(): List<String> = listOf(name) + aliasValues

    fun matches(value: String, ignoreCase: Boolean): Boolean =
        getNameAndAliases().any { it.equals(value, ignoreCase = ignoreCase) }

    companion object {
        fun new(name: String): PossibleValue = PossibleValue(name)
    }
}
