package seedu.expensetracker.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.model.user.Password;
import seedu.expensetracker.model.user.PasswordTest;
import seedu.expensetracker.testutil.ModelStub;
import seedu.expensetracker.testutil.ModelStubNoUser;

//@@author JasonChong96
public class SetPasswordCommandTest {

    private static final Password INVALID_PASSWORD = new Password("password1", true);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ModelStubSetPassword model = new ModelStubSetPassword();
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullNewPassword_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new SetPasswordCommand(null, null, null);
    }

    @Test
    public void constructor_nullPlainNewPassword_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new SetPasswordCommand(null, PasswordTest.VALID_PASSWORD, null);
    }

    @Test
    public void execute_noSelectedUser_throwsNoUserSelectedException() throws NoUserSelectedException {
        thrown.expect(NoUserSelectedException.class);
        new SetPasswordCommand(null, PasswordTest.VALID_PASSWORD,
                PasswordTest.VALID_PASSWORD_STRING).execute(new ModelStubNoUser(), commandHistory);
    }

    @Test
    public void execute_noOldPassword_assertSetPasswordSuccessful() throws NoUserSelectedException {
        CommandResult commandResult = new SetPasswordCommand(null, PasswordTest.VALID_PASSWORD,
                PasswordTest.VALID_PASSWORD_STRING).execute(model, commandHistory);
        assertEquals(SetPasswordCommand.MESSAGE_SET_PASSWORD_SUCCESS, commandResult.feedbackToUser);
    }

    @Test
    public void execute_oldPasswordInvalid_assertSetPasswordFailure() throws NoUserSelectedException {
        CommandResult commandResult = new SetPasswordCommand(
                INVALID_PASSWORD,
                PasswordTest.VALID_PASSWORD,
                PasswordTest.VALID_PASSWORD_STRING).execute(model, commandHistory);
        assertEquals(SetPasswordCommand.MESSAGE_INCORRECT_PASSWORD, commandResult.feedbackToUser);
    }

    @Test
    public void testEquals() {
        assertEquals(new SetPasswordCommand(PasswordTest.VALID_PASSWORD,
                        PasswordTest.VALID_PASSWORD,
                        PasswordTest.VALID_PASSWORD_STRING),
                new SetPasswordCommand(PasswordTest.VALID_PASSWORD,
                        PasswordTest.VALID_PASSWORD,
                        PasswordTest.VALID_PASSWORD_STRING));
        assertNotEquals(new SetPasswordCommand(PasswordTest.VALID_PASSWORD,
                        PasswordTest.VALID_PASSWORD,
                        PasswordTest.VALID_PASSWORD_STRING),
                new SetPasswordCommand(PasswordTest.VALID_PASSWORD,
                        new Password(PasswordTest.VALID_PASSWORD_STRING.toUpperCase(), true),
                        PasswordTest.VALID_PASSWORD_STRING));
    }

    /**
     * Model stub for use with SetPasswordCommand. Supports the method setPassword and isMatchPassword.
     */
    private class ModelStubSetPassword extends ModelStub {
        @Override
        public void setPassword(Password newPassword, String plainPassword) {
        }

        @Override
        public boolean isMatchPassword(Password toCheck) {
            return !INVALID_PASSWORD.equals(toCheck);
        }
    }
}
