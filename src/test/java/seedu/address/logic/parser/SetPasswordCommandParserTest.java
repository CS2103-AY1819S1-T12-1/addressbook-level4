package seedu.address.logic.parser;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.address.logic.commands.SetPasswordCommand;
import seedu.address.model.user.Password;
import seedu.address.model.user.PasswordTest;

public class SetPasswordCommandParserTest {
    private SetPasswordCommandParser parser = new SetPasswordCommandParser();

    @Test
    public void parse_validPassword() {
        assertParseSuccess(parser, " " + CliSyntax.PREFIX_OLD_PASSWORD + PasswordTest.VALID_PASSWORD_STRING
                + " " + CliSyntax.PREFIX_NEW_PASSWORD + PasswordTest.VALID_PASSWORD_STRING,
                new SetPasswordCommand(PasswordTest.VALID_PASSWORD, PasswordTest.VALID_PASSWORD,
                        PasswordTest.VALID_PASSWORD_STRING));
    }

    @Test
    public void parse_invalidSyntax() {
        assertParseFailure(parser, PasswordTest.VALID_PASSWORD_STRING,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SetPasswordCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidOldPassword() {
        assertParseFailure(parser, " " + CliSyntax.PREFIX_OLD_PASSWORD
                        + PasswordTest.INVALID_PASSWORD_STRING_SHORT + " " + CliSyntax.PREFIX_NEW_PASSWORD
                        + PasswordTest.VALID_PASSWORD_STRING,
                Password.MESSAGE_PASSWORD_CONSTRAINTS);
    }

    @Test
    public void parse_invalidNewPassword() {
        assertParseFailure(parser, " " + CliSyntax.PREFIX_OLD_PASSWORD
                        + PasswordTest.VALID_PASSWORD_STRING + " " + CliSyntax.PREFIX_NEW_PASSWORD
                        + PasswordTest.INVALID_PASSWORD_STRING_SHORT,
                Password.MESSAGE_PASSWORD_CONSTRAINTS);
    }
}
