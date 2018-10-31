package seedu.address.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_EXPENSES;
import static seedu.address.testutil.TypicalExpenses.ICECREAM;
import static seedu.address.testutil.TypicalExpenses.SCHOOLFEE;

import java.nio.file.Paths;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.StatsCommand.StatsMode;
import seedu.address.logic.commands.StatsCommand.StatsPeriod;
import seedu.address.logic.parser.ArgumentMultimap;
import seedu.address.logic.parser.ArgumentTokenizer;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.exceptions.InvalidDataException;
import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.model.exceptions.NonExistentUserException;
import seedu.address.model.exceptions.UserAlreadyExistsException;
import seedu.address.model.expense.ExpenseContainsKeywordsPredicate;
import seedu.address.testutil.ExpenseTrackerBuilder;
import seedu.address.testutil.ModelUtil;

public class ModelManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ModelManager modelManager = (ModelManager) ModelUtil.modelWithTestUser();
    private ModelManager modelManagerLoggedOut = new ModelManager();

    public ModelManagerTest() throws UserAlreadyExistsException, NonExistentUserException, NoUserSelectedException,
            InvalidDataException, ParseException {
    }

    @Test
    public void checkBudgetRestart_noFrequency_doesNotResetSpending() throws NoUserSelectedException {
        double previousExpenses = modelManager.getMaximumBudget().getCurrentExpenses();
        modelManager.checkBudgetRestart();
        assertTrue(modelManager.getExpenseTracker().getMaximumTotalBudget().getCurrentExpenses() == previousExpenses);
    }

    @Test
    public void checkBudgetRestart_frequency_doesNotResetSpendingIfNotNextRecurrence() throws NoUserSelectedException {
        double previousExpenses = modelManager.getMaximumBudget().getCurrentExpenses();
        modelManager.setRecurrenceFrequency(Integer.MAX_VALUE);
        modelManager.checkBudgetRestart();
        assertTrue(modelManager.getExpenseTracker().getMaximumTotalBudget().getCurrentExpenses() == previousExpenses);
    }

    @Test
    public void checkBudgetRestart_frequency_resetSpendingIfNextRecurrence() throws NoUserSelectedException {
        modelManager.setRecurrenceFrequency(0);
        try {

            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.out.println("Interrupted. Skipping test.");
            return;
        }
        modelManager.checkBudgetRestart();
        assertTrue(modelManager.getExpenseTracker().getMaximumTotalBudget().getCurrentExpenses() == 0);
    }


    @Test
    public void hasExpense_nullExpense_throwsNullPointerException() throws NoUserSelectedException {
        thrown.expect(NullPointerException.class);
        modelManager.hasExpense(null);
    }

    @Test
    public void hasExpense_expenseNotInExpenseTracker_returnsFalse() throws NoUserSelectedException {
        assertFalse(modelManager.hasExpense(SCHOOLFEE));
    }

    @Test
    public void hasExpense_expenseInExpenseTracker_returnsTrue() throws NoUserSelectedException {
        modelManager.addExpense(SCHOOLFEE);
        assertTrue(modelManager.hasExpense(SCHOOLFEE));
    }

    @Test
    public void getFilteredExpenseList_modifyList_throwsUnsupportedOperationException() throws NoUserSelectedException {
        thrown.expect(UnsupportedOperationException.class);
        modelManager.getFilteredExpenseList().remove(0);
    }

    @Test
    public void getExpenseTracker_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.getExpenseTracker();
    }

    @Test
    public void indicateExpenseTrackerChanged_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.indicateExpenseTrackerChanged();
    }

    @Test
    public void hasExpense_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.hasExpense(SCHOOLFEE);
    }

    @Test
    public void getFilteredExpenseList_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.getFilteredExpenseList();
    }

    @Test
    public void updateFilteredExpenseList_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.updateFilteredExpenseList(unused -> true);
    }

    @Test
    public void canUndoExpenseTracker_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.canUndoExpenseTracker();
    }

    @Test
    public void commitExpenseTracker_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.commitExpenseTracker();
    }

    @Test
    public void getExpenseStats_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.getExpenseStats();
    }

    @Test
    public void updateExpenseStats_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.updateExpenseStatsPredicate(unused -> true);
    }

    @Test
    public void addWarningNotification_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.addWarningNotification();
    }

    @Test
    public void addTipNotification_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.addTipNotification();
    }

    @Test
    public void getNotificationList_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.getNotificationList();
    }

    @Test
    public void toggleTipNotification_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.toggleTipNotification(false);
    }

    @Test
    public void toggleWarningNotification_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.toggleWarningNotification(false);
    }

    @Test
    public void toggleBothNotification_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.toggleBothNotification(false);
    }

    @Test
    public void getNotification_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.getNotificationHandler();
    }

    @Test
    public void getStatsPeriodReturnsCorrectStatsPeriod() {
        modelManager.updateStatsPeriod(StatsPeriod.DAY);
        assertTrue(modelManager.getStatsPeriod() == StatsPeriod.DAY);
        modelManager.updateStatsPeriod(StatsPeriod.MONTH);
        assertTrue(modelManager.getStatsPeriod() == StatsPeriod.MONTH);
    }

    @Test
    public void getStatsModeReturnsCorrectStatsMode() {
        modelManager.updateStatsMode(StatsMode.TIME);
        assertTrue(modelManager.getStatsMode() == StatsMode.TIME);
        modelManager.updateStatsMode(StatsMode.CATEGORY);
        assertTrue(modelManager.getStatsMode() == StatsMode.CATEGORY);
    }

    @Test
    public void getPeriodAmountReturnsCorrectPeriodAmount() {
        modelManager.updatePeriodAmount(7);
        assertTrue(modelManager.getPeriodAmount() == 7);
    }

    @Test
    public void indicateUserLoggedIn_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.indicateUserLoggedIn();
    }

    @Test
    public void equals() throws NoUserSelectedException {
        ExpenseTracker expenseTracker =
                new ExpenseTrackerBuilder().withExpense(SCHOOLFEE).withExpense(ICECREAM).build();
        ExpenseTracker differentExpenseTracker = new ExpenseTracker(ModelUtil.TEST_USERNAME, null, null);
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(expenseTracker, userPrefs, null);
        ModelManager modelManagerCopy = new ModelManager(expenseTracker, userPrefs, null);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different expenseTracker -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentExpenseTracker, userPrefs, null)));

        // different filteredList -> returns false
        ArgumentMultimap keywordsMap = ArgumentTokenizer.tokenize(" n/"
                + SCHOOLFEE.getName().expenseName, PREFIX_NAME);
        modelManager.updateFilteredExpenseList(new ExpenseContainsKeywordsPredicate(keywordsMap));
        assertFalse(modelManager.equals(new ModelManager(expenseTracker, userPrefs, null)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredExpenseList(PREDICATE_SHOW_ALL_EXPENSES);


        // different userPrefs -> returns true
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setExpenseTrackerDirPath(Paths.get("differentFilePath"));
        assertTrue(modelManager.equals(new ModelManager(expenseTracker, differentUserPrefs, null)));
    }
}
