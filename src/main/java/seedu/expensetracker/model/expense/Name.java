package seedu.expensetracker.model.expense;

import static seedu.expensetracker.commons.util.AppUtil.checkArgument;

/**
 * Represents a Expense's name in the expense tracker.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name extends ExpenseField {

    public static final String MESSAGE_NAME_CONSTRAINTS =
            "Names should not be blank. It should be alphanumeric.";

    /*
     * The first character of the expensetracker must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String NAME_VALIDATION_REGEX = "[a-zA-Z0-9][a-zA-Z0-9 ]*";

    public final String expenseName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        super(name);
        checkArgument(isValidName(name), MESSAGE_NAME_CONSTRAINTS);
        expenseName = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(NAME_VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Name // instanceof handles nulls
                && expenseName.equals(((Name) other).expenseName)); // state check
    }

    @Override
    public int hashCode() {
        return expenseName.hashCode();
    }

}
