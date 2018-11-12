package seedu.expensetracker;

import java.io.IOException;
import java.nio.file.Path;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import seedu.expensetracker.commons.core.Config;
import seedu.expensetracker.commons.core.EventsCenter;
import seedu.expensetracker.commons.core.LogsCenter;
import seedu.expensetracker.commons.core.Version;
import seedu.expensetracker.commons.events.ui.ExitAppRequestEvent;
import seedu.expensetracker.commons.exceptions.DataConversionException;
import seedu.expensetracker.commons.exceptions.IllegalValueException;
import seedu.expensetracker.commons.util.ConfigUtil;
import seedu.expensetracker.commons.util.StringUtil;
import seedu.expensetracker.logic.Logic;
import seedu.expensetracker.logic.LogicManager;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.ModelManager;
import seedu.expensetracker.model.ReadOnlyExpenseTracker;
import seedu.expensetracker.model.UserPrefs;
import seedu.expensetracker.model.encryption.EncryptedExpenseTracker;
import seedu.expensetracker.model.encryption.EncryptionUtil;
import seedu.expensetracker.model.notification.Tip;
import seedu.expensetracker.model.notification.Tips;
import seedu.expensetracker.model.user.Username;
import seedu.expensetracker.model.util.SampleDataUtil;
import seedu.expensetracker.storage.ExpensesStorage;
import seedu.expensetracker.storage.JsonTipsStorage;
import seedu.expensetracker.storage.JsonUserPrefsStorage;
import seedu.expensetracker.storage.Storage;
import seedu.expensetracker.storage.StorageManager;
import seedu.expensetracker.storage.TipsStorage;
import seedu.expensetracker.storage.UserPrefsStorage;
import seedu.expensetracker.storage.XmlExpensesStorage;
import seedu.expensetracker.ui.Ui;
import seedu.expensetracker.ui.UiManager;

/**
 * The main entry point to the application.
 */
public class MainApp extends Application {

    public static final Version VERSION = new Version(1, 4, 0, false);

    private static final Logger logger = LogsCenter.getLogger(MainApp.class);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;
    protected UserPrefs userPrefs;

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing ExpenseTracker ]===========================");
        super.init();

        AppParameters appParameters = AppParameters.parse(getParameters());
        config = initConfig(appParameters.getConfigPath());

        UserPrefsStorage userPrefsStorage = new JsonUserPrefsStorage(config.getUserPrefsFilePath());
        userPrefs = initPrefs(userPrefsStorage);
        ExpensesStorage expensesStorage = new XmlExpensesStorage(userPrefs.getExpenseTrackerDirPath());

        TipsStorage tipsStorage = new JsonTipsStorage();
        storage = new StorageManager(expensesStorage, userPrefsStorage, tipsStorage);


        initLogging(config);

        Tips tips = initTips();

        model = initModelManager(storage, userPrefs, tips);

        logic = new LogicManager(model);

        ui = new UiManager(logic, config, userPrefs);

        initEventsCenter();

    }

    /**
     * Returns a {@code ModelManager} with the data from {@code storage}'s expense tracker and {@code userPrefs}. <br>
     * A user "sample" with a sample ExpenseTracker will be added if the username does not exist.
     * or no users will be used instead if errors occur when reading {@code storage}'s expense tracker.
     */
    protected Model initModelManager(Storage storage, UserPrefs userPrefs, Tips tips) {
        Map<Username, EncryptedExpenseTracker> expenseTrackers;
        try {
            expenseTrackers = storage.readAllExpenses(userPrefs.getExpenseTrackerDirPath());


        } catch (DataConversionException e) {
            logger.warning("Data files are not in the correct format. Will be starting with no accounts.");
            expenseTrackers = new TreeMap<>();
        } catch (IOException e) {
            logger.warning("Problem while reading from the files. Will be starting with no accounts");
            expenseTrackers = new TreeMap<>();
        }
        ReadOnlyExpenseTracker sampleExpenseTracker = SampleDataUtil.getSampleExpenseTracker();
        if (!expenseTrackers.containsKey(sampleExpenseTracker.getUsername())) {
            try {
                expenseTrackers.put(sampleExpenseTracker.getUsername(),
                        EncryptionUtil.encryptTracker(sampleExpenseTracker));
            } catch (IllegalValueException e) {
                throw new IllegalStateException("Sample user has invalid key. ");
            }
        }
        return new ModelManager(expenseTrackers, userPrefs, tips);
    }

    protected void initLogging(Config config) {
        LogsCenter.init(config);
    }

    /**
     * Returns a {@code Config} using the file at {@code configFilePath}. <br>
     * The default file path {@code Config#DEFAULT_CONFIG_FILE} will be used instead
     * if {@code configFilePath} is null.
     */
    protected Config initConfig(Path configFilePath) {
        Config initializedConfig;
        Path configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if (configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. "
                    + "Using default config properties");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    /**
     * Returns a {@code UserPrefs} using the file at {@code storage}'s user prefs file path,
     * or a new {@code UserPrefs} with default configuration if errors occur when
     * reading from the file.
     */
    protected UserPrefs initPrefs(UserPrefsStorage storage) {
        Path prefsFilePath = storage.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. "
                    + "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Will be starting with an empty ExpenseTracker");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }


    /**
     * Returns a {@code Tips} using the file at {@code userPref}'s tips file path,
     * or a new {@code tips} with default configuration if errors occur when
     * reading from the file.
     */
    protected Tips initTips() {
        Tips tips;
        List<Tip> tipsList;
        try {
            Optional<List<Tip>> tipsOptional = storage.readTips();
            tipsList = tipsOptional.orElse(new ArrayList<Tip>());
            tips = new Tips(tipsList);

        } catch (IOException e) {
            logger.warning(e.getMessage());
            tips = new Tips();
        }
        return tips;
    }

    protected void initEventsCenter() {
        EventsCenter.getInstance().registerHandler(this);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting ExpenseTracker " + MainApp.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping ExpenseTracker ] =============================");
        ui.stop();
        if (model.hasSelectedUser()) {
            try {
                storage.saveUserPrefs(userPrefs);
            } catch (IOException e) {
                logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
            }
        }
        Platform.exit();
        System.exit(0);
    }

    @Subscribe
    public void handleExitAppRequestEvent(ExitAppRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
