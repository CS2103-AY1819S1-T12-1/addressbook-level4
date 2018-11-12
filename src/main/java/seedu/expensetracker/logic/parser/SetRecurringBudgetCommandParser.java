package seedu.expensetracker.logic.parser;
//@@author winsonhys
import static seedu.expensetracker.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_HOURS;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_MINUTES;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_SECONDS;

import java.util.stream.Stream;

import seedu.expensetracker.logic.commands.SetRecurringBudgetCommand;
import seedu.expensetracker.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new SetRecurringBudgetCommand object
 */
public class SetRecurringBudgetCommandParser implements Parser<SetRecurringBudgetCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the AddCommand
     * and returns a SetRecurringBudgetCommand object for execution.
     * @param args Arguments to parse
     * @return A new SetRecurringBudgetCommand
     * @throws ParseException
     */
    @Override
    public SetRecurringBudgetCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
            ArgumentTokenizer.tokenize(args, PREFIX_HOURS, PREFIX_MINUTES, PREFIX_SECONDS);
        if (!areAnyPrefixesPresent(argMultimap, PREFIX_HOURS, PREFIX_MINUTES, PREFIX_SECONDS)
            || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                SetRecurringBudgetCommand.MESSAGE_USAGE));
        }
        try {
            long hours = ParserUtil.parseHours(argMultimap.getValue(PREFIX_HOURS).orElse("0"));
            long minutes = ParserUtil.parseMinutes(argMultimap.getValue(PREFIX_MINUTES).orElse("0"));
            long seconds = ParserUtil.parseSeconds(argMultimap.getValue(PREFIX_SECONDS).orElse("0"));
            long totalSecondsToNextRecurrence = hours + minutes + seconds;
            return new SetRecurringBudgetCommand(totalSecondsToNextRecurrence);
        } catch (IllegalArgumentException ce) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                SetRecurringBudgetCommand.MESSAGE_USAGE));
        }
    }
    private static boolean areAnyPrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).anyMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
