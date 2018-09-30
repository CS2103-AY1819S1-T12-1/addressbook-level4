package seedu.address.model;

import java.util.HashMap;

import seedu.address.model.expense.Category;
import seedu.address.model.expense.ExpenseTemp;


//@@author Jiang Chen
/**
 * Manage all Category class created during addition of expense.
 * Duplicate is not allowed.
 * */
public class CategoryList {
    private final HashMap<String, Category> list;

    /**
     * Create a HashMap of {@code Category}.
     * The key is name of {@code Category} and the value is {@code Category} itself.
     * */
    public CategoryList() {
        this.list = new HashMap<>();
    }

    /**
     * Add a new {@code Category} into CategoryList.
     * */
    public void addCategory(Category categoryTemp) {
        this.list.put(categoryTemp.getName(), categoryTemp);
    }

    /**
     * add {@code ExpenseTemp} into an existed {@code Category}.
     * */
    public void addExpense(Category categoryTemp, ExpenseTemp expenseTemp) {
        Category currentCate = this.list.get(categoryTemp.getName());
        currentCate.addIntoCategory(expenseTemp);
    }

    public Category getCategory(String key) {
        return this.list.get(key);
    }

    /**
     * Returns true if a categoryTemp with the same identity as {@code category} exists in the address book.
     */
    public boolean hasCategory(Category category) {
        return this.list.containsKey(category.getName());
    }

    @Override
    public String toString() {
        String categoryString = "";
        for (String i : this.list.keySet()) {
            categoryString += i;
            categoryString += " ";
            for (ExpenseTemp e : this.list.get(i).getExpenseList()) {
                categoryString += e;
            }
            categoryString += " ";
        }
        return categoryString;
    }

    @Override
    public int hashCode() {
        return this.list.hashCode();
    }


}
