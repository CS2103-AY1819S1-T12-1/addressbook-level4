package seedu.expensetracker.model.encryption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static seedu.expensetracker.model.encryption.EncryptionUtil.DEFAULT_ENCRYPTION_KEY;
import static seedu.expensetracker.testutil.Assert.assertThrows;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.expensetracker.commons.exceptions.IllegalValueException;

//@@author JasonChong96
public class EncryptionUtilTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void createEncryptionKey_successful128bitKey() {
        assertEquals(EncryptionUtil.createEncryptionKey(createStringOfLength(0)).length(), 32);
        assertEquals(EncryptionUtil.createEncryptionKey(createStringOfLength(1)).length(), 32);
        assertEquals(EncryptionUtil.createEncryptionKey(createStringOfLength(33)).length(), 32);
    }

    @Test
    public void createEncryptionKey_nullPassword_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> EncryptionUtil.createEncryptionKey(null));
    }

    @Test
    public void encryptString_successfulEncryptedNotSameAsInput() throws IllegalValueException {
        String length1String = createStringOfLength(1);
        String length32String = createStringOfLength(32);
        assertNotEquals(EncryptionUtil.encryptString(length1String, DEFAULT_ENCRYPTION_KEY), length1String);
        assertNotEquals(EncryptionUtil.encryptString(length32String, DEFAULT_ENCRYPTION_KEY), length32String);
    }

    @Test
    public void encryptString_nullString_throwsNullPointerException() throws IllegalValueException {
        thrown.expect(NullPointerException.class);
        EncryptionUtil.encryptString(null, DEFAULT_ENCRYPTION_KEY);
    }

    @Test
    public void encryptString_nullKey_throwsNullPointerException() throws IllegalValueException {
        thrown.expect(NullPointerException.class);
        EncryptionUtil.encryptString(createStringOfLength(1), null);
    }

    @Test
    public void decryptString_successful() throws IllegalValueException {
        String originalString = createStringOfLength(1);
        String encryptedString = EncryptionUtil.encryptString(originalString, DEFAULT_ENCRYPTION_KEY);
        assertEquals(EncryptionUtil.decryptString(encryptedString, DEFAULT_ENCRYPTION_KEY), originalString);
    }

    @Test
    public void decryptString_nullString_throwsNullPointerException() throws IllegalValueException {
        thrown.expect(NullPointerException.class);
        EncryptionUtil.decryptString(null, DEFAULT_ENCRYPTION_KEY);
    }

    @Test
    public void decryptString_nullKey_throwsNullPointerException() throws IllegalValueException {
        thrown.expect(NullPointerException.class);
        EncryptionUtil.decryptString(createStringOfLength(1), null);
    }

    /**
     * Creates a string with length matching to input.
     * @param length the length of the output string
     * @return a string with length matching to input.
     */
    private String createStringOfLength(int length) {
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length) {
            sb.append('a');
        }
        return sb.toString();
    }
}
