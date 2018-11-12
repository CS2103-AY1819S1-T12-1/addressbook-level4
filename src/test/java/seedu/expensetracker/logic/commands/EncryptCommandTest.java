package seedu.expensetracker.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.testutil.ModelStub;

//@@author JasonChong96
public class EncryptCommandTest {

    private static final String ENCRYPTED_STRING = "eeeeee";

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullString_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new EncryptCommand(null);
    }

    @Test
    public void testExecute() throws NoUserSelectedException {
        EncryptCommand commandEmpty = new EncryptCommand("");
        EncryptCommand command = new EncryptCommand("a");
        ModelStubEncrypt model = new ModelStubEncrypt();
        CommandResult emptyResult = commandEmpty.execute(model, null);
        assertEquals(String.format(EncryptCommand.MESSAGE_SUCCESS, ENCRYPTED_STRING), emptyResult.feedbackToUser);
        CommandResult result = command.execute(model, null);
        assertEquals(String.format(EncryptCommand.MESSAGE_SUCCESS, ENCRYPTED_STRING), result.feedbackToUser);
    }

    @Test
    public void testEquals() {
        EncryptCommand commandEmpty = new EncryptCommand("");
        EncryptCommand command = new EncryptCommand("a");
        assertEquals(command, new EncryptCommand("a"));
        assertEquals(commandEmpty, new EncryptCommand(""));
        assertNotEquals(command, commandEmpty);
        assertNotEquals(command, new EncryptCommand("b"));
    }

    @Test
    public void testHashCode() {
        EncryptCommand commandEmpty = new EncryptCommand("");
        EncryptCommand command = new EncryptCommand("a");
        assertEquals(command.hashCode(), new EncryptCommand("a").hashCode());
        assertEquals(commandEmpty.hashCode(), new EncryptCommand("").hashCode());
    }

    /**
     * A model stub that have all of the methods failing except for encryptString which is used by sEncryptCommand
     */
    private class ModelStubEncrypt extends ModelStub {
        @Override
        public String encryptString(String toEncrypt) {
            return ENCRYPTED_STRING;
        }
    }
}
