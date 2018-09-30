package seedu.address.model.expense;

/**
 * Temporary class for testing purpose TODO: Remove it after morphing Person to Expense
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class ExpenseTemp {
    private Name name;
    private Category categoryTemp;

    /**
     * Construct a temporary {@code ExpenseTemp} for testing purpose.
     * @param - Name cannot be null
     * @param - categoryTemp can be null
     * */
    public ExpenseTemp(Name name, Category categoryTemp) {
        this.name = name;
        this.categoryTemp = categoryTemp;
    }

    public Category getCategoryTemp() {
        return this.categoryTemp;
    }

    @Override
    public String toString() {
        return this.name.expenseName;
    }

}
