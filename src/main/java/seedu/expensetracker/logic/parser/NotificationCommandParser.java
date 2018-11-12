package seedu.expensetracker.logic.parser;

import static seedu.expensetracker.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_NOTIFICATION_TYPE;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_TOGGLE;
import static seedu.expensetracker.logic.parser.ParserUtil.arePrefixesPresent;

import seedu.expensetracker.logic.commands.NotificationCommand;
import seedu.expensetracker.logic.commands.NotificationCommand.NotificationCommandDescriptor;
import seedu.expensetracker.logic.parser.exceptions.ParseException;

//@author Snookerballs

/**
 * Parses input arguments and creates a new NotificationCommand object
 */
public class NotificationCommandParser implements Parser<NotificationCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the NotificationCommand
     * and returns an NotificationCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public NotificationCommand parse(String args) throws ParseException {
        NotificationCommandDescriptor descriptor = new NotificationCommandDescriptor();
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_TOGGLE, PREFIX_NOTIFICATION_TYPE);

        if (!arePrefixesPresent(argMultimap, PREFIX_TOGGLE)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    NotificationCommand.MESSAGE_USAGE));
        }
        boolean isSuccessfulSet = descriptor.setToggle(argMultimap.getValue(PREFIX_TOGGLE).get());

        if (!isSuccessfulSet) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    NotificationCommand.MESSAGE_USAGE));
        }

        if (arePrefixesPresent(argMultimap, PREFIX_NOTIFICATION_TYPE)) {
            isSuccessfulSet = descriptor.setNotificationType(argMultimap.getValue(PREFIX_NOTIFICATION_TYPE).get());
            if (!isSuccessfulSet) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        NotificationCommand.MESSAGE_USAGE));
            }
        }
        return new NotificationCommand(descriptor);
    }
}
