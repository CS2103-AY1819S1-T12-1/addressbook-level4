package seedu.address.model.expense;

/**
 * Represents an Expense in the expense tracker.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Expense {
    private Date date;

    public Expense(String inputDate) {
        date = new Date(inputDate);
    }

}
