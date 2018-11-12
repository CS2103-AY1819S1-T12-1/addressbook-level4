package systemtests;

import java.nio.file.Path;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

import org.junit.Assert;
import org.testfx.api.FxToolkit;

import guitests.guihandles.MainWindowHandle;
import javafx.stage.Stage;

import seedu.expensetracker.TestApp;
import seedu.expensetracker.commons.exceptions.IllegalValueException;
import seedu.expensetracker.logic.LoginCredentials;
import seedu.expensetracker.logic.parser.exceptions.ParseException;
import seedu.expensetracker.model.ReadOnlyExpenseTracker;
import seedu.expensetracker.model.exceptions.InvalidDataException;
import seedu.expensetracker.model.exceptions.NonExistentUserException;
import seedu.expensetracker.testutil.TypicalExpenses;

/**
 * Contains helper methods that system tests require.
 */
public class SystemTestSetupHelper {
    private TestApp testApp;
    private MainWindowHandle mainWindowHandle;

    /**
     * Sets up a new {@code TestApp} and returns it.
     */
    public TestApp setupApplication(Supplier<ReadOnlyExpenseTracker> expenseTracker, Path saveFileLocation) {
        try {
            FxToolkit.registerStage(Stage::new);
            FxToolkit.setupApplication(() -> {
                try {
                    return testApp = new TestApp(expenseTracker, saveFileLocation);
                } catch (IllegalValueException e) {
                    throw new IllegalStateException("Illegal value in sample test data");
                }
            });
        } catch (TimeoutException te) {
            throw new AssertionError("Application takes too long to set up.", te);
        }

        return testApp;
    }

    /**
     * Initializes TestFX.
     */
    public static void initialize() {
        try {
            FxToolkit.registerPrimaryStage();
            FxToolkit.hideStage();
        } catch (TimeoutException e) {
            throw new AssertionError(e);
        }
    }

    /**
     * Encapsulates the primary stage of {@code TestApp} in a {@code MainWindowHandle} and returns it.
     */
    public MainWindowHandle setupMainWindowHandle() {
        try {
            FxToolkit.setupFixture(() -> {
                try {
                    testApp.getActualModel().loadUserData(new LoginCredentials(TypicalExpenses.SAMPLE_USERNAME, null));
                } catch (NonExistentUserException | InvalidDataException | ParseException e) {
                    Assert.fail(e.getMessage());
                }
            });
        } catch (TimeoutException e) {
            Assert.fail(e.getMessage());
        }
        try {
            FxToolkit.setupStage((stage) -> {
                mainWindowHandle = new MainWindowHandle(stage);
                mainWindowHandle.focus();
            });
            FxToolkit.showStage();
        } catch (TimeoutException te) {
            throw new AssertionError("Stage takes too long to set up.", te);
        }

        return mainWindowHandle;
    }

    /**
     * Tears down existing stages.
     */
    public void tearDownStage() {
        try {
            FxToolkit.cleanupStages();
        } catch (TimeoutException te) {
            throw new AssertionError("Stage takes too long to tear down.", te);
        }
    }
}
