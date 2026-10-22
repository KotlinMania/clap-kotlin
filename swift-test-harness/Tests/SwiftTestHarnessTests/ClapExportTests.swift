import XCTest
import Clap

final class ClapExportTests: XCTestCase {

    func testArgActionEnumAndBehavior() throws {
        XCTAssertEqual(ArgAction.allCases.count, 9)
        XCTAssertEqual(ArgAction.Set.rawValue, 0)
        XCTAssertEqual(ArgAction.Append.rawValue, 1)
        XCTAssertEqual(ArgAction.SetTrue.rawValue, 2)
        XCTAssertEqual(ArgAction.SetFalse.rawValue, 3)
        XCTAssertEqual(ArgAction.Count.rawValue, 4)
        XCTAssertEqual(ArgAction.Help.rawValue, 5)
        XCTAssertEqual(ArgAction.HelpShort.rawValue, 6)
        XCTAssertEqual(ArgAction.HelpLong.rawValue, 7)
        XCTAssertEqual(ArgAction.Version.rawValue, 8)

        XCTAssertTrue(ArgAction.Set.takesValues())
        XCTAssertTrue(ArgAction.Append.takesValues())
        XCTAssertFalse(ArgAction.SetTrue.takesValues())
        XCTAssertFalse(ArgAction.SetFalse.takesValues())
        XCTAssertFalse(ArgAction.Count.takesValues())

        XCTAssertEqual(ArgAction("Set"), .Set)
        XCTAssertEqual(ArgAction(rawValue: 2), .SetTrue)
        XCTAssertEqual(ArgAction.Count.description, "Count")
    }

    func testErrorKindEnum() throws {
        XCTAssertEqual(ErrorKind.allCases.count, 7)
        XCTAssertEqual(ErrorKind.UnknownArgument.rawValue, 0)
        XCTAssertEqual(ErrorKind.MissingValue.rawValue, 1)
        XCTAssertEqual(ErrorKind.InvalidValue.rawValue, 2)
        XCTAssertEqual(ErrorKind.DisplayHelp.rawValue, 3)
        XCTAssertEqual(ErrorKind.DisplayVersion.rawValue, 4)
        XCTAssertEqual(ErrorKind.MissingSubcommand.rawValue, 5)
        XCTAssertEqual(ErrorKind.ArgumentConflict.rawValue, 6)

        XCTAssertEqual(ErrorKind("InvalidValue"), .InvalidValue)
        XCTAssertEqual(ErrorKind(rawValue: 5), .MissingSubcommand)
        XCTAssertEqual(ErrorKind.DisplayHelp.description, "DisplayHelp")
    }

    func testValueHintEnum() throws {
        XCTAssertEqual(ValueHint.allCases.count, 13)
        XCTAssertEqual(ValueHint.FilePath.rawValue, 3)
        XCTAssertEqual(ValueHint.DirPath.rawValue, 4)
        XCTAssertEqual(ValueHint.Url.rawValue, 11)

        XCTAssertEqual(ValueHint("FilePath"), .FilePath)
        XCTAssertEqual(ValueHint(rawValue: 12), .EmailAddress)
        XCTAssertEqual(ValueHint.Username.description, "Username")
    }

    func testPossibleValueApi() throws {
        let pv = builder.PossibleValue.Companion.shared.new(name: "release")
            .help(help: "Build in release mode")
            .alias(name: "rel")

        XCTAssertEqual(pv.getName(), "release")
        XCTAssertEqual(pv.getNameAndAliases(), ["release", "rel"])
        XCTAssertTrue(pv.matches(value: "release", ignoreCase: false))
        XCTAssertTrue(pv.matches(value: "RELEASE", ignoreCase: true))
        XCTAssertFalse(pv.matches(value: "debug", ignoreCase: false))
        XCTAssertFalse(pv.isHideSet())
        XCTAssertTrue(pv.shouldShowHelp())

        let hiddenPv = pv.hide(yes: true)
        XCTAssertTrue(hiddenPv.isHideSet())
        XCTAssertFalse(hiddenPv.shouldShowHelp())
    }

