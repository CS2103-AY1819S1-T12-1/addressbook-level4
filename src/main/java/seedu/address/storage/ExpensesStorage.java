package seedu.address.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import seedu.address.commons.exceptions.DataConversionException;
import seedu.address.model.ReadOnlyAddressBook;

/**
 * Represents a storage for {@link seedu.address.model.AddressBook}.
 */
public interface ExpensesStorage {

    /**
     * Returns the file path of the data file.
     */
    Path getExpensesFilePath();

    /**
     * Returns AddressBook data as a {@link ReadOnlyAddressBook}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyAddressBook> readExpenses() throws DataConversionException, IOException;

    /**
     * @see #getExpensesFilePath()
     */
    Optional<ReadOnlyAddressBook> readExpenses(Path filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyAddressBook} to the storage.
     * @param addressBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveExpenses(ReadOnlyAddressBook addressBook) throws IOException;

    /**
     * @see #saveExpenses(ReadOnlyAddressBook)
     */
    void saveExpenses(ReadOnlyAddressBook addressBook, Path filePath) throws IOException;

    /**
     * Backup the given {@link ReadOnlyAddressBook} to the storage.
     * @param addressBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void backupExpenses(ReadOnlyAddressBook addressBook) throws IOException;

    /**
     * @see #backupExpenses(ReadOnlyAddressBook)
     */
    void backupExpenses(ReadOnlyAddressBook addressBook, Path filePath) throws IOException;

}
