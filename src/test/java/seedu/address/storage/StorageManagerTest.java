package seedu.address.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static seedu.address.model.encryption.EncryptionUtil.DEFAULT_ENCRYPTION_KEY;
import static seedu.address.testutil.TypicalExpenses.getTypicalExpenseTracker;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import seedu.address.commons.events.model.ExpenseTrackerChangedEvent;
import seedu.address.commons.events.storage.DataSavingExceptionEvent;
import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.ExpenseTracker;
import seedu.address.model.ReadOnlyExpenseTracker;
import seedu.address.model.UserPrefs;
import seedu.address.model.encryption.EncryptedExpenseTracker;
import seedu.address.model.encryption.EncryptionUtil;
import seedu.address.testutil.ModelUtil;
import seedu.address.ui.testutil.EventsCollectorRule;

public class StorageManagerTest {

    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    @Rule
    public final EventsCollectorRule eventsCollectorRule = new EventsCollectorRule();

    private StorageManager storageManager;

    @Before
    public void setUp() {
        XmlExpensesStorage expenseTrackerStorage = new XmlExpensesStorage(getTempFilePath("ab"));
        JsonUserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(getTempFilePath("prefs"));
        JsonTipsStorage tipsStorage = new JsonTipsStorage(getTempFilePath("tips"));
        storageManager = new StorageManager(expenseTrackerStorage, userPrefsStorage, tipsStorage);
    }

    private Path getTempFilePath(String fileName) {
        return testFolder.getRoot().toPath().resolve(fileName);
    }


    @Test
    public void prefsReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link JsonUserPrefsStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link JsonUserPrefsStorageTest} class.
         */
        UserPrefs original = new UserPrefs();
        original.setGuiSettings(300, 600, 4, 6);
        storageManager.saveUserPrefs(original);
        UserPrefs retrieved = storageManager.readUserPrefs().get();
        assertEquals(original, retrieved);
    }

    @Test
    public void expenseTrackerReadSave() throws Exception {
        /*
         * Note: This is an integration test that verifies the StorageManager is properly wired to the
         * {@link XmlExpensesStorage} class.
         * More extensive testing of UserPref saving/reading is done in {@link XmlExpensesStorageTest} class.
         */
        ExpenseTracker original = getTypicalExpenseTracker();
        storageManager.saveExpenses(EncryptionUtil.encryptTracker(original));
        ReadOnlyExpenseTracker retrieved = storageManager.readAllExpenses(storageManager.getExpensesDirPath())
                .get(original.getUsername()).decryptTracker(DEFAULT_ENCRYPTION_KEY);
        assertEquals(original, new ExpenseTracker(retrieved));
    }

    @Test
    public void getExpenseTrackerDirPath() {
        assertNotNull(storageManager.getExpensesDirPath());
    }

    @Test
    public void handleExpenseTrackerChangedEvent_exceptionThrown_eventRaised() throws IllegalValueException {
        // Create a StorageManager while injecting a stub that  throws an exception when the save method is called
        Storage storage = new StorageManager(new XmlExpensesStorageExceptionThrowingStub(Paths.get("dummy")),
                                             new JsonUserPrefsStorage(Paths.get("dummy")),
                                             new JsonTipsStorage(Paths.get("dummy")));
        storage.handleExpenseTrackerChangedEvent(
                new ExpenseTrackerChangedEvent(EncryptionUtil.encryptTracker(
                        new ExpenseTracker(ModelUtil.TEST_USERNAME, null,
                                DEFAULT_ENCRYPTION_KEY))));
        assertTrue(eventsCollectorRule.eventsCollector.getMostRecent() instanceof DataSavingExceptionEvent);
    }


    /**
     * A Stub class to throw an exception when the save method is called
     */
    class XmlExpensesStorageExceptionThrowingStub extends XmlExpensesStorage {

        public XmlExpensesStorageExceptionThrowingStub(Path filePath) {
            super(filePath);
        }

        @Override
        public void saveExpenses(EncryptedExpenseTracker expenseTracker, Path filePath) throws IOException {
            throw new IOException("dummy exception");
        }
    }


}
