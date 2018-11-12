package systemtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.expensetracker.ui.StatusBarFooter.SYNC_STATUS_UPDATED;
import static seedu.expensetracker.ui.testutil.GuiTestAssert.assertExpenseListMatching;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;

import guitests.guihandles.BudgetPanelHandle;
import guitests.guihandles.CommandBoxHandle;
import guitests.guihandles.ExpenseListPanelHandle;
import guitests.guihandles.MainMenuHandle;
import guitests.guihandles.MainWindowHandle;
import guitests.guihandles.ResultDisplayHandle;
import guitests.guihandles.StatusBarFooterHandle;
import seedu.expensetracker.TestApp;
import seedu.expensetracker.commons.core.EventsCenter;
import seedu.expensetracker.commons.core.index.Index;
import seedu.expensetracker.commons.exceptions.IllegalValueException;
import seedu.expensetracker.logic.commands.ClearCommand;
import seedu.expensetracker.logic.commands.FindCommand;
import seedu.expensetracker.logic.commands.ListCommand;
import seedu.expensetracker.logic.commands.SelectCommand;
import seedu.expensetracker.model.ExpenseTracker;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.testutil.TypicalExpenses;
import seedu.expensetracker.ui.CommandBox;

/**
 * A system test class for ExpenseTracker, which provides access to handles of GUI components and helper methods
 * for test verification.
 */
public abstract class ExpenseTrackerSystemTest {
    @ClassRule
    public static ClockRule clockRule = new ClockRule();

    private static final List<String> COMMAND_BOX_DEFAULT_STYLE = Arrays.asList("text-input", "text-field");
    private static final List<String> COMMAND_BOX_ERROR_STYLE =
            Arrays.asList("text-input", "text-field", CommandBox.ERROR_STYLE_CLASS);

    protected TestApp testApp;

    private MainWindowHandle mainWindowHandle;
    private SystemTestSetupHelper setupHelper;
    @BeforeClass
    public static void setupBeforeClass() {
        SystemTestSetupHelper.initialize();
    }

    @Before
    public void setUp() throws NoUserSelectedException {
        setupHelper = new SystemTestSetupHelper();
        testApp = setupHelper.setupApplication(this::getInitialData, getDataFileLocation());
        mainWindowHandle = setupHelper.setupMainWindowHandle();
        assertApplicationStartingStateIsCorrect();

    }

    @After
    public void tearDown() {
        setupHelper.tearDownStage();
        EventsCenter.clearSubscribers();
    }

    /**
     * Returns the data to be loaded into the file in {@link #getDataFileLocation()}.
     */
    protected ExpenseTracker getInitialData() {
        return TypicalExpenses.getTypicalExpenseTracker();
    }

    /**
     * Returns the directory of the data file.
     */
    protected Path getDataFileLocation() {
        return TestApp.SAVE_LOCATION_FOR_TESTING;
    }

    public MainWindowHandle getMainWindowHandle() {
        return mainWindowHandle;
    }

    public CommandBoxHandle getCommandBox() {
        return mainWindowHandle.getCommandBox();
    }

    public ExpenseListPanelHandle getExpenseListPanel() {
        return mainWindowHandle.getExpenseListPanel();
    }

    public MainMenuHandle getMainMenu() {
        return mainWindowHandle.getMainMenu();
    }

    public StatusBarFooterHandle getStatusBarFooter() {
        return mainWindowHandle.getStatusBarFooter();
    }

    public ResultDisplayHandle getResultDisplay() {
        return mainWindowHandle.getResultDisplay();
    }

    public BudgetPanelHandle getBudgetPanel() {
        return mainWindowHandle.getBudgetPanel();
    }

    /**
     * Executes {@code command} in the application's {@code CommandBox}.
     * Method returns after UI components have been updated.
     */
    protected void executeCommand(String command) {
        rememberStates();
        // Injects a fixed clock before executing a command so that the time stamp shown in the status bar
        // after each command is predictable and also different from the previous command.
        clockRule.setInjectedClockToCurrentTime();

        mainWindowHandle.getCommandBox().run(command);

    }

    /**
     * Displays all expenses in the expense tracker.
     */
    protected void showAllExpenses() throws NoUserSelectedException {

        executeCommand(ListCommand.COMMAND_WORD);
        assertEquals(getModel().getExpenseTracker().getExpenseList().size(),
                getModel().getFilteredExpenseList().size());
    }

    /**
     * Displays all expenses with any parts of their names matching {@code keyword} (case-insensitive).
     */
    protected void showExpensesWithName(String keyword) throws NoUserSelectedException {
        executeCommand(FindCommand.COMMAND_WORD + " " + keyword);
        assertTrue(getModel().getFilteredExpenseList().size()
                < getModel().getExpenseTracker().getExpenseList().size());
    }

    /**
     * Selects the expense at {@code index} of the displayed list.
     */
    protected void selectExpense(Index index) {
        executeCommand(SelectCommand.COMMAND_WORD + " " + index.getOneBased());
        assertEquals(index.getZeroBased(), getExpenseListPanel().getSelectedCardIndex());
    }

