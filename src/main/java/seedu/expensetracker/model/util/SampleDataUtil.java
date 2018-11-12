package seedu.expensetracker.model.util;

import static seedu.expensetracker.model.encryption.EncryptionUtil.DEFAULT_ENCRYPTION_KEY;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.expensetracker.model.ExpenseTracker;
import seedu.expensetracker.model.ReadOnlyExpenseTracker;
import seedu.expensetracker.model.expense.Category;
import seedu.expensetracker.model.expense.Cost;
import seedu.expensetracker.model.expense.Date;
import seedu.expensetracker.model.expense.Expense;
import seedu.expensetracker.model.expense.Name;
import seedu.expensetracker.model.tag.Tag;
import seedu.expensetracker.model.user.Username;

/**
 * Contains utility methods for populating {@code ExpenseTracker} with sample data.
 */
public class SampleDataUtil {
    public static Expense[] getSampleExpenses() {
        return new Expense[] {
            new Expense(new Name("School Fee"), new Category("School"),
                new Cost("1.00"),
                getTagSet("friends")),
            new Expense(new Name("Have lunch"), new Category("Food"),
                new Cost("2.00"),
                getTagSet("colleagues", "friends")),
            new Expense(new Name("Toy"), new Category("Entertainment"),
                new Cost("3.50"), new Date("01-09-2018"),
                getTagSet("neighbours")),
            new Expense(new Name("New clothes"), new Category("Shopping"),
                new Cost("4.00"), new Date("01-08-2018"),
                getTagSet("family")),
            new Expense(new Name("Pay tax"), new Category("Tax"),
                new Cost("9.00"), new Date("5-08-2018"),
                getTagSet("classmates")),
            new Expense(new Name("Buy books"), new Category("Book"),
                new Cost("10.00"), new Date("4-07-2018"),
                getTagSet("colleagues"))
        };
    }

    public static ReadOnlyExpenseTracker getSampleExpenseTracker() {
        ExpenseTracker sampleAb = new ExpenseTracker(new Username("sample"), null, DEFAULT_ENCRYPTION_KEY);
        for (Expense sampleExpense : getSampleExpenses()) {
            sampleAb.addExpense(sampleExpense);
        }
        sampleAb.modifyNotificationHandler(LocalDateTime.parse("2018-11-01T17:20:16.847790"),
                true, true);
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
