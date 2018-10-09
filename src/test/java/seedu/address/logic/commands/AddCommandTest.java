package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Predicate;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.ObservableList;
import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.UserPrefs;
import seedu.address.model.budget.Budget;
import seedu.address.model.expense.Expense;
import seedu.address.model.user.Username;
import seedu.address.testutil.ExpenseBuilder;

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
    }

    @Test
    public void execute_expenseAcceptedByModel_budgetExceed() throws Exception {
        ModelStubBudget modelStub = new ModelStubBudget(false);
        Expense validExpense = new ExpenseBuilder().build();
        CommandResult commandResult = new AddCommand(validExpense).execute(modelStub, commandHistory);

        assertEquals(AddCommand.MESSAGE_BUDGET_EXCEED_WARNING, commandResult.feedbackToUser);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_expenseAcceptedByModel_withinBudget() throws Exception {
        ModelStubBudget modelStub = new ModelStubBudget(true);
        Expense validExpense = new ExpenseBuilder().build();
        CommandResult commandResult = new AddCommand(validExpense).execute(modelStub, commandHistory);

        assertEquals(String.format(AddCommand.MESSAGE_SUCCESS, validExpense), commandResult.feedbackToUser);
        assertEquals(EMPTY_COMMAND_HISTORY, commandHistory);
    }

    @Test
    public void execute_duplicateExpense_throwsCommandException() throws Exception {
        Expense validExpense = new ExpenseBuilder().build();
        AddCommand addCommand = new AddCommand(validExpense);
        ModelStub modelStub = new ModelStubWithExpense(validExpense);

        thrown.expect(CommandException.class);
        thrown.expectMessage(AddCommand.MESSAGE_DUPLICATE_EXPENSE);
        addCommand.execute(modelStub, commandHistory);
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
     * A default model stub that have all of the methods failing.
     */
    private class ModelStub implements Model {
        @Override
        public boolean addExpense(Expense expense) {
            throw new AssertionError("addExpense method should not be called.");
        }

        @Override
        public void resetData(ReadOnlyAddressBook newData) {
            throw new AssertionError("resetData method should not be called.");
        }

        @Override
        public void modifyMaximumBudget(Budget budget) {
            throw new AssertionError("modifyMaximumBudget method should not be called.");
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError("getAddressBook should not be called.");
        }

        @Override
        public boolean hasExpense(Expense expense) {
            throw new AssertionError("hasExpense method should not be called.");
        }

        @Override
        public void deleteExpense(Expense target) {
            throw new AssertionError("deleteExpense method should not be called.");
        }

        @Override
        public void updateExpense(Expense target, Expense editedExpense) {
            throw new AssertionError("updateExpense method should not be called.");
        }

        @Override
        public ObservableList<Expense> getFilteredExpenseList() {
            throw new AssertionError("getFilteredExpenseList method should not be called.");
        }

        @Override
        public void updateFilteredExpenseList(Predicate<Expense> predicate) {
            throw new AssertionError("updateFilteredExpenseList method should not be called.");
        }

        @Override
        public ObservableList<Expense> getExpenseStats() {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public void updateExpenseStats(Predicate<Expense> predicate) {
            throw new AssertionError("This method should not be called.");
        }

        @Override
        public boolean canUndoAddressBook() {
            throw new AssertionError("canUndoAddressBook method should not be called.");
        }

        @Override
        public boolean canRedoAddressBook() {
            throw new AssertionError("canRedoAddressBook method should not be called.");
        }

        @Override
        public void undoAddressBook() {
            throw new AssertionError("undoAddressBook method should not be called.");
        }

        @Override
        public void redoAddressBook() {
            throw new AssertionError("redoAddressBook method should not be called.");
        }

        @Override
        public void commitAddressBook() {
            throw new AssertionError("commitAddressBook method should not be called.");
        }

        @Override
        public void loadUserData(Username username) {
            throw new AssertionError("loadUserData method should not be called.");
        }

        @Override
        public void unloadUserData() {
            throw new AssertionError("unloadUserData method should not be called.");
        }

        @Override
        public boolean isUserExists(Username username) {
            throw new AssertionError("isUserExists method should not be called.");
        }

        @Override
        public void addUser(Username username) {
            throw new AssertionError("addUser method should not be called.");
        }

        @Override
        public boolean hasSelectedUser() {
            throw new AssertionError("hasSelectedUser method should not be called.");
        }

        @Override
        public Model copy(UserPrefs userPrefs) {
            throw new AssertionError("copy method should not be called.");
        }

        @Override
        public Budget getMaximumBudget() {
            throw new AssertionError("getMaximumBudget method should not be called.");
        }

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
        public void commitAddressBook() {
            // called by {@code AddCommand#execute()}
        }

        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook(new Username("aa"));
        }
    }

    /**
     * A Model stub that will always result in a successful add, but can be within or above the budget
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
        public void commitAddressBook() {
            // called by {@code AddCommand#execute()}
        }
        @Override
        public boolean addExpense(Expense expense) {
            return this.withinBudget;
        }
        @Override
        public ReadOnlyAddressBook getAddressBook() {
            return new AddressBook(new Username("aa"));
        }
    }


}
