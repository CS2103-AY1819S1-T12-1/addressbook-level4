package seedu.expensetracker.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.expensetracker.model.user.UsernameTest.VALID_USERNAME_STRING;
import static seedu.expensetracker.testutil.ModelUtil.getTypicalModel;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.exceptions.UserAlreadyExistsException;
import seedu.expensetracker.model.user.Username;
import seedu.expensetracker.testutil.TypicalExpenses;

public class SignUpCommandIntegrationTest {
    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Model model = getTypicalModel();
    private CommandHistory commandHistory = new CommandHistory();

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
