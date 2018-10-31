package seedu.address.model.encryption;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.tag.Tag;

//@@author JasonChong96
/**
 * Represents a Expense's tag in Expense Tracker in it's encrypted form.
 * Guarantees: immutable;
 */
public class EncryptedTag {
    private String encryptedString;

    /**
     * A constructor for an EncryptedTag that takes in an encrypted Tag representation of the tah.
     * @param encryptedString the encrypted String representation of the tag
     */
    public EncryptedTag(String encryptedString) {
        requireNonNull(encryptedString);
        this.encryptedString = encryptedString;
    }

    /**
     * A constructor for an EncryptedTag that takes in an unencrypted Tag instance of the tag.
     * @param src the unencrypted Tag instance
     * @param key the encryption key to use, must be 128-bit
     * @throws IllegalValueException when the input encryption key is invalid
     */
    public EncryptedTag(Tag src, String key) throws IllegalValueException {
        requireAllNonNull(src, key);
        this.encryptedString = EncryptionUtil.encryptString(src.tagName, key);
    }

    /**
     * Decrypts this EncryptedTag with the given encryption key.
     * @param key the encryption key to use, must be 128-bit
     * @return an Tag instance of the decrypted Tag
     * @throws IllegalValueException when the encryption key is invalid
     */
    public Tag getDecrypted(String key) throws IllegalValueException {
        return new Tag(EncryptionUtil.decryptString(encryptedString, key));
    }

    /**
     * Gets the String representation of this EncryptedTag.
     * @return the String representation of this EncryptedTag
     */
    public String getEncryptedString() {
        return encryptedString;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || other instanceof EncryptedTag // instanceof handles nulls
                && encryptedString.equals(((EncryptedTag) other).encryptedString); // state check
    }

    @Override
    public int hashCode() {
        return encryptedString.hashCode();
    }
}
