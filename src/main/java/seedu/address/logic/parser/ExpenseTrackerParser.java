package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import seedu.address.logic.commands.AddCategoryBudgetCommand;
import seedu.address.logic.commands.AddCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.Command;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.HistoryCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.LoginCommand;
import seedu.address.logic.commands.MassEditCommand;
import seedu.address.logic.commands.NotificationCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.SelectCommand;
import seedu.address.logic.commands.SetBudgetCommand;
import seedu.address.logic.commands.SetPasswordCommand;
import seedu.address.logic.commands.SetRecurringBudgetCommand;
import seedu.address.logic.commands.SignUpCommand;
import seedu.address.logic.commands.StatsCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses user input.
 */
public class ExpenseTrackerParser {

    /**
     * Used for initial separation of command word and args.
     */
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

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

        case AddCommand.COMMAND_ALIAS:
        case AddCommand.COMMAND_WORD:
            return new AddCommandParser().parse(arguments);

        case EditCommand.COMMAND_ALIAS:
        case EditCommand.COMMAND_WORD:
            return new EditCommandParser().parse(arguments);

        case MassEditCommand.COMMAND_ALIAS:
        case MassEditCommand.COMMAND_WORD:
            return new MassEditCommandParser().parse(arguments);

        case SelectCommand.COMMAND_ALIAS:
        case SelectCommand.COMMAND_WORD:
            return new SelectCommandParser().parse(arguments);

        case DeleteCommand.COMMAND_ALIAS:
        case DeleteCommand.COMMAND_WORD:
            return new DeleteCommandParser().parse(arguments);

        case ClearCommand.COMMAND_ALIAS:
        case ClearCommand.COMMAND_WORD:
            return new ClearCommand();

        case FindCommand.COMMAND_ALIAS:
        case FindCommand.COMMAND_WORD:
            return new FindCommandParser().parse(arguments);

        case ListCommand.COMMAND_ALIAS:
        case ListCommand.COMMAND_WORD:
            return new ListCommand();

        case HistoryCommand.COMMAND_ALIAS:
        case HistoryCommand.COMMAND_WORD:
            return new HistoryCommand();

        case ExitCommand.COMMAND_ALIAS:
        case ExitCommand.COMMAND_WORD:
            return new ExitCommand();

        case HelpCommand.COMMAND_WORD:
            return new HelpCommand();

        case UndoCommand.COMMAND_ALIAS:
        case UndoCommand.COMMAND_WORD:
            return new UndoCommand();

        case RedoCommand.COMMAND_ALIAS:
        case RedoCommand.COMMAND_WORD:
            return new RedoCommand();

        case LoginCommand.COMMAND_WORD:
        case LoginCommand.COMMAND_ALIAS:
            return new LoginCommandParser().parse(arguments);

        case SignUpCommand.COMMAND_WORD:
        case SignUpCommand.COMMAND_ALIAS:
            return new SignUpCommandParser().parse(arguments);

        case SetBudgetCommand.COMMAND_WORD:
        case SetBudgetCommand.COMMAND_ALIAS:
            return new SetBudgetCommandParser().parse(arguments);

        case StatsCommand.COMMAND_WORD:
        case StatsCommand.COMMAND_ALIAS:
            return new StatsCommandParser().parse(arguments);

        case SetRecurringBudgetCommand.COMMAND_WORD:
        case SetRecurringBudgetCommand.COMMAND_ALIAS:
            return new SetRecurringBudgetCommandParser().parse(arguments);

        case AddCategoryBudgetCommand.COMMAND_WORD:
        case AddCategoryBudgetCommand.COMMAND_ALIAS:
            return new AddCategoryBudgetCommandParser().parse(arguments);

        case SetPasswordCommand.COMMAND_WORD:
        case SetPasswordCommand.COMMAND_ALIAS:
            return new SetPasswordCommandParser().parse(arguments);

        case NotificationCommand.COMMAND_WORD:
        case NotificationCommand.COMMAND_ALIAS:
            return new NotificationCommandParser().parse(arguments);

        default:
            throw new ParseException(MESSAGE_UNKNOWN_COMMAND);
        }
    }

}
