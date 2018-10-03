package seedu.address.model.util;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.expense.*;
import seedu.address.model.expense.Expense;
import seedu.address.model.tag.Tag;
import seedu.address.model.user.Username;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Expense[] getSampleExpenses() {
        return new Expense[] {
            new Expense(new Name("Alex Yeoh"), new Category("School"),
                new Cost("1.00"),
                getTagSet("friends")),
            new Expense(new Name("Bernice Yu"), new Category("Food"),
                new Cost("2.00"),
                getTagSet("colleagues", "friends")),
            new Expense(new Name("Charlotte Oliveiro"), new Category("Entertainment"),
                new Cost("3.50"), new Date("01-10-2018"),
                getTagSet("neighbours")),
            new Expense(new Name("David Li"), new Category("Shopping"),
                new Cost("4.00"), new Date("01-10-2018"),
                getTagSet("family")),
            new Expense(new Name("Irfan Ibrahim"), new Category("Tax"),
                new Cost("9.00"), new Date("01-09-2018"),
                getTagSet("classmates")),
            new Expense(new Name("Roy Balakrishnan"), new Category("Book"),
                new Cost("10.00"), new Date("01-09-2018"),
                getTagSet("colleagues"))
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook(new Username("sample"));
        for (Expense sampleExpense : getSampleExpenses()) {
            sampleAb.addExpense(sampleExpense);
        }
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
