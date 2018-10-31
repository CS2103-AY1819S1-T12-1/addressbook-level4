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
public class EncryptedCostTest {
    @Test
    public void constructor_fromNullString_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new EncryptedCost(null));
    }

    @Test
    public void constructor_fromValidString_success() {
        assertEquals(new EncryptedCost("a").getEncryptedString(), "a");
    }

    @Test
    public void constructor_nullCost_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new EncryptedCost(null, DEFAULT_ENCRYPTION_KEY));
    }

    @Test
    public void constructor_nullKey_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new EncryptedCost(KFC.getCost(),
                null));
    }

    @Test
    public void testEquals() throws IllegalValueException {
        assertEquals(new EncryptedCost(""), new EncryptedCost(""));
        assertNotEquals(new EncryptedCost("a"), new EncryptedCost(""));
        assertEquals(new EncryptedCost(KFC.getCost(), DEFAULT_ENCRYPTION_KEY),
                new EncryptedCost(KFC.getCost(), DEFAULT_ENCRYPTION_KEY));
        assertNotEquals(new EncryptedCost(KFC.getCost(), DEFAULT_ENCRYPTION_KEY),
                new EncryptedCost(IPHONE.getCost(), DEFAULT_ENCRYPTION_KEY));
        // Same encrypted string but from different constructors
        assertEquals(new EncryptedCost(KFC.getCost(), DEFAULT_ENCRYPTION_KEY),
                new EncryptedCost(EncryptionUtil.encryptString(KFC.getCost().value,
                        DEFAULT_ENCRYPTION_KEY)));
    }

    @Test
    public void testEncryptionDecryption() throws IllegalValueException {
        // Check that encryption and decryption are symmetrical with the same key
        assertEquals(KFC.getCost(),
                new EncryptedCost(KFC.getCost(), DEFAULT_ENCRYPTION_KEY).getDecrypted(DEFAULT_ENCRYPTION_KEY));
    }

    @Test
    public void testHashCode() {
        assertEquals(new EncryptedCost("").hashCode(),
                new EncryptedCost("").hashCode());
        assertEquals(new EncryptedCost("a").hashCode(),
                new EncryptedCost("a").hashCode());
    }
}
