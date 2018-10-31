package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

import seedu.address.commons.events.model.ExpenseTrackerChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyExpenseTracker;
import seedu.address.model.UserPrefs;
import seedu.address.model.encryption.EncryptedExpenseTracker;
import seedu.address.model.user.Username;

/**
 * API of the Storage component
 */
public interface Storage extends ExpensesStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    Path getExpensesDirPath();

    @Override
    Optional<EncryptedExpenseTracker> readExpenses() throws DataConversionException, IOException;

    /**
     * Returns all ExpenseTracker data as a map with String keys and {@link ReadOnlyExpenseTracker} values. Data is read
     * from the input dirPath.
     * Creates the directory at the given path if does not exists.
     * @param dirPath cannot be null
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Map<Username, EncryptedExpenseTracker> readAllExpenses(Path dirPath) throws DataConversionException, IOException;

    @Override
    void saveExpenses(EncryptedExpenseTracker expenseTracker) throws IOException;

    /**
     * Saves the current version of the Expense Tracker to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleExpenseTrackerChangedEvent(ExpenseTrackerChangedEvent abce);
}
