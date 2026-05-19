// port-lint: source lib.rs
package io.github.kotlinmania.clap.builder

import io.github.kotlinmania.clap.Arg
import io.github.kotlinmania.clap.Command

interface ArgExt {
    fun augmentArg(arg: Arg): Arg = arg
}

interface CommandExt {
    fun augmentCommand(command: Command): Command = command
}
