package seedu.expensetracker.testutil;

import static seedu.expensetracker.model.encryption.EncryptionUtil.DEFAULT_ENCRYPTION_KEY;
import static seedu.expensetracker.model.encryption.EncryptionUtil.encryptExpense;

import java.util.HashSet;
import java.util.Set;

import seedu.expensetracker.commons.exceptions.IllegalValueException;
import seedu.expensetracker.model.encryption.EncryptedExpense;
import seedu.expensetracker.model.expense.Category;
import seedu.expensetracker.model.expense.Cost;
import seedu.expensetracker.model.expense.Date;
import seedu.expensetracker.model.expense.Expense;
import seedu.expensetracker.model.expense.Name;
import seedu.expensetracker.model.tag.Tag;
import seedu.expensetracker.model.util.SampleDataUtil;

/**
 * A utility class to help with building Expense objects.
 */
public class ExpenseBuilder {

    public static final String DEFAULT_NAME = "Alice Pauline";
    public static final String DEFAULT_CATEGORY = "Default";
    public static final String DEFAULT_COST = "321.00";

    private Name name;
    private Category category;
    private Cost cost;
    private Date date;
    private Set<Tag> tags;

    public ExpenseBuilder() {
        name = new Name(DEFAULT_NAME);
        category = new Category(DEFAULT_CATEGORY);
        cost = new Cost(DEFAULT_COST);
        date = new Date();
        tags = new HashSet<>();
    }

    /**
     * Initializes the ExpenseBuilder with the data of {@code expenseToCopy}.
     */
    public ExpenseBuilder(Expense expenseToCopy) {
        name = expenseToCopy.getName();
        category = expenseToCopy.getCategory();
        cost = expenseToCopy.getCost();
        date = expenseToCopy.getDate();
        tags = new HashSet<>(expenseToCopy.getTags());
    }

    /**
     * Sets the {@code Name} of the {@code Expense} that we are building.
     */
    public ExpenseBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Expense} that we are building.
     */
    public ExpenseBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
        return this;
    }

    /**
     * Sets the {@code Cost} of the {@code Expense} that we are building.
     */
    public ExpenseBuilder withCost(String cost) {
        this.cost = new Cost(cost);
        return this;
    }

    /**
     * Sets the {@code Category} of the {@code Expense} that we are building.
     */
    public ExpenseBuilder withCategory(String category) {
        this.category = new Category(category);
        return this;
    }

    /**
     * Sets the {@code Date} of the {@code Expense} that we are building.
     */
    public ExpenseBuilder withDate(String date) {
        this.date = new Date(date);
        return this;
    }

    public Expense build() {
        return new Expense(name, category, cost, date, tags);
    }

    public EncryptedExpense buildEncrypted() throws IllegalValueException {
        return encryptExpense(build(), DEFAULT_ENCRYPTION_KEY);
    }

}