    /**
     * Deletes all expenses in the expense tracker.
     */
    protected void deleteAllExpenses() throws NoUserSelectedException {
        executeCommand(ClearCommand.COMMAND_WORD);
        try {
            Thread.sleep(1000);
            assertTrue(getBudgetPanel().isExpenseCorrect("0.00"));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertEquals(0, getModel().getExpenseTracker().getExpenseList().size());
    }

    /**
     * Asserts that the {@code CommandBox} displays {@code expectedCommandInput}, the {@code ResultDisplay} displays
     * {@code expectedResultMessage}, the storage contains the same expense objects as {@code expectedModel}
     * and the expense list panel displays the expenses in the model correctly.
     */
    protected void assertApplicationDisplaysExpected(String expectedCommandInput, String expectedResultMessage,
            Model expectedModel) throws NoUserSelectedException, IllegalValueException {
        assertEquals(expectedCommandInput, getCommandBox().getInput());
        assertEquals(expectedResultMessage, getResultDisplay().getText());
        assertEquals(new ExpenseTracker(expectedModel.getExpenseTracker()), testApp.readStorageExpenseTracker());
        assertExpenseListMatching(getExpenseListPanel(), expectedModel.getFilteredExpenseList());
    }

    /**
     * Calls {@code BrowserPanelHandle}, {@code ExpenseListPanelHandle} and {@code StatusBarFooterHandle} to remember
     * their current state.
     */
    private void rememberStates() {
        StatusBarFooterHandle statusBarFooterHandle = getStatusBarFooter();
        statusBarFooterHandle.rememberSaveLocation();
        statusBarFooterHandle.rememberSyncStatus();
        getExpenseListPanel().rememberSelectedExpenseCard();
    }

    /**
     * Asserts that the previously selected card is now deselected and the browser's url remains displaying the details
     * of the previously selected expense.
     */
    protected void assertSelectedCardDeselected() {
        assertFalse(getExpenseListPanel().isAnyCardSelected());
    }

    /**
     * Asserts that the browser's url is changed to display the details of the expense in the expense list panel at
     * {@code expectedSelectedCardIndex}, and only the card at {@code expectedSelectedCardIndex} is selected.
     * @see ExpenseListPanelHandle#isSelectedExpenseCardChanged()
     */
    protected void assertSelectedCardChanged(Index expectedSelectedCardIndex) {
        getExpenseListPanel().navigateToCard(getExpenseListPanel().getSelectedCardIndex());

        assertEquals(expectedSelectedCardIndex.getZeroBased(), getExpenseListPanel().getSelectedCardIndex());
    }

    /**
     * Asserts that the browser's url and the selected card in the expense list panel remain unchanged.
     * @see ExpenseListPanelHandle#isSelectedExpenseCardChanged()
     */
    protected void assertSelectedCardUnchanged() {
        assertFalse(getExpenseListPanel().isSelectedExpenseCardChanged());
    }

    /**
     * Asserts that the command box's shows the default style.
     */
    protected void assertCommandBoxShowsDefaultStyle() {
        assertEquals(COMMAND_BOX_DEFAULT_STYLE, getCommandBox().getStyleClass());
    }

    /**
     * Asserts that the command box's shows the error style.
     */
    protected void assertCommandBoxShowsErrorStyle() {
        assertEquals(COMMAND_BOX_ERROR_STYLE, getCommandBox().getStyleClass());
    }

    /**
     * Asserts that the entire status bar remains the same.
     */
    protected void assertStatusBarUnchanged() {
        StatusBarFooterHandle handle = getStatusBarFooter();
        assertFalse(handle.isSaveLocationChanged());
        assertFalse(handle.isSyncStatusChanged());
    }

    /**
     * Asserts that only the sync status in the status bar was changed to the timing of
     * {@code ClockRule#getInjectedClock()}, while the save location remains the same.
     */
    protected void assertStatusBarUnchangedExceptSyncStatus() {
        StatusBarFooterHandle handle = getStatusBarFooter();
        String timestamp = new Date(clockRule.getInjectedClock().millis()).toString();
        String expectedSyncStatus = String.format(SYNC_STATUS_UPDATED, timestamp);
        assertEquals(expectedSyncStatus, handle.getSyncStatus());
        assertFalse(handle.isSaveLocationChanged());
    }

    /**
     * Asserts that the starting state of the application is correct.
     */
    private void assertApplicationStartingStateIsCorrect() {
        assertEquals("", getCommandBox().getInput());
        assertEquals("", getResultDisplay().getText());
        //assertExpenseListMatching(getExpenseListPanel(), getModel().getFilteredExpenseList());
        assertEquals(Paths.get(".").resolve(testApp.getStorageSaveLocation()).toString(),
                getStatusBarFooter().getSaveLocation());
    }

    /**
     * Returns a copy of the current model.
     */
    protected Model getModel() {
        return testApp.getModel();
    }
}
