package seedu.address.model.encryption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static seedu.address.model.encryption.EncryptionUtil.DEFAULT_ENCRYPTION_KEY;
import static seedu.address.testutil.TypicalExpenses.IPHONE;
import static seedu.address.testutil.TypicalExpenses.KFC;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.testutil.Assert;

//@@author JasonChong96
public class EncryptedNameTest {
    @Test
    public void constructor_fromNullString_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new EncryptedName(null));
    }

    @Test
    public void constructor_fromValidString_success() {
        assertEquals(new EncryptedName("a").getEncryptedString(), "a");
    }

    @Test
    public void constructor_nullName_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new EncryptedName(null, DEFAULT_ENCRYPTION_KEY));
    }

    @Test
    public void constructor_nullKey_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new EncryptedName(KFC.getName(), null));
    }

    @Test
    public void testEquals() throws IllegalValueException {
        assertEquals(new EncryptedName(""), new EncryptedName(""));
        assertNotEquals(new EncryptedName("a"), new EncryptedName(""));
        assertEquals(new EncryptedName(KFC.getName(), DEFAULT_ENCRYPTION_KEY),
                new EncryptedName(KFC.getName(), DEFAULT_ENCRYPTION_KEY));
        assertNotEquals(new EncryptedName(KFC.getName(), DEFAULT_ENCRYPTION_KEY),
                new EncryptedName(IPHONE.getName(), DEFAULT_ENCRYPTION_KEY));
        // Same encrypted string but from different constructors
        assertEquals(new EncryptedName(KFC.getName(), DEFAULT_ENCRYPTION_KEY),
                new EncryptedName(EncryptionUtil.encryptString(KFC.getName().expenseName, DEFAULT_ENCRYPTION_KEY)));
    }

    @Test
    public void testEncryptionDecryption() throws IllegalValueException {
        // Check that encryption and decryption are symmetrical with the same key
        assertEquals(KFC.getName(),
                new EncryptedName(KFC.getName(), DEFAULT_ENCRYPTION_KEY).getDecrypted(DEFAULT_ENCRYPTION_KEY));
    }

    @Test
    public void testHashCode() {
        assertEquals(new EncryptedName("").hashCode(), new EncryptedName("").hashCode());
        assertEquals(new EncryptedName("a").hashCode(), new EncryptedName("a").hashCode());
    }
}
