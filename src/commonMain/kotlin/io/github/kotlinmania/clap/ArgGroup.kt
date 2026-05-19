// port-lint: source lib.rs
package io.github.kotlinmania.clap

/**
 * A named relationship between arguments.
 */
class ArgGroup private constructor(
    private val idValue: String,
) {
    private val members = mutableListOf<String>()
    private var requiredValue = false
    private var multipleValue = false

    fun arg(id: String): ArgGroup = apply {
        members += id
    }

    fun args(ids: Iterable<String>): ArgGroup = apply {
        members += ids
    }

    fun required(yes: Boolean): ArgGroup = apply {
        requiredValue = yes
    }

    fun multiple(yes: Boolean): ArgGroup = apply {
        multipleValue = yes
    }

    fun getId(): String = idValue

    fun getArgs(): List<String> = members.toList()

    fun isRequiredSet(): Boolean = requiredValue

    fun isMultipleSet(): Boolean = multipleValue

    companion object {
        fun new(id: String): ArgGroup = ArgGroup(id)
    }
}
