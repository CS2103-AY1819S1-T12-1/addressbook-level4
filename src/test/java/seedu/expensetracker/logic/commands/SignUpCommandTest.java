package seedu.expensetracker.logic.commands;

import static org.junit.Assert.assertEquals;

import static seedu.expensetracker.model.user.UsernameTest.VALID_USERNAME_STRING;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.model.exceptions.UserAlreadyExistsException;
import seedu.expensetracker.model.user.Username;
import seedu.expensetracker.testutil.ModelStub;

//@@author JasonChong96
public class SignUpCommandTest {
    private static final Username EXISTING_USER = new Username("existing");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ModelStubSignUp model = new ModelStubSignUp();
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullUsername_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new SignUpCommand(null);
    }

    @Test
    public void execute_userAcceptedByModel_signUpSuccessful() throws Exception {
        CommandResult commandResult = new SignUpCommand(new Username(VALID_USERNAME_STRING))
                .execute(model, commandHistory);
        assertEquals(String.format(SignUpCommand.MESSAGE_SIGN_UP_SUCCESS, VALID_USERNAME_STRING),
                commandResult.feedbackToUser);
    }

    @Test
    public void execute_duplicateRejectedByModel_signUpFailed() throws Exception {
        thrown.expect(UserAlreadyExistsException.class);
        new SignUpCommand(EXISTING_USER).execute(model, commandHistory);
    }

    /**
     * Model stub for use with SignUpCommand. Supports the method addUser.
     */
    private class ModelStubSignUp extends ModelStub {
        @Override
        public void addUser(Username username) throws UserAlreadyExistsException {
            if (EXISTING_USER.equals(username)) {
                throw new UserAlreadyExistsException(username);
            }
        }
    }
}
