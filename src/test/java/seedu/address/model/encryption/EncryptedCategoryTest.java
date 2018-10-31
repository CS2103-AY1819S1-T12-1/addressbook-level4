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
public class EncryptedCategoryTest {
    @Test
    public void constructor_fromNullString_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new EncryptedCategory(null));
    }

    @Test
    public void constructor_fromValidString_success() {
        assertEquals(new EncryptedCategory("a").getEncryptedString(), "a");
    }

    @Test
    public void constructor_nullCategory_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new EncryptedCategory(null, DEFAULT_ENCRYPTION_KEY));
    }

    @Test
    public void constructor_nullKey_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new EncryptedCategory(KFC.getCategory(),
                null));
    }

    @Test
    public void testEquals() throws IllegalValueException {
        assertEquals(new EncryptedCategory(""), new EncryptedCategory(""));
        assertNotEquals(new EncryptedCategory("a"), new EncryptedCategory(""));
        assertEquals(new EncryptedCategory(KFC.getCategory(), DEFAULT_ENCRYPTION_KEY),
                new EncryptedCategory(KFC.getCategory(), DEFAULT_ENCRYPTION_KEY));
        assertNotEquals(new EncryptedCategory(KFC.getCategory(), DEFAULT_ENCRYPTION_KEY),
                new EncryptedCategory(IPHONE.getCategory(), DEFAULT_ENCRYPTION_KEY));
        // Same encrypted string but from different constructors
        assertEquals(new EncryptedCategory(KFC.getCategory(), DEFAULT_ENCRYPTION_KEY),
                new EncryptedCategory(EncryptionUtil.encryptString(KFC.getCategory().categoryName,
                        DEFAULT_ENCRYPTION_KEY)));
    }

    @Test
    public void testEncryptionDecryption() throws IllegalValueException {
        // Check that encryption and decryption are symmetrical with the same key
        assertEquals(KFC.getCategory(),
                new EncryptedCategory(KFC.getCategory(), DEFAULT_ENCRYPTION_KEY).getDecrypted(DEFAULT_ENCRYPTION_KEY));
    }

    @Test
    public void testHashCode() {
        assertEquals(new EncryptedCategory("").hashCode(),
                new EncryptedCategory("").hashCode());
        assertEquals(new EncryptedCategory("a").hashCode(),
                new EncryptedCategory("a").hashCode());
    }
}
