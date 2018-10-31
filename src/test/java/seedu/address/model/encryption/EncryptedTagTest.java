package seedu.address.model.encryption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FOOD;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.model.encryption.EncryptionUtil.DEFAULT_ENCRYPTION_KEY;

import org.junit.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;
import seedu.address.testutil.Assert;

//@@author JasonChong96
public class EncryptedTagTest {
    private static final Tag FOOD_TAG = new Tag(VALID_TAG_FOOD);
    private static final Tag FRIEND_TAG = new Tag(VALID_TAG_FRIEND);

    @Test
    public void constructor_fromNullString_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new EncryptedTag(null));
    }

    @Test
    public void constructor_fromValidString_success() {
        assertEquals(new EncryptedTag("a").getEncryptedString(), "a");
    }

    @Test
    public void constructor_nullTag_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new EncryptedTag(null, DEFAULT_ENCRYPTION_KEY));
    }

    @Test
    public void constructor_nullKey_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new EncryptedTag(FOOD_TAG,
                null));
    }

    @Test
    public void testEquals() throws IllegalValueException {
        assertEquals(new EncryptedTag(""), new EncryptedTag(""));
        assertNotEquals(new EncryptedTag("a"), new EncryptedTag(""));
        assertEquals(new EncryptedTag(FOOD_TAG, DEFAULT_ENCRYPTION_KEY),
                new EncryptedTag(FOOD_TAG, DEFAULT_ENCRYPTION_KEY));
        assertNotEquals(new EncryptedTag(FOOD_TAG, DEFAULT_ENCRYPTION_KEY),
                new EncryptedTag(FRIEND_TAG, DEFAULT_ENCRYPTION_KEY));
        // Same encrypted string but from different constructors
        assertEquals(new EncryptedTag(FOOD_TAG, DEFAULT_ENCRYPTION_KEY),
                new EncryptedTag(EncryptionUtil.encryptString(FOOD_TAG.tagName,
                        DEFAULT_ENCRYPTION_KEY)));
    }

    @Test
    public void testEncryptionDecryption() throws IllegalValueException {
        // Check that encryption and decryption are symmetrical with the same key
        assertEquals(FOOD_TAG,
                new EncryptedTag(FOOD_TAG, DEFAULT_ENCRYPTION_KEY).getDecrypted(DEFAULT_ENCRYPTION_KEY));
    }

    @Test
    public void testHashCode() {
        assertEquals(new EncryptedTag("").hashCode(),
                new EncryptedTag("").hashCode());
        assertEquals(new EncryptedTag("a").hashCode(),
                new EncryptedTag("a").hashCode());
    }
}
