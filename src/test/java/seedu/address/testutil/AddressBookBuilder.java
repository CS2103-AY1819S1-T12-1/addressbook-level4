package seedu.address.testutil;

import seedu.address.model.AddressBook;
import seedu.address.model.expense.Expense;

/**
 * A utility class to help with building Addressbook objects.
 * Example usage: <br>
 *     {@code AddressBook ab = new AddressBookBuilder().withExpense("John", "Doe").build();}
 */
public class AddressBookBuilder {

    private AddressBook addressBook;

    public AddressBookBuilder() {
        addressBook = new AddressBook(ModelUtil.TEST_USERNAME);
    }

    public AddressBookBuilder(AddressBook addressBook) {
        this.addressBook = addressBook;
    }

    /**
     * Adds a new {@code Expense} to the {@code AddressBook} that we are building.
     */
    public AddressBookBuilder withExpense(Expense expense) {
        addressBook.addExpense(expense);
        return this;
    }

    public AddressBook build() {
        return addressBook;
    }
}
