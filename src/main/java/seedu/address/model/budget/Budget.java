package seedu.address.model.budget;
//@@author winsonhys

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import seedu.address.model.expense.Expense;


/**
 * Represents maximum budget of an expense tracker
 * Guarantees: details are present and not null, field values are validated, mutable.
 */
public class Budget {
    public static final String MESSAGE_BUDGET_CONSTRAINTS =
        "Cost should only take values in the following format: {int}.{digit}{digit}";

    public static final String BUDGET_VALIDATION_REGEX = "(\\d+).(\\d)(\\d)";

    private double budgetCap;
    private double currentExpenses;



    /**
     * Constructs a {@code Budget}
     *
     * @param budget a valid double
     */
    public Budget(String budget) {
        requireNonNull(budget);
        checkArgument(isValidBudget(budget), BUDGET_VALIDATION_REGEX);
        this.budgetCap = Double.parseDouble(budget);
        this.currentExpenses = 0.0;
    }

    /**
     * Constructs a {@code Budget} with modified current expenses
     * @param budget
     * @param currentExpenses
     */
    public Budget(double budget, double currentExpenses) {
        this.budgetCap = budget;
        this.currentExpenses = currentExpenses;

    }

    /**
     * Returns true if a given string is a valid budget.
     */
    public static boolean isValidBudget(String test) {
        return test.matches(BUDGET_VALIDATION_REGEX);
    }


    /**
     * Modifies the current (@code Budget) to have a new value
     *
     * @param budget a valid double
     */
    public void modifyBudget(double budget) {
        this.budgetCap = budget;
    }

    /**
     * Returns the most recent timestamp in which the budget is modified/created
     *
     * @return a Calendar object that consists of the most recent timestamp.
     */

    /**
     * Attemps to add expense
     * @param expense a valid expense of type double
     * @return true if expense is successfully added, false if adding expense will result in budget exceeding.
     */

    public boolean addExpense(double expense) {
        this.currentExpenses += expense;
        return this.currentExpenses <= this.budgetCap;
    }

    public void removeExpense(Expense expense) {
        this.currentExpenses -= expense.getCost().getCostValue();
    }

    /**
     * Resets the total expense to 0
     */
    public void clearSpending() {
        this.currentExpenses = 0;
    }

    /**
     * Alters the current total expense
     * @param target valid expense in spending to be removed
     * @param editedExpense new expense to be added
     */
    public void alterSpending(Expense target, Expense editedExpense) {
        this.currentExpenses -= target.getCost().getCostValue();
        this.currentExpenses += editedExpense.getCost().getCostValue();
    }

    public double getBudgetCap() {
        return this.budgetCap;
    }

    public double getCurrentExpenses() {
        return this.currentExpenses;
    }

    @Override
    public boolean equals(Object budget) {
        Budget anotherBudget = (Budget) budget;
        return this.currentExpenses == anotherBudget.currentExpenses
            && this.budgetCap == anotherBudget.budgetCap;
    }

    @Override
    public String toString() {
        return String.format("$%f", this.budgetCap);
    }
}
