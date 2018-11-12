package seedu.expensetracker.model.expense;

import static seedu.expensetracker.commons.util.AppUtil.checkArgument;


/**
 * Represents a Expense's cost in the expense tracker.
 * Guarantees: immutable; is valid as declared in {@link #isValidCost(String)}
 */
public class Cost extends ExpenseField {

    public static final String MESSAGE_COST_CONSTRAINTS =
            "Cost should only take values in the following format: {digit}.{digit}{digit}";

    /*
     * The first character of the expensetracker must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String COST_VALIDATION_REGEX = "(\\d+)\\.(\\d)(\\d)";

    public final String value;

    /**
     * Constructs an {@code Cost}.
     * @param cost A valid cost.
     */
    public Cost(String cost) {
        super(cost);
        checkArgument(isValidCost(cost), MESSAGE_COST_CONSTRAINTS);
        value = cost;
    }

    /**
     * Returns true if a given string is a valid cost.
     */
    public static boolean isValidCost(String test) {
        return test.matches(COST_VALIDATION_REGEX);
    }

    public double getCostValue() {
        return Double.parseDouble(this.value);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Cost // instanceof handles nulls
                && value.equals(((Cost) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
