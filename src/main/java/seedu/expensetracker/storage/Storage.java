package seedu.expensetracker.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import seedu.expensetracker.commons.events.model.ExpenseTrackerChangedEvent;
import seedu.expensetracker.commons.events.storage.DataSavingExceptionEvent;
import seedu.expensetracker.commons.exceptions.DataConversionException;
import seedu.expensetracker.model.ReadOnlyExpenseTracker;
import seedu.expensetracker.model.UserPrefs;
import seedu.expensetracker.model.encryption.EncryptedExpenseTracker;
import seedu.expensetracker.model.notification.Tip;
import seedu.expensetracker.model.user.Username;

/**
 * API of the Storage component
 */
public interface Storage extends ExpensesStorage, UserPrefsStorage, TipsStorage {

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

    @Override
    Optional<List<Tip>> readTips() throws IOException;
}
