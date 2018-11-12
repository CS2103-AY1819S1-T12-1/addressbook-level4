package seedu.expensetracker.model.encryption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static seedu.expensetracker.model.encryption.EncryptionUtil.DEFAULT_ENCRYPTION_KEY;
import static seedu.expensetracker.testutil.TypicalExpenses.IPHONE;
import static seedu.expensetracker.testutil.TypicalExpenses.KFC;

import org.junit.Test;

import seedu.expensetracker.commons.exceptions.IllegalValueException;
import seedu.expensetracker.testutil.Assert;

//@@author JasonChong96
public class EncryptedDateTest {
    @Test
    public void constructor_fromNullString_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new EncryptedDate(null));
    }

    @Test
    public void constructor_fromValidString_success() {
        assertEquals(new EncryptedDate("a").getEncryptedString(), "a");
    }

    @Test
    public void constructor_nullDate_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new EncryptedDate(null, DEFAULT_ENCRYPTION_KEY));
    }

    @Test
    public void constructor_nullKey_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new EncryptedDate(KFC.getDate(),
                null));
    }

    @Test
    public void testEquals() throws IllegalValueException {
        assertEquals(new EncryptedDate(""), new EncryptedDate(""));
        assertNotEquals(new EncryptedDate("a"), new EncryptedDate(""));
        assertEquals(new EncryptedDate(KFC.getDate(), DEFAULT_ENCRYPTION_KEY),
                new EncryptedDate(KFC.getDate(), DEFAULT_ENCRYPTION_KEY));
        assertNotEquals(new EncryptedDate(KFC.getDate(), DEFAULT_ENCRYPTION_KEY),
                new EncryptedDate(IPHONE.getDate(), DEFAULT_ENCRYPTION_KEY));
        // Same encrypted string but from different constructors
        assertEquals(new EncryptedDate(KFC.getDate(), DEFAULT_ENCRYPTION_KEY),
                new EncryptedDate(EncryptionUtil.encryptString(KFC.getDate().toString(),
                        DEFAULT_ENCRYPTION_KEY)));
    }

    @Test
    public void testEncryptionDecryption() throws IllegalValueException {
        // Check that encryption and decryption are symmetrical with the same key
        assertEquals(KFC.getDate(),
                new EncryptedDate(KFC.getDate(), DEFAULT_ENCRYPTION_KEY).getDecrypted(DEFAULT_ENCRYPTION_KEY));
    }

    @Test
    public void testHashCode() {
        assertEquals(new EncryptedDate("").hashCode(),
                new EncryptedDate("").hashCode());
        assertEquals(new EncryptedDate("a").hashCode(),
                new EncryptedDate("a").hashCode());
    }
}
