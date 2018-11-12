package seedu.expensetracker.logic.commands;

import static org.junit.Assert.assertEquals;
import static seedu.expensetracker.testutil.ModelUtil.getTypicalModel;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;

//@@author JasonChong96
public class EncryptCommandIntegrationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testExecute() throws NoUserSelectedException {
        EncryptCommand commandEmpty = new EncryptCommand("");
        EncryptCommand command = new EncryptCommand("a");
        Model model = getTypicalModel();
        CommandResult emptyResult = commandEmpty.execute(model, null);
        assertEquals(String.format(EncryptCommand.MESSAGE_SUCCESS, model.encryptString("")),
                emptyResult.feedbackToUser);
        CommandResult result = command.execute(model, null);
        assertEquals(String.format(EncryptCommand.MESSAGE_SUCCESS, model.encryptString("a")),
                result.feedbackToUser);
    }

    @Test
    public void testExecute_noUser_throwsNoUserSelectedException() throws NoUserSelectedException {
        Model model = getTypicalModel();
        model.unloadUserData();
        EncryptCommand command = new EncryptCommand("");
        thrown.expect(NoUserSelectedException.class);
        command.execute(model, null);
    }
}
