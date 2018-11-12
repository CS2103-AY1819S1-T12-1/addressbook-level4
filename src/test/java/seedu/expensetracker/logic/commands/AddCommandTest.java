package seedu.expensetracker.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.expensetracker.model.encryption.EncryptionUtil.DEFAULT_ENCRYPTION_KEY;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.model.ExpenseTracker;
import seedu.expensetracker.model.ReadOnlyExpenseTracker;
import seedu.expensetracker.model.budget.CategoryBudget;
import seedu.expensetracker.model.budget.TotalBudget;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.model.expense.Expense;
import seedu.expensetracker.model.notification.Notification;
import seedu.expensetracker.model.user.Username;
import seedu.expensetracker.testutil.ExpenseBuilder;
import seedu.expensetracker.testutil.ModelStub;

public class AddCommandTest {

    private static final CommandHistory EMPTY_COMMAND_HISTORY = new CommandHistory();

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private CommandHistory commandHistory = new CommandHistory();

    @Test
    public void constructor_nullExpense_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new AddCommand(null);
    }
    @Test
    public void execute_expenseAcceptedByModel_addSuccessful() throws Exception {
        ModelStubAcceptingExpenseAdded modelStub = new ModelStubAcceptingExpenseAdded();
        Expense validExpense = new ExpenseBuilder().build();

        CommandResult commandResult = new AddCommand(validExpense).execute(modelStub, commandHistory);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validExpense), commandResult.feedbackToUser);
        assertEquals(Arrays.asList(validExpense), modelStub.expensesAdded);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
        assertFalse(modelStub.addWarningNotification());
    }

    @Test
    public void execute_expenseAcceptedByModel_budgetExceed() throws Exception {
        ModelStubBudget modelStub = new ModelStubBudget(false);
        Expense validExpense = new ExpenseBuilder().build();
        CommandResult commandResult = new AddCommand(validExpense).execute(modelStub, commandHistory);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validExpense), commandResult.feedbackToUser);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
        assertTrue(modelStub.addWarningNotification());
    }

    @Test
    public void execute_expenseAcceptedByModel_withinBudget() throws Exception {
        ModelStubBudget modelStub = new ModelStubBudget(true);
        Expense validExpense = new ExpenseBuilder().build();
        CommandResult commandResult = new AddCommand(validExpense).execute(modelStub, commandHistory);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validExpense), commandResult.feedbackToUser);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
        assertFalse(modelStub.addWarningNotification());
    }

    @Test
    public void execute_duplicateExpense_addSuccessful() throws Exception {
        Expense validExpense = new ExpenseBuilder().build();
        ModelStub modelStub = new ModelStubAcceptingExpenseAdded();
        CommandResult commandResult = new AddCommand(validExpense).execute(modelStub, commandHistory);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validExpense), commandResult.feedbackToUser);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
        assertFalse(modelStub.addWarningNotification());
    }

    @Test
    public void equals() {
        Expense alice = new ExpenseBuilder().withName("Alice").build();
        Expense bob = new ExpenseBuilder().withName("Bob").build();
        AddCommand addAliceCommand = new AddCommand(alice);
        AddCommand addBobCommand = new AddCommand(bob);

        // same object -> returns true
        assertTrue(addAliceCommand.equals(addAliceCommand));

        // same values -> returns true
        AddCommand addAliceCommandCopy = new AddCommand(alice);
        assertTrue(addAliceCommand.equals(addAliceCommandCopy));

        // different types -> returns false
        assertFalse(addAliceCommand.equals(1));

        // null -> returns false
        assertFalse(addAliceCommand.equals(null));

        // different expense -> returns false
        assertFalse(addAliceCommand.equals(addBobCommand));
    }

    /**
     * A Model stub that contains a single expense.
     */
    private class ModelStubWithExpense extends ModelStub {
        private final Expense expense;

        ModelStubWithExpense(Expense expense) {
            requireNonNull(expense);
            this.expense = expense;
        }

        @Override
        public boolean hasExpense(Expense expense) {
            requireNonNull(expense);
            return this.expense.isSameExpense(expense);
        }

        @Override
        public TotalBudget getMaximumBudget() {
            // called by {@param UpdateBudgetDisplayEvent}
            return new TotalBudget(0, 0);
        }
    }

    /**
     * A Model stub that always accept the expense being added.
     */
    private class ModelStubAcceptingExpenseAdded extends ModelStub {
        final ArrayList<Expense> expensesAdded = new ArrayList<>();


        @Override
        public boolean hasExpense(Expense expense) {
            requireNonNull(expense);
            return expensesAdded.stream().anyMatch(expense::isSameExpense);
        }

        @Override
        public boolean addExpense(Expense expense) {
            requireNonNull(expense);
            expensesAdded.add(expense);
            return true;
        }

        @Override
        public void commitExpenseTracker() {
            // called by {@code AddCommand#execute()}
        }

        @Override
        public ReadOnlyExpenseTracker getExpenseTracker() {
            return new ExpenseTracker(new Username("aa"), null, DEFAULT_ENCRYPTION_KEY);
        }

        @Override
        public TotalBudget getMaximumBudget() {
            // called by {@param UpdateBudgetDisplayEvent}
            return new TotalBudget(0, 0);
        }

        @Override
        public boolean addWarningNotification() {
            return false;
        }

        @Override
        public HashSet<CategoryBudget> getCategoryBudgets() throws NoUserSelectedException {
            return new HashSet<>();
        }
    }

    /**
     * A Model stub that will always result in a successful add, but can be within or above the totalBudget
     */
    private class ModelStubBudget extends ModelStub {
        private final boolean withinBudget;

        public ModelStubBudget(boolean withinBudget) {
            this.withinBudget = withinBudget;
        }
        @Override
        public boolean hasExpense(Expense expense) {
            return false;
        }
        @Override
        public void commitExpenseTracker() {
            // called by {@code AddCommand#execute()}
        }
        @Override
        public void addGeneralNotification(Notification notif) {
            // called by (@code AddCommand#execute()}
        }
        @Override
        public boolean addExpense(Expense expense) {
            return this.withinBudget;
        }
        @Override
        public ReadOnlyExpenseTracker getExpenseTracker() {
            return new ExpenseTracker(new Username("aa"), null, DEFAULT_ENCRYPTION_KEY);
        }

        @Override
        public TotalBudget getMaximumBudget() {
            // called by {@param UpdateBudgetDisplayEvent}
            return new TotalBudget(0, 0);
        }

        @Override
        public boolean addWarningNotification() {
            if (withinBudget) {
                return false;
            } else {
                return true;
            }
        }

        @Override
        public HashSet<CategoryBudget> getCategoryBudgets() throws NoUserSelectedException {
            return new HashSet<>();
        }
    }


}
