package seedu.expensetracker.model.encryption;

import seedu.expensetracker.commons.exceptions.IllegalValueException;
import seedu.expensetracker.model.expense.Date;

//@@author JasonChong96
/**
 * Represents a Expense's date in Expense Tracker in it's encrypted form.
 * Guarantees: immutable;
 */
public class EncryptedDate extends EncryptedExpenseField<Date> {

    /**
     * A constructor for an EncryptedDate that takes in an encrypted Date representation of the date.
     * @param encryptedString the encrypted String representation of the date
     */
    public EncryptedDate(String encryptedString) {
        super(encryptedString);
    }

    /**
     * A constructor for an EncryptedDate that takes in an unencrypted Date instance of the date.
     * @param src the unencrypted Date instance
     * @param key the encryption key to use, must be 128-bit
     * @throws IllegalValueException when the input encryption key is invalid
     */
    public EncryptedDate(Date src, String key) throws IllegalValueException {
        super(src, key);
    }

    @Override
    public Date getDecrypted(String key) throws IllegalValueException {
        return new Date(EncryptionUtil.decryptString(getEncryptedString(), key));
    }
}
