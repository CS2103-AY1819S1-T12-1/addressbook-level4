package seedu.address.model;

import javafx.collections.ObservableList;
import seedu.address.model.budget.Budget;
import seedu.address.model.expense.Expense;
import seedu.address.model.user.Username;

/**
 * Unmodifiable view of an address book
 */
public interface ReadOnlyAddressBook {

    /**
     * Returns an unmodifiable view of the expenses list.
     * This list will not contain any duplicate expenses.
     */
    ObservableList<Expense> getExpenseList();
    Budget getMaximumBudget();
    Username getUsername();
}
