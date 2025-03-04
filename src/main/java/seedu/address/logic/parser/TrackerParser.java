package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_KNOWN_COMMANDS;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.ExportCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ImportCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.TagCommand;
import seedu.address.logic.commands.UntagCommand;
import seedu.address.logic.commands.add.AddCommand;
import seedu.address.logic.commands.delete.DeleteCommand;
import seedu.address.logic.commands.edit.EditCommand;
import seedu.address.logic.commands.mark.MarkAsUnwatchedCommand;
import seedu.address.logic.commands.mark.MarkAsWatchedCommand;
import seedu.address.logic.commands.navigation.BackNavCommand;
import seedu.address.logic.commands.navigation.NavCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.logic.parser.navigation.NavCommandParser;

/**
 * Parses user input.
 */
public class TrackerParser {

    /**
     * Used for initial separation of command word and args.
     */
    public static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    /**
     * Parses user input into command for execution.
     *
     * @param userInput full user input string
     * @return the command based on the user input
     * @throws ParseException if the user input does not conform the expected format
     */
    public Command parseCommand(String userInput) throws ParseException {
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");
        switch (commandWord) {
        case NavCommand.COMMAND_WORD:
            return new NavCommandParser().parse(arguments);

        case BackNavCommand.COMMAND_WORD:
            return new BackNavCommand();

        case UntagCommand.COMMAND_WORD:
            return new UntagCommandParser().parse(arguments);

        case TagCommand.COMMAND_WORD:
            return new TagCommandParser().parse(arguments);

        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case MarkAsWatchedCommand.COMMAND_WORD:
            return new MarkAsWatchedCommandParser().parse(arguments);

        case MarkAsUnwatchedCommand.COMMAND_WORD:
            return new MarkAsUnwatchedCommandParser().parse(arguments);

        case ListCommand.COMMAND_WORD:
            return new ListCommandParser().parse(arguments);

        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case ExportCommand.COMMAND_WORD:
            return new ExportCommandParser().parse(arguments);

        case ImportCommand.COMMAND_WORD:
            return new ImportCommandParser().parse(arguments);


        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND + MESSAGE_KNOWN_COMMANDS);
        }
    }
}
