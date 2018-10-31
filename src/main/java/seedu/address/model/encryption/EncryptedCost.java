package seedu.address.model.encryption;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.expense.Cost;

//@@author JasonChong96
/**
 * Represents a Expense's cost in Expense Tracker in it's encrypted form.
 * Guarantees: immutable;
 */
public class EncryptedCost extends EncryptedExpenseField<Cost> {

    /**
     * A constructor for an EncryptedCost that takes in an encrypted String representation of the cost.
     * @param encryptedString the encrypted String representation of the cost
     */
    public EncryptedCost(String encryptedString) {
        super(encryptedString);
    }

    /**
     * A constructor for an EncryptedCost that takes in an unencrypted Cost instance of the cost.
     * @param src the unencrypted Cost instance
     * @param key the encryption key to use, must be 128-bit
     * @throws IllegalValueException when the input encryption key is invalid
     */
    public EncryptedCost(Cost src, String key) throws IllegalValueException {
        super(src, key);
    }

    @Override
    public Cost getDecrypted(String key) throws IllegalValueException {
        return new Cost(EncryptionUtil.decryptString(getEncryptedString(), key));
    }
}
