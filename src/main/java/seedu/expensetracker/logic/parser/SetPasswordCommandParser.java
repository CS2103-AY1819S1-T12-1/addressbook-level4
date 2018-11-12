package seedu.expensetracker.logic.parser;

import static seedu.expensetracker.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_NEW_PASSWORD;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_OLD_PASSWORD;
import static seedu.expensetracker.logic.parser.ParserUtil.arePrefixesPresent;

import java.util.Optional;

import seedu.expensetracker.logic.commands.SetPasswordCommand;
import seedu.expensetracker.logic.parser.exceptions.ParseException;
import seedu.expensetracker.model.user.Password;

//@@author JasonChong96
/**
 * Parses input arguments and creates a new SetPasswordCommand object
 */
public class SetPasswordCommandParser implements Parser<SetPasswordCommand> {
    @Override
    public SetPasswordCommand parse(String userInput) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(userInput, PREFIX_OLD_PASSWORD, PREFIX_NEW_PASSWORD);

        if (!arePrefixesPresent(argMultimap, PREFIX_NEW_PASSWORD)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetPasswordCommand.MESSAGE_USAGE));
        }
        String newPasswordString = argMultimap.getValue(PREFIX_NEW_PASSWORD).get();
        Password newPassword = ParserUtil.parsePassword(newPasswordString);
        Optional<String> oldPasswordString = argMultimap.getValue(PREFIX_OLD_PASSWORD);
        // Optional#map is not used as ParserUtil#parsePassword(String) throws a checked exception
        Optional<Password> oldPassword = oldPasswordString.isPresent()
                ? Optional.of(ParserUtil.parsePassword(oldPasswordString.get())) : Optional.empty();
        return new SetPasswordCommand(oldPassword.orElse(null), newPassword, newPasswordString);
    }
}
