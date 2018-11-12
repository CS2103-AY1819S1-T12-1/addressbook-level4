package seedu.expensetracker.storage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import seedu.expensetracker.commons.core.ComponentManager;
import seedu.expensetracker.commons.core.LogsCenter;
import seedu.expensetracker.commons.events.model.ExpenseTrackerChangedEvent;
import seedu.expensetracker.commons.events.storage.DataSavingExceptionEvent;
import seedu.expensetracker.commons.exceptions.DataConversionException;
import seedu.expensetracker.model.UserPrefs;
import seedu.expensetracker.model.encryption.EncryptedExpenseTracker;
import seedu.expensetracker.model.notification.Tip;
import seedu.expensetracker.model.user.Username;

/**
 * Manages storage of ExpenseTracker data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private ExpensesStorage expensesStorage;
    private UserPrefsStorage userPrefsStorage;
    private TipsStorage tipsStorage;


    public StorageManager(ExpensesStorage expensesStorage, UserPrefsStorage userPrefsStorage, TipsStorage tipsStorage) {
        super();
        this.expensesStorage = expensesStorage;
        this.userPrefsStorage = userPrefsStorage;
        this.tipsStorage = tipsStorage;
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Path getUserPrefsFilePath() {
        return userPrefsStorage.getUserPrefsFilePath();
    }

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ ExpenseTracker methods ==============================

    @Override
    public Path getExpensesDirPath() {
        return expensesStorage.getExpensesDirPath();
    }

    @Override
    public Optional<EncryptedExpenseTracker> readExpenses() throws DataConversionException, IOException {
        return readExpenses(expensesStorage.getExpensesDirPath());
    }

    @Override
    public Optional<EncryptedExpenseTracker> readExpenses(Path filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return expensesStorage.readExpenses(filePath);
    }

    @Override
    public Map<Username, EncryptedExpenseTracker> readAllExpenses(Path dirPath) throws DataConversionException,
            IOException {
        File dir = new File(dirPath.toString());
        final Map<Username, EncryptedExpenseTracker> trackers = new TreeMap<>();
        File[] directoryListing = dir.listFiles();
        if (!dir.mkdir()) {
            if (directoryListing != null) {
                for (File child : directoryListing) {
                    readExpenses(Paths.get(child.getPath())).ifPresent(
                        expenseTracker -> trackers.put(new Username(child.getName().replace(".xml", "")),
                                expenseTracker));
                }
            }
        }
        return trackers;
    }

    @Override
    public void saveExpenses(EncryptedExpenseTracker expenseTracker) throws IOException {
        Path path = Paths.get(expensesStorage.getExpensesDirPath().toString(),
                expenseTracker.getUsername().toString() + ".xml");
        saveExpenses(expenseTracker, path);
    }

    @Override
    public void saveExpenses(EncryptedExpenseTracker expenseTracker, Path filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        expensesStorage.saveExpenses(expenseTracker, filePath);
    }

    @Override
    @Subscribe
    public void handleExpenseTrackerChangedEvent(ExpenseTrackerChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveExpenses(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

    @Override
    public Optional<List<Tip>> readTips() throws IOException {
        Optional<List<Tip>> tipsOptional = tipsStorage.readTips();
        return tipsOptional;
    }


}
