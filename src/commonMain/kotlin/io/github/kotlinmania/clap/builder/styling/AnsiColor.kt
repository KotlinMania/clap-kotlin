// port-lint: source lib.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.clap.builder.styling

import kotlin.native.HiddenFromObjC

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
    @HiddenFromObjC
    fun bold(): Style = copy(bold = true)

    @HiddenFromObjC
    fun underline(): Style = copy(underline = true)

    @HiddenFromObjC
    fun dimmed(): Style = copy(dimmed = true)

    @HiddenFromObjC
    fun italic(): Style = copy(italic = true)
}
