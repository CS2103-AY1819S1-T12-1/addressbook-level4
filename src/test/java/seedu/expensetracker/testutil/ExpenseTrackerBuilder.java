package seedu.expensetracker.testutil;

import static seedu.expensetracker.model.encryption.EncryptionUtil.DEFAULT_ENCRYPTION_KEY;

import seedu.expensetracker.model.ExpenseTracker;
import seedu.expensetracker.model.expense.Expense;

/**
 * A utility class to help with building ExpenseTracker objects.
 * Example usage: <br>
 *     {@code ExpenseTracker ab = new ExpenseTrackerBuilder().withExpense("Have lunch", "Books").build();}
 */
public class ExpenseTrackerBuilder {

    private ExpenseTracker expenseTracker;

    public ExpenseTrackerBuilder() {
        expenseTracker = new ExpenseTracker(ModelUtil.TEST_USERNAME, null, DEFAULT_ENCRYPTION_KEY);
    }

    public ExpenseTrackerBuilder(ExpenseTracker expenseTracker) {
        this.expenseTracker = expenseTracker;
    }

    /**
     * Adds a new {@code Expense} to the {@code ExpenseTracker} that we are building.
     */
    public ExpenseTrackerBuilder withExpense(Expense expense) {
        expenseTracker.addExpense(expense);
        return this;
    }

    public ExpenseTracker build() {
        return expenseTracker;
    }
}
