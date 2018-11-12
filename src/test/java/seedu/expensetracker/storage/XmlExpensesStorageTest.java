package seedu.expensetracker.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static seedu.expensetracker.model.encryption.EncryptionUtil.DEFAULT_ENCRYPTION_KEY;
import static seedu.expensetracker.testutil.TypicalExpenses.GAMBLE;
import static seedu.expensetracker.testutil.TypicalExpenses.SCHOOLFEE;
import static seedu.expensetracker.testutil.TypicalExpenses.STOCK;
import static seedu.expensetracker.testutil.TypicalExpenses.getTypicalExpenseTracker;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.rules.TemporaryFolder;

import seedu.expensetracker.commons.exceptions.DataConversionException;
import seedu.expensetracker.commons.exceptions.IllegalValueException;
import seedu.expensetracker.model.ExpenseTracker;
import seedu.expensetracker.model.ReadOnlyExpenseTracker;
import seedu.expensetracker.model.encryption.EncryptedExpenseTracker;
import seedu.expensetracker.model.encryption.EncryptionUtil;
import seedu.expensetracker.model.user.Username;
import seedu.expensetracker.testutil.ModelUtil;

public class XmlExpensesStorageTest {
    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "XmlExpensesStorageTest");

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();

    @Test
    public void readExpenseTracker_nullFilePath_throwsNullPointerException() throws Exception {
        thrown.expect(NullPointerException.class);
        readExpenseTracker(null);
    }

    private Optional<EncryptedExpenseTracker> readExpenseTracker(String filePath) throws Exception {
        return new XmlExpensesStorage(Paths.get(filePath)).readExpenses(addToTestDataPathIfNotNull(filePath));
    }

    private Path addToTestDataPathIfNotNull(String prefsFileInTestDataFolder) {
        return prefsFileInTestDataFolder != null
                ? TEST_DATA_FOLDER.resolve(prefsFileInTestDataFolder)
                : null;
    }

    @Test
    public void read_missingFile_emptyResult() throws Exception {
        assertFalse(readExpenseTracker("NonExistentFile.xml").isPresent());
    }

    @Test
    public void read_notXmlFormat_exceptionThrown() throws Exception {

        thrown.expect(DataConversionException.class);
        readExpenseTracker("NotXmlFormatExpenseTracker.xml");

        /* IMPORTANT: Any code below an exception-throwing line (like the one above) will be ignored.
         * That means you should not have more than one exception test in one method
         */
    }

    @Test
    public void readExpenseTracker_invalidExpenseExpenseTracker_throwDataConversionException() throws Exception {
        thrown.expect(DataConversionException.class);
        readExpenseTracker("invalidExpenseExpenseTracker.xml");
    }

    @Test
    public void readExpenseTracker_invalidAndValidExpenseExpenseTracker_throwDataConversionException()
            throws Exception {
        thrown.expect(DataConversionException.class);
        readExpenseTracker("invalidAndValidExpenseExpenseTracker.xml");
    }

    @Test
    public void readAndSaveExpenseTracker_allInOrder_success() throws Exception {
        Path filePath = testFolder.getRoot().toPath().resolve("TempExpenseTracker.xml");
        ExpenseTracker original = getTypicalExpenseTracker();
        original.setUsername(new Username("TempExpenseTracker"));
        XmlExpensesStorage xmlExpenseTrackerStorage = new XmlExpensesStorage(filePath);
        //Save in new file and read back
        xmlExpenseTrackerStorage.saveExpenses(EncryptionUtil.encryptTracker(original), filePath);
        ReadOnlyExpenseTracker readBack =
                xmlExpenseTrackerStorage.readExpenses(filePath).get().decryptTracker(DEFAULT_ENCRYPTION_KEY);

        assertEquals(original, new ExpenseTracker(readBack));

        //Modify data, overwrite exiting file, and read back
        original.addExpense(STOCK);
        original.removeExpense(SCHOOLFEE);
        xmlExpenseTrackerStorage.saveExpenses(EncryptionUtil.encryptTracker(original), filePath);
        readBack = xmlExpenseTrackerStorage.readExpenses(filePath).get().decryptTracker(DEFAULT_ENCRYPTION_KEY);
        assertEquals(original, new ExpenseTracker(readBack));

        //Save and read without specifying file path
        original.addExpense(GAMBLE);
        xmlExpenseTrackerStorage.saveExpenses(EncryptionUtil.encryptTracker(original)); //file path not specified
        //file path not specified
        readBack = xmlExpenseTrackerStorage.readExpenses().get().decryptTracker(DEFAULT_ENCRYPTION_KEY);
        assertEquals(original, new ExpenseTracker(readBack));

    }

    @Test
    public void saveExpenseTracker_nullExpenseTracker_throwsNullPointerException() throws IllegalValueException {
        thrown.expect(NullPointerException.class);
        saveExpenseTracker(null, "SomeFile.xml");
    }

    /**
     * Saves {@code expenseTracker} at the specified {@code filePath}.
     */
    private void saveExpenseTracker(ReadOnlyExpenseTracker expenseTracker, String filePath) throws
            IllegalValueException {
        try {
            new XmlExpensesStorage(Paths.get(filePath))
                    .saveExpenses(EncryptionUtil.encryptTracker(expenseTracker),
                            addToTestDataPathIfNotNull(filePath));
        } catch (IOException ioe) {
            throw new AssertionError("There should not be an error writing to the file.", ioe);
        }
    }

    @Test
    public void saveExpenseTracker_nullFilePath_throwsNullPointerException() throws IllegalValueException {
        thrown.expect(NullPointerException.class);
        saveExpenseTracker(new ExpenseTracker(ModelUtil.TEST_USERNAME, null, DEFAULT_ENCRYPTION_KEY), null);
    }


}
