package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ExpenseTracker;
import seedu.address.model.ReadOnlyExpenseTracker;
import seedu.address.model.encryption.EncryptedExpenseTracker;

/**
 * Represents a storage for {@link ExpenseTracker}.
 */
public interface ExpensesStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getExpensesDirPath();

    /**
     * Returns ExpenseTracker data as a {@link ReadOnlyExpenseTracker}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<EncryptedExpenseTracker> readExpenses() throws DataConversionException, IOException;

    /**
     * @see #getExpensesDirPath()
     */
    Optional<EncryptedExpenseTracker> readExpenses(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link EncryptedExpenseTracker} to the storage.
     * @param expenseTracker cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveExpenses(EncryptedExpenseTracker expenseTracker) throws IOException;

    /**
     * @see #saveExpenses(EncryptedExpenseTracker)
     */
    void saveExpenses(EncryptedExpenseTracker expenseTracker, Path filePath) throws IOException;
}
