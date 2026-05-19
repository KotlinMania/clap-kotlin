// port-lint: source lib.rs
package io.github.kotlinmania.clap.builder.styling

/**
 * Styling used when rendering command help and errors.
 */
class Styles private constructor() {
    private val entries = mutableMapOf<String, Style>()

    fun header(style: Style): Styles = apply { entries["header"] = style }

    fun error(style: Style): Styles = apply { entries["error"] = style }

    fun usage(style: Style): Styles = apply { entries["usage"] = style }

    fun literal(style: Style): Styles = apply { entries["literal"] = style }

    fun placeholder(style: Style): Styles = apply { entries["placeholder"] = style }

    fun valid(style: Style): Styles = apply { entries["valid"] = style }

    fun invalid(style: Style): Styles = apply { entries["invalid"] = style }

    fun context(style: Style): Styles = apply { entries["context"] = style }

    fun contextValue(style: Style): Styles = apply { entries["context_value"] = style }

    fun get(name: String): Style? = entries[name]

    companion object {
        fun styled(): Styles = Styles()
    }
}
