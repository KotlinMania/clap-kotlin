// port-lint: source lib.rs
package io.github.kotlinmania.clap

import io.github.kotlinmania.clap.builder.PossibleValue
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class CommandTest {
    @Test
    fun parsesFlagsOptionsAndPositionals() {
        val command =
            Command
                .new("demo")
                .arg(
                    Arg
                        .new("verbose")
                        .long("verbose")
                        .short('v')
                        .action(ArgAction.Count),
                ).arg(
                    Arg
                        .new("config")
                        .long("config")
                        .short('c')
                        .action(ArgAction.Set),
                ).arg(Arg.new("name").index(1))

        val matches = command.getMatchesFrom(listOf("demo", "-vv", "--config", "fast", "alice"))

        assertEquals(2, matches.getCount("verbose"))
        assertEquals("fast", matches.getOne<String>("config"))
        assertEquals("alice", matches.getOne<String>("name"))
    }

    @Test
    fun seedsBooleanDefaults() {
        val command =
            Command
                .new("demo")
                .arg(Arg.new("debug").long("debug").action(ArgAction.SetTrue))

        val absent = command.getMatchesFrom(listOf("demo"))
        val present = command.getMatchesFrom(listOf("demo", "--debug"))

        assertFalse(absent.getFlag("debug"))
        assertTrue(present.getFlag("debug"))
    }

    @Test
    fun validatesPossibleValues() {
        val command =
            Command
                .new("demo")
                .arg(
                    Arg
                        .new("mode")
                        .long("mode")
                        .valueParser(
                            listOf(
                                PossibleValue.new("fast"),
                                PossibleValue.new("slow").help("slower than fast"),
                            ),
                        ),
                )

        val matches = command.getMatchesFrom(listOf("demo", "--mode", "slow"))
        val failure = command.tryGetMatchesFrom(listOf("demo", "--mode", "secret"))

        assertEquals("slow", matches.getOne<String>("mode"))
        assertTrue(failure.isFailure)
    }

    @Test
    fun capturesSubcommandMatches() {
        val command =
            Command
                .new("demo")
                .subcommand(Command.new("run").arg(Arg.new("target").index(1)))

        val subcommand = command.getMatchesFrom(listOf("demo", "run", "tests")).subcommand()

        assertNotNull(subcommand)
        assertEquals("run", subcommand.first)
        assertEquals("tests", subcommand.second.getOne<String>("target"))
    }
}
