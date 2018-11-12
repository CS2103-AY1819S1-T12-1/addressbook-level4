package seedu.expensetracker.logic.parser;

import static seedu.expensetracker.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.expensetracker.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.expensetracker.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.Test;

import seedu.expensetracker.logic.commands.SignUpCommand;
import seedu.expensetracker.model.user.Username;
import seedu.expensetracker.model.user.UsernameTest;

//@@author JasonChong96
public class SignUpCommandParserTest {
    private SignUpCommandParser parser = new SignUpCommandParser();

    @Test
    public void parse_validUsername() {
        assertParseSuccess(parser, UsernameTest.VALID_USERNAME_STRING,
                new SignUpCommand(new Username(UsernameTest.VALID_USERNAME_STRING)));
    }

    @Test
    public void parse_invalidUsername() {
        assertParseFailure(parser, UsernameTest.INVALID_USERNAME_STRING,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, SignUpCommand.MESSAGE_USAGE));
    }
}
