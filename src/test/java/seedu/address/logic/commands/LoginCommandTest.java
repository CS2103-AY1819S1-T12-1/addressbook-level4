package seedu.address.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.testutil.TypicalExpenses.getTypicalAddressBook;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.CommandHistory;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.exceptions.NonExistentUserException;
import seedu.address.model.user.Username;
import seedu.address.model.user.UsernameTest;
import seedu.address.testutil.TypicalExpenses;

//@@author JasonChong96
public class LoginCommandTest {
    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullUsername_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new LoginCommand(null);
    }

    @Test
    public void execute_userAcceptedByModel_loginSuccessful() throws Exception {
        CommandResult commandResult = new LoginCommand(TypicalExpenses.SAMPLE_USERNAME)
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
        new LoginCommand(new Username(UsernameTest.VALID_USERNAME_STRING)).execute(model, commandHistory);
    }
}
