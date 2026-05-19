// port-lint: source lib.rs
package io.github.kotlinmania.clap.builder.styling

/**
 * ANSI foreground colors used when rendering help and errors.
 */
enum class AnsiColor {
    Black,
    Red,
    Green,
    Yellow,
    Blue,
    Magenta,
    Cyan,
    White,
    ;

    fun onDefault(): Style = Style(foreground = this)
}

data class Style(
    val foreground: AnsiColor? = null,
    val bold: Boolean = false,
    val underline: Boolean = false,
    val dimmed: Boolean = false,
    val italic: Boolean = false,
) {
    fun bold(): Style = copy(bold = true)

    fun underline(): Style = copy(underline = true)

    fun dimmed(): Style = copy(dimmed = true)

    fun italic(): Style = copy(italic = true)
}
