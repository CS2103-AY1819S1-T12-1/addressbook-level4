package seedu.expensetracker.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.expensetracker.commons.exceptions.IllegalValueException;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.testutil.ModelStub;
import seedu.expensetracker.testutil.ModelStubNoUser;

//@@author JasonChong96
public class DecryptCommandTest {

    private static final String DECRYPTED_STRING = "eeeeee";
    private static final String INVALID_STRING = "aeeeeee";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullString_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new DecryptCommand(null);
    }

    @Test
    public void testExecute() throws NoUserSelectedException {
        DecryptCommand commandEmpty = new DecryptCommand("");
        DecryptCommand command = new DecryptCommand("a");
        ModelStubDecrypt model = new ModelStubDecrypt();
        CommandResult emptyResult = commandEmpty.execute(model, null);
        assertEquals(String.format(DecryptCommand.MESSAGE_SUCCESS, DECRYPTED_STRING), emptyResult.feedbackToUser);
        CommandResult result = command.execute(model, null);
        assertEquals(String.format(DecryptCommand.MESSAGE_SUCCESS, DECRYPTED_STRING), result.feedbackToUser);
        DecryptCommand commandInvalid = new DecryptCommand(INVALID_STRING);
        CommandResult invalidResult = commandInvalid.execute(model, null);
        assertEquals(DecryptCommand.MESSAGE_FAILURE, invalidResult.feedbackToUser);
    }

    @Test
    public void testExecute_noUser_throwsNoUserSelectedException() throws NoUserSelectedException {
        thrown.expect(NoUserSelectedException.class);
        DecryptCommand command = new DecryptCommand("a");
        ModelStubNoUser model = new ModelStubNoUser();
        command.execute(model, null);
    }

    @Test
    public void testEquals() {
        DecryptCommand commandEmpty = new DecryptCommand("");
        DecryptCommand command = new DecryptCommand("a");
        assertEquals(command, new DecryptCommand("a"));
        assertEquals(commandEmpty, new DecryptCommand(""));
        assertNotEquals(command, commandEmpty);
        assertNotEquals(command, new DecryptCommand("b"));
    }

    @Test
    public void testHashCode() {
        DecryptCommand commandEmpty = new DecryptCommand("");
        DecryptCommand command = new DecryptCommand("a");
        assertEquals(command.hashCode(), new DecryptCommand("a").hashCode());
        assertEquals(commandEmpty.hashCode(), new DecryptCommand("").hashCode());
    }

    /**
     * A model stub that have all of the methods failing except for decryptString which is used by sDecryptCommand
     */
    private class ModelStubDecrypt extends ModelStub {
        @Override
        public String decryptString(String toDecrypt) throws IllegalValueException {
            if (toDecrypt.equals(INVALID_STRING)) {
                throw new IllegalValueException("Invalid String");
            }
            return DECRYPTED_STRING;
        }
    }
}
