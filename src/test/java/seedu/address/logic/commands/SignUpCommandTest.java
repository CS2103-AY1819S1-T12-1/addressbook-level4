package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import static seedu.address.model.user.UsernameTest.VALID_USERNAME_STRING;
import static seedu.address.testutil.TypicalExpenses.getTypicalAddressBook;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.exceptions.UserAlreadyExistsException;
import seedu.address.model.user.Username;
import seedu.address.testutil.TypicalExpenses;

//@@author JasonChong96
public class SignUpCommandTest {
    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
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
        assertTrue(model.isUserExists(new Username(VALID_USERNAME_STRING)));
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_duplicateRejectedByModel_signUpFailed() throws Exception {
        assertTrue(model.isUserExists(TypicalExpenses.SAMPLE_USERNAME));
        thrown.expect(UserAlreadyExistsException.class);
        new SignUpCommand(TypicalExpenses.SAMPLE_USERNAME).execute(model, commandHistory);
    }
}