    func testStyledStrApi() throws {
        let styled = builder.StyledStr(text: "Antigravity CLI")
        XCTAssertEqual(styled.asString(), "Antigravity CLI")
        XCTAssertEqual(styled.toString(), "Antigravity CLI")

        let styledFrom = builder.StyledStr.Companion.shared.from(value: "From Companion")
        XCTAssertEqual(styledFrom.asString(), "From Companion")
    }

    func testArgConstructionAndInspection() throws {
        let shortChar = Character("c").utf16.first!
        let arg = Arg.Companion.shared.new(id: "config")
            .short(short: shortChar)
            .long(long: "config")
            .help(help: "Path to configuration file")
            .action(action: .Set)
            .valueHint(valueHint: .FilePath)
            .defaultValue(value: "default.cfg")
            .required(yes: true)

        XCTAssertEqual(arg.getId(), "config")
        XCTAssertEqual(arg.getLong(), "config")
        XCTAssertEqual(arg.getHelp(), "Path to configuration file")
        XCTAssertEqual(arg.getAction(), .Set)
        XCTAssertEqual(arg.getValueHint(), .FilePath)
        XCTAssertEqual(arg.getDefaultValues(), ["default.cfg"])
    }

    func testArgGroupConstructionAndInspection() throws {
        let group = ArgGroup.Companion.shared.new(id: "mode")
            .arg(id: "fast")
            .arg(id: "safe")
            .required(yes: true)
            .multiple(yes: false)

        XCTAssertEqual(group.getId(), "mode")
        XCTAssertEqual(group.getArgs(), ["fast", "safe"])
        XCTAssertTrue(group.isRequiredSet())
        XCTAssertFalse(group.isMultipleSet())
    }

    func testCommandConstructionAndHierarchy() throws {
        let verboseChar = Character("v").utf16.first!
        let verboseArg = Arg.Companion.shared.new(id: "verbose")
            .short(short: verboseChar)
            .long(long: "verbose")
            .action(action: .Count)
            .help(help: "Increase verbosity level")

        let subcmd = Command.Companion.shared.new(name: "status")
            .about(about: "Show system status")

        let app = Command.Companion.shared.new(name: "my-cli")
            .version(version: "1.2.3")
            .about(about: "Modern command-line parser")
            .longAbout(about: "A full-featured command-line argument parser port for Kotlin and Swift")
            .arg(arg: verboseArg)
            .subcommand(command: subcmd)

        XCTAssertEqual(app.getName(), "my-cli")
        XCTAssertEqual(app.getVersion(), "1.2.3")
        XCTAssertEqual(app.getAbout(), "Modern command-line parser")
        XCTAssertEqual(app.getLongAbout(), "A full-featured command-line argument parser port for Kotlin and Swift")

        let args = app.getArguments()
        XCTAssertEqual(args.count, 1)
        XCTAssertEqual(args.first?.getId(), "verbose")
        XCTAssertEqual(args.first?.getAction(), .Count)

        let subcommands = app.getSubcommands()
        XCTAssertEqual(subcommands.count, 1)
        XCTAssertEqual(subcommands.first?.getName(), "status")
        XCTAssertEqual(subcommands.first?.getAbout(), "Show system status")
    }

    func testStylingAndColorPalette() throws {
        let red = builder.styling.AnsiColor.Red
        XCTAssertEqual(red.rawValue, 1)
        XCTAssertEqual(red.description, "Red")

        let defaultStyle = red.onDefault()
        XCTAssertEqual(defaultStyle.foreground, .Red)
        XCTAssertFalse(defaultStyle.bold)

        let boldGreen = builder.styling.Style(
            foreground: .Green,
            bold: true,
            underline: false,
            dimmed: false,
            italic: false
        )
        XCTAssertEqual(boldGreen.foreground, .Green)
        XCTAssertTrue(boldGreen.bold)
        XCTAssertFalse(boldGreen.underline)
    }
}
