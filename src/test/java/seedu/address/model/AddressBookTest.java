package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_COST_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.TypicalExpenses.ALICE;
import static seedu.address.testutil.TypicalExpenses.getTypicalAddressBook;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.model.expense.Category;
import seedu.address.model.expense.Expense;
import seedu.address.model.expense.Name;
import seedu.address.model.expense.exceptions.DuplicateExpenseException;
import seedu.address.model.user.Username;
import seedu.address.testutil.ExpenseBuilder;
import seedu.address.testutil.ModelUtil;

public class AddressBookTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final AddressBook addressBook = new AddressBook(ModelUtil.TEST_USERNAME);

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getExpenseList());
    }

    @Test
    public void resetData_null_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        addressBook.resetData(null);
    }

    @Test
    public void resetData_withValidReadOnlyAddressBook_replacesData() {
        AddressBook newData = getTypicalAddressBook();
        addressBook.resetData(newData);
        assertEquals(newData, addressBook);
    }

    @Test
    public void resetData_withDuplicateExpenses_throwsDuplicateExpenseException() {
        // Two expenses with the same identity fields
        Expense editedAlice = new ExpenseBuilder(ALICE).withCost(VALID_COST_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Expense> newExpenses = Arrays.asList(ALICE, editedAlice);
        AddressBookStub newData = new AddressBookStub(newExpenses);

        thrown.expect(DuplicateExpenseException.class);
        addressBook.resetData(newData);
    }

    @Test
    public void hasExpense_nullExpense_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        addressBook.hasExpense(null);
    }

    @Test
    public void hasExpense_expenseNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasExpense(ALICE));
    }

    @Test
    public void haExpense_expenseInAddressBook_returnsTrue() {
        addressBook.addExpense(ALICE);
        assertTrue(addressBook.hasExpense(ALICE));
    }

    @Test
    public void hasExpense_expenseWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addExpense(ALICE);
        Expense editedAlice = new ExpenseBuilder(ALICE).withCost(VALID_COST_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(addressBook.hasExpense(editedAlice));
    }

    @Test
    public void getExpenseList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        addressBook.getExpenseList().remove(0);
    }

    /**
     * A stub ReadOnlyAddressBook whose expenses list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Expense> expenses = FXCollections.observableArrayList();

        AddressBookStub(Collection<Expense> expenses) {
            this.expenses.setAll(expenses);
        }

        @Override
        public ObservableList<Expense> getExpenseList() {
            return expenses;
        }

        @Override
        public Username getUsername() {
            return ModelUtil.TEST_USERNAME;
        }
    }



}
