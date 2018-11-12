package seedu.expensetracker.logic.parser;

import static seedu.expensetracker.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_PASSWORD;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_USERNAME;
import static seedu.expensetracker.logic.parser.ParserUtil.arePrefixesPresent;

import java.util.Optional;

import seedu.expensetracker.logic.LoginCredentials;
import seedu.expensetracker.logic.commands.LoginCommand;
import seedu.expensetracker.logic.parser.exceptions.ParseException;
import seedu.expensetracker.model.user.Username;

//@@author JasonChong96
/**
 * Parses input arguments and creates a new LoginCommand object
 */
public class LoginCommandParser implements Parser<LoginCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the LoginCommand
     * and returns an LoginCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public LoginCommand parse(String userInput) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(userInput, PREFIX_USERNAME, PREFIX_PASSWORD);
        if (!arePrefixesPresent(argMultimap, PREFIX_USERNAME)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, LoginCommand.MESSAGE_USAGE));
        }
        Username username = ParserUtil.parseUsername(argMultimap.getValue(PREFIX_USERNAME).get());
        Optional<String> plainPassword = argMultimap.getValue(PREFIX_PASSWORD);
        LoginCredentials loginCredentials = new LoginCredentials(username, plainPassword.orElse(null));
        return new LoginCommand(loginCredentials);
    }
}
