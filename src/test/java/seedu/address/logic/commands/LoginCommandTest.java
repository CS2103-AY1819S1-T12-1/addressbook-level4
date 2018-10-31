package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.ModelUtil.getTypicalModel;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.exceptions.NonExistentUserException;
import seedu.address.model.user.LoginInformation;
import seedu.address.model.user.PasswordTest;
import seedu.address.model.user.Username;
import seedu.address.model.user.UsernameTest;
import seedu.address.testutil.TypicalExpenses;

//@@author JasonChong96
public class LoginCommandTest {
    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = getTypicalModel();
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullLoginInformation_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new LoginCommand(null);
    }

    @Test
    public void execute_userAcceptedByModel_loginSuccessful() throws Exception {
        CommandResult commandResult = new LoginCommand(new LoginInformation(TypicalExpenses.SAMPLE_USERNAME, null))
                .execute(model, commandHistory);
        assertEquals(String.format(LoginCommand.MESSAGE_LOGIN_SUCCESS, TypicalExpenses.SAMPLE_USERNAME.toString()),
                commandResult.feedbackToUser);
        assertTrue(model.hasSelectedUser());
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_nonExistantUser_loginFailed() throws Exception {
        assertFalse(model.isUserExists(new Username(UsernameTest.VALID_USERNAME_STRING)));
        thrown.expect(NonExistentUserException.class);
        new LoginCommand(new LoginInformation(new Username(UsernameTest.VALID_USERNAME_STRING), null))
                .execute(model, commandHistory);
    }

    @Test
    public void execute_incorrectPassword_loginFailed() throws Exception {
        model.setPassword(PasswordTest.VALID_PASSWORD, PasswordTest.VALID_PASSWORD_STRING);
        CommandResult commandResult =
                new LoginCommand(new LoginInformation(TypicalExpenses.SAMPLE_USERNAME, null)).execute(model,
                commandHistory);
        assertEquals(LoginCommand.MESSAGE_INCORRECT_PASSWORD, commandResult.feedbackToUser);
    }
}
