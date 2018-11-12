package seedu.expensetracker.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.expensetracker.testutil.ModelUtil.getTypicalModel;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;

//@@author JasonChong96
public class DecryptCommandIntegrationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testExecute() throws NoUserSelectedException {
        Model model = getTypicalModel();
        String encryptedString = model.encryptString("");
        DecryptCommand command = new DecryptCommand(encryptedString);
        CommandResult result = command.execute(model, null);
        assertEquals(String.format(DecryptCommand.MESSAGE_SUCCESS, ""), result.feedbackToUser);
        DecryptCommand commandInvalid = new DecryptCommand("a");
        CommandResult invalidResult = commandInvalid.execute(model, null);
        assertEquals(DecryptCommand.MESSAGE_FAILURE, invalidResult.feedbackToUser);
    }

    @Test
    public void testExecute_noUser_throwsNoUserSelectedException() throws NoUserSelectedException {
        Model model = getTypicalModel();
        String encryptedString = model.encryptString("");
        model.unloadUserData();
        DecryptCommand command = new DecryptCommand(encryptedString);
        thrown.expect(NoUserSelectedException.class);
        command.execute(model, null);
    }
}
