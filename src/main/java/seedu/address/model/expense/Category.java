package seedu.address.model.expense;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.util.ArrayList;

/**
 * Represents a Expense's category in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidCategory(String)}
 */
public class Category {


    public static final String MESSAGE_CATEGORY_CONSTRAINTS =
            "Category name should not be blank.";
    public static final String CATEGORY_VALIDATION_REGEX = "\\p{Alnum}+";
    private final String categoryName;
    private final ArrayList<Expense> expenseList = new ArrayList<>();

    /**
     * Constructs a {@code Category}.
     *
     * @param category A valid category number.
     */
    public Category(String category) {
        requireNonNull(category);
        checkArgument(isValidCategory(category), MESSAGE_CATEGORY_CONSTRAINTS);
        categoryName = category;
    }

    /**
     * Returns true if a given string is a valid category.
     */
    public static boolean isValidCategory(String test) {
        return test.matches(CATEGORY_VALIDATION_REGEX);
    }

    /**
     * Add an Expense into the {@code expenseList} in Category.
     * */
    public void addIntoCategory(Expense expense) {
        this.expenseList.add(expense);
    }

    public ArrayList<Expense> getExpenseList() {
        return this.expenseList;
    }

    public String getName() {
        return this.categoryName;
    }

    @Override
    public String toString() {
        return categoryName;
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
