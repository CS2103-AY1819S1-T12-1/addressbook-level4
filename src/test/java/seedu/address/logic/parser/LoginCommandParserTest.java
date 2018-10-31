package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.LoginCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.user.LoginInformation;
import seedu.address.model.user.Password;
import seedu.address.model.user.PasswordTest;
import seedu.address.model.user.Username;
import seedu.address.model.user.UsernameTest;

//@@author JasonChong96
public class LoginCommandParserTest {
    private LoginCommandParser parser = new LoginCommandParser();

    @Test
    public void parse_validUsername() throws ParseException {
        assertParseSuccess(parser, " " + CliSyntax.PREFIX_USERNAME + UsernameTest.VALID_USERNAME_STRING,
                new LoginCommand(new LoginInformation(new Username(UsernameTest.VALID_USERNAME_STRING), null)));
    }

    @Test
    public void parse_validUsernamePassword() throws ParseException {
        assertParseSuccess(parser, " " + CliSyntax.PREFIX_USERNAME + UsernameTest.VALID_USERNAME_STRING
                + " " + CliSyntax.PREFIX_PASSWORD + PasswordTest.VALID_PASSWORD_STRING,
                new LoginCommand(new LoginInformation(new Username(UsernameTest.VALID_USERNAME_STRING),
                        PasswordTest.VALID_PASSWORD_STRING)));
    }

    @Test
    public void parse_invalidSyntax() {
        assertParseFailure(parser, UsernameTest.INVALID_USERNAME_STRING,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, LoginCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidUsername() {
        assertParseFailure(parser, " " + CliSyntax.PREFIX_USERNAME + UsernameTest.INVALID_USERNAME_STRING,
                Username.MESSAGE_NAME_CONSTRAINTS);
    }

    @Test
    public void parse_invalidPassword() {
        assertParseFailure(parser,
                " " + CliSyntax.PREFIX_USERNAME + UsernameTest.VALID_USERNAME_STRING + " "
                        + CliSyntax.PREFIX_PASSWORD + PasswordTest.INVALID_PASSWORD_STRING_SHORT,
                Password.MESSAGE_PASSWORD_CONSTRAINTS);
    }
}
