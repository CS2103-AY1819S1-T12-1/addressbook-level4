package seedu.address.model.expense;

import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Expense's category in the expense tracker.
 * Guarantees: immutable; is valid as declared in {@link #isValidCategory(String)}
 */
public class Category extends ExpenseField {


    public static final String MESSAGE_CATEGORY_CONSTRAINTS =
            "Category name should not be blank. It should be alphanumeric.";
    public static final String CATEGORY_VALIDATION_REGEX = "[a-zA-Z0-9][a-zA-Z0-9 ]+";
    public static final String[] INITIAL_CATEGORIES = {"Food", "Transport", "Stationary", "Printing"};
    public final String categoryName;



    /**
     * Constructs a {@code Category}.
     *
     * @param category A valid category number.
     */
    public Category(String category) {
        super(category);
        checkArgument(isValidCategory(category), MESSAGE_CATEGORY_CONSTRAINTS);
        categoryName = category;
    }

    /**
     * Returns true if a given string is a valid category.
     */
    public static boolean isValidCategory(String test) {
        return test.matches(CATEGORY_VALIDATION_REGEX);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Category // instanceof handles nulls
                && categoryName.equals(((Category) other).categoryName)); // state check
    }

    @Override
    public int hashCode() {
        return categoryName.hashCode();
    }

}
