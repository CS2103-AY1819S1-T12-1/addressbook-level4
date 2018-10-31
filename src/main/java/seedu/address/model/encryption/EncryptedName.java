package seedu.address.model.encryption;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.expense.Name;

//@@author JasonChong96
/**
 * Represents a Expense's name in Expense Tracker in it's encrypted form.
 * Guarantees: immutable;
 */
public class EncryptedName extends EncryptedExpenseField<Name> {

    /**
     * A constructor for an EncryptedName that takes in an encrypted Name representation of the name.
     * @param encryptedString the encrypted String representation of the name
     */
    public EncryptedName(String encryptedString) {
        super(encryptedString);
    }

    /**
     * A constructor for an EncryptedName that takes in an unencrypted Name instance of the name.
     * @param src the unencrypted Name instance
     * @param key the encryption key to use, must be 128-bit
     * @throws IllegalValueException when the input encryption key is invalid
     */
    public EncryptedName(Name src, String key) throws IllegalValueException {
        super(src, key);
    }

    @Override
    public Name getDecrypted(String key) throws IllegalValueException {
        return new Name(EncryptionUtil.decryptString(getEncryptedString(), key));
    }
}
