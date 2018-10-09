package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.List;

import javafx.collections.ObservableList;

import seedu.address.model.budget.Budget;
import seedu.address.model.expense.Expense;
import seedu.address.model.expense.UniqueExpenseList;
import seedu.address.model.user.Username;

/**
 * Wraps all data at the address-book level
 * Duplicates are not allowed (by .isSameExpense comparison)
 */
public class AddressBook implements ReadOnlyAddressBook {

    protected Username username;
    private final UniqueExpenseList expenses;
    private Budget maximumBudget;

    /**
     * Creates an empty AddressBook with the given username.
     * @param username the username of the AddressBook
     */
    public AddressBook(Username username) {
        this.username = username;
        this.expenses = new UniqueExpenseList();
        this.maximumBudget = new Budget("28.00");
    }

    /**
     * Creates an AddressBook using the Expenses in the {@code toBeCopied}
     */
    public AddressBook(ReadOnlyAddressBook toBeCopied) {
        this(toBeCopied.getUsername());
        resetData(toBeCopied);
    }
    //// budget operations

    /**
     * Modifies the maximum budget for the current expense tracker
     * @param budget a valid double
     */
    public void modifyMaximumBudget(double budget) {
        this.maximumBudget.modifyBudget(budget);
    }

    /**
     * Modifies the maximum budget for the current expense tracker
     * @param budget a valid Budget
     */
    public void modifyMaximumBudget(Budget budget) {
        this.maximumBudget = budget;
    }
    //// list overwrite operaticons

    /**
     * Replaces the contents of the expense list with {@code expenses}.
     * {@code expenses} must not contain duplicate expenses.
     */
    public void setExpenses(List<Expense> expenses) {
        this.expenses.setExpenses(expenses);
        expenses.forEach(expense -> this.maximumBudget.addExpense(expense.getCost().getCostValue()));
    }

    /**
     * Resets the existing data of this {@code AddressBook} with {@code newData}.
     */
    public void resetData(ReadOnlyAddressBook newData) {
        requireNonNull(newData);

        this.setExpenses(newData.getExpenseList());
        this.maximumBudget = newData.getMaximumBudget();
    }

    //// expense-level operations

    /**
     * Returns true if a expense with the same identity as {@code expense} exists in the address book.
     */
    public boolean hasExpense(Expense expense) {
        requireNonNull(expense);
        return this.expenses.contains(expense);
    }

    /**
     * Adds a expense into the address book
     * @return true if expense is successfully added withouot exceeding budget, else false
     */
    public boolean addExpense(Expense p) {
        this.expenses.add(p);
        return this.maximumBudget.addExpense(p.getCost().getCostValue());
    }

    /**
     * Replaces the given expense {@code target} in the list with {@code editedExpense}.
     * {@code target} must exist in the address book.
     * The expense identity of {@code editedExpense}
     * must not be the same as another existing expense in the address book.
     */
    public void updateExpense(Expense target, Expense editedExpense) {
        requireNonNull(editedExpense);

        this.expenses.setExpense(target, editedExpense);
        this.maximumBudget.alterSpending(target, editedExpense);

    }

    /**
     * Removes {@code key} from this {@code AddressBook}.
     * {@code key} must exist in the address book.
     */
    public void removeExpense(Expense key) {
        expenses.remove(key);
        this.maximumBudget.removeExpense(key);
    }

    public Budget getMaximumBudget() {
        return new Budget(this.maximumBudget.getBudgetCap(), this.maximumBudget.getCurrentExpenses());
    }

    public Username getUsername() {
        return username;
    }

    public void setUsername(Username newUsername) {
        this.username = newUsername;
    }
    //// util methods

    @Override
    public String toString() {
        return expenses.asUnmodifiableObservableList().size() + " expenses";
        // TODO: refine later
    }

    @Override
    public ObservableList<Expense> getExpenseList() {
        return expenses.asUnmodifiableObservableList();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddressBook // instanceof handles nulls
                && expenses.equals(((AddressBook) other).expenses))
                && this.maximumBudget.equals(((AddressBook) other).maximumBudget);
    }

    @Override
    public int hashCode() {
        return expenses.hashCode();
    }
}
