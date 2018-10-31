package seedu.address.model.encryption;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.expense.ExpenseField;

//@@author JasonChong96
/**
 * Represents a Expense's field in Expense Tracker in it's encrypted form.
 * Guarantees: immutable;
 */
public abstract class EncryptedExpenseField<T extends ExpenseField> {
    private final String encryptedString;

    /**
     * A constructor for an EncryptedExpenseField that takes in an encrypted String representation of the expense field.
     * @param encryptedString the encrypted String representation of the expense field
     */
    public EncryptedExpenseField(String encryptedString) {
        requireNonNull(encryptedString);
        this.encryptedString = encryptedString;
    }

    /**
     * A constructor for an EncryptedExpenseField that takes in an unencrypted instance of the expense field.
     * @param src the unencrypted instance of the expense field
     * @param key the encryption key to use, must be 128-bit
     * @throws IllegalValueException when the input encryption key is invalid
     */
    public EncryptedExpenseField(T src, String key) throws IllegalValueException {
        requireNonNull(src, key);
        this.encryptedString = EncryptionUtil.encryptString(src.toString(), key);
    }

    /**
     * Decrypts this EncryptedExpenseField with the given encryption key.
     * @param key the encryption key to use, must be 128-bit
     * @return an instance of the decrypted ExpenseField
     * @throws IllegalValueException when the encryption key is invalid
     */
    public abstract T getDecrypted(String key) throws IllegalValueException;

    /**
     * Gets the String representation of this EncryptedExpenseField.
     * @return the String representation of this EncryptedExpenseField
     */
    public String getEncryptedString() {
        return encryptedString;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other.getClass().equals(this.getClass()) // instanceof handles nulls
                && encryptedString.equals(((EncryptedExpenseField) other).encryptedString)); // state check
    }

    @Override
    public int hashCode() {
        return encryptedString.hashCode();
    }
}
