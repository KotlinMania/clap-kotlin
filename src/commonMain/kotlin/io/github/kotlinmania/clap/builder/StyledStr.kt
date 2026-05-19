// port-lint: source lib.rs
package io.github.kotlinmania.clap.builder

/**
 * Text that may carry styling metadata.
 */
class StyledStr(
    private val text: String = "",
) {
    fun asString(): String = text

    override fun toString(): String = text

    companion object {
        fun from(value: String): StyledStr = StyledStr(value)
    }
}
