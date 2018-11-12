package seedu.expensetracker.model.expense;

import static java.util.Objects.requireNonNull;

/**
 * Represents a field of an Expense.
 * Guarantees: immutable;
 */
public abstract class ExpenseField {
    private String fieldString;

    public ExpenseField(String fieldString) {
        requireNonNull(fieldString);
        this.fieldString = fieldString;
    }

    @Override
    public String toString() {
        return fieldString;
    }
}
