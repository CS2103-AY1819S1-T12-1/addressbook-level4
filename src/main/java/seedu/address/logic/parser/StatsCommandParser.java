package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_MODE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERIOD;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PERIOD_AMOUNT;
import static seedu.address.logic.parser.ParserUtil.arePrefixesPresent;

import seedu.address.logic.commands.StatsCommand;

import seedu.address.logic.parser.exceptions.ParseException;



//@author jonathantjm

/**
 * Parses input arguments and creates a new StatsCommand object
 */
public class StatsCommandParser implements Parser<StatsCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the LoginCommand
     * and returns an LoginCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public StatsCommand parse(String args) throws ParseException {
        if (args.length() == 0) {
            return new StatsCommand();
        }

        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_PERIOD_AMOUNT, PREFIX_PERIOD, PREFIX_MODE);

        if (!arePrefixesPresent(argMultimap, PREFIX_PERIOD_AMOUNT, PREFIX_PERIOD, PREFIX_MODE)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, StatsCommand.MESSAGE_USAGE));
        }

        int periodAmount = Integer.parseInt(argMultimap.getValue(PREFIX_PERIOD_AMOUNT).get());
        String mode = argMultimap.getValue(PREFIX_MODE).get();
        String period = argMultimap.getValue(PREFIX_PERIOD).get();

        try {
            return new StatsCommand(periodAmount, period, mode);
        } catch (IllegalArgumentException iae) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, StatsCommand.MESSAGE_USAGE));
        }
    }
}
