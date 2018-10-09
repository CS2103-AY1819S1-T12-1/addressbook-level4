package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.expense.Expense;

/**
 * An Immutable AddressBook that is serializable to XML format
 */
@XmlRootElement(name = "addressbook")
public class XmlSerializableAddressBook {

    public static final String MESSAGE_DUPLICATE_EXPENSE = "Expenses list contains duplicate expense(s).";

    @XmlElement
    private List<XmlAdaptedExpense> expenses;
    @XmlElement
    private XmlAdaptedUsername username;
    @XmlElement
    private XmlAdaptedBudget budget;

    /**
     * Creates an empty XmlSerializableAddressBook.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableAddressBook() {
        expenses = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableAddressBook(ReadOnlyAddressBook src) {
        this();
        this.username = new XmlAdaptedUsername(src.getUsername());
        expenses.addAll(src.getExpenseList().stream().map(XmlAdaptedExpense::new).collect(Collectors.toList()));
        this.budget = new XmlAdaptedBudget(src.getMaximumBudget());
    }

    /**
     * Converts this addressbook into the model's {@code AddressBook} object.
     *
     * @throws IllegalValueException if there were any data constraints violated or duplicates in the
     * {@code XmlAdaptedExpense}.
     */
    public AddressBook toModelType() throws IllegalValueException {
        AddressBook addressBook = new AddressBook(username.toModelType());
        for (XmlAdaptedExpense p : expenses) {
            Expense expense = p.toModelType();
            if (addressBook.hasExpense(expense)) {
                throw new IllegalValueException(MESSAGE_DUPLICATE_EXPENSE);
            }
            addressBook.addExpense(expense);
        }
        if (this.budget != null) {
            addressBook.modifyMaximumBudget(this.budget.toModelType());
        }
        return addressBook;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlSerializableAddressBook)) {
            return false;
        }
        return expenses.equals(((XmlSerializableAddressBook) other).expenses);
    }
}
