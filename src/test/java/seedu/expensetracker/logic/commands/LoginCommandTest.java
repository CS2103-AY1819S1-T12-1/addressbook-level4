package seedu.expensetracker.logic.commands;

import static org.junit.Assert.assertEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.logic.LoginCredentials;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.exceptions.NonExistentUserException;
import seedu.expensetracker.model.user.Password;
import seedu.expensetracker.model.user.Username;
import seedu.expensetracker.testutil.ModelStub;
import seedu.expensetracker.testutil.TypicalExpenses;

//@@author JasonChong96
public class LoginCommandTest {
    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();
    private static final Username NON_EXISTANT_USERNAME = new Username("noexist");
    private static final String INVALID_PASSWORD_STRING = "password1";
    private static final Password INVALID_PASSWORD = new Password(INVALID_PASSWORD_STRING, true);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelStubLogin();

    @Test
    public void constructor_nullLoginInformation_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new LoginCommand(null);
    }

    @Test
    public void execute_userAcceptedByModel_loginSuccessful() throws Exception {
        CommandResult commandResult = new LoginCommand(new LoginCredentials(TypicalExpenses.SAMPLE_USERNAME, null))
                .execute(model, null);
        assertEquals(String.format(LoginCommand.MESSAGE_LOGIN_SUCCESS, TypicalExpenses.SAMPLE_USERNAME.toString()),
                commandResult.feedbackToUser);
    }

    @Test
    public void execute_nonExistantUser_loginFailed() throws Exception {
        thrown.expect(NonExistentUserException.class);
        new LoginCommand(new LoginCredentials(NON_EXISTANT_USERNAME, null))
                .execute(model, null);
    }

    @Test
    public void execute_incorrectPassword_loginFailed() throws Exception {
        CommandResult commandResult =
                new LoginCommand(new LoginCredentials(TypicalExpenses.SAMPLE_USERNAME, INVALID_PASSWORD_STRING))
                        .execute(model, null);
        assertEquals(LoginCommand.MESSAGE_INCORRECT_PASSWORD, commandResult.feedbackToUser);
    }

    /**
     * Model stub for use with LoginCommand. Supports loadUserData(LoginCredentials) method.
     */
    private class ModelStubLogin extends ModelStub {
        @Override
        public boolean loadUserData(LoginCredentials loginCredentials) throws NonExistentUserException {
            if (loginCredentials.getUsername().equals(NON_EXISTANT_USERNAME)) {
                throw new NonExistentUserException(NON_EXISTANT_USERNAME, 0);
            } else {
                return !INVALID_PASSWORD.equals(loginCredentials.getPassword().orElse(null));
            }
        }
    }
}
