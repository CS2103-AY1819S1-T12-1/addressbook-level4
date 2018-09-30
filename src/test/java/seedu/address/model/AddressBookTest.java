package seedu.address.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

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
import seedu.address.model.expense.ExpenseTemp;
import seedu.address.model.expense.Name;
import seedu.address.model.expense.Person;
import seedu.address.model.expense.exceptions.DuplicatePersonException;
import seedu.address.model.user.Username;
import seedu.address.testutil.ModelUtil;
import seedu.address.testutil.PersonBuilder;

public class AddressBookTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final AddressBook addressBook = new AddressBook(ModelUtil.TEST_USERNAME);

    @Test
    public void constructor() {
        assertEquals(Collections.emptyList(), addressBook.getPersonList());
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
    public void resetData_withDuplicatePersons_throwsDuplicatePersonException() {
        // Two persons with the same identity fields
        Person editedAlice = new PersonBuilder(ALICE).withCost(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        List<Person> newPersons = Arrays.asList(ALICE, editedAlice);
        AddressBookStub newData = new AddressBookStub(newPersons);

        thrown.expect(DuplicatePersonException.class);
        addressBook.resetData(newData);
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        addressBook.hasPerson(null);
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        assertTrue(addressBook.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personWithSameIdentityFieldsInAddressBook_returnsTrue() {
        addressBook.addPerson(ALICE);
        Person editedAlice = new PersonBuilder(ALICE).withCost(VALID_ADDRESS_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(addressBook.hasPerson(editedAlice));
    }

    @Test
    public void getPersonList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        addressBook.getPersonList().remove(0);
    }

    /**
     * @return the string which consists of category and expense under the category.
     * The order is unexpected as the string is converted from HashMap.
     * */
    public String testAddExpense(ExpenseTemp e) {
        addressBook.addExpense(e);
        String result = addressBook.getCategoryList().toString();
        return result;
    }

    /**
     * @return the string of a expense list under a particular category.
     * */
    public String getTargetExpenseList(String name, String category) {
        ExpenseTemp e = new ExpenseTemp(new Name(name), new Category(category));
        addressBook.addExpense(e);
        return addressBook.getCategoryList().getCategory(category).getExpenseList().toString();
    }

    @Test
    public void addExpense_categoryNotExist() {
        ExpenseTemp e = new ExpenseTemp(new Name("firstExpense"), new Category("Test"));
        assertEquals("Test firstExpense ", testAddExpense(e));

        assertEquals("[secondExpense]", getTargetExpenseList("secondExpense", "secondTest"));
        assertEquals("[thirdExpense]", getTargetExpenseList("thirdExpense", "thirdTest"));
    }

    @Test
    public void addExpense_categoryExit() {
        assertEquals("[first]", getTargetExpenseList("first", "Test1"));
        assertEquals("[first, second]", getTargetExpenseList("second", "Test1"));
        assertEquals("[first, second, third]", getTargetExpenseList("third", "Test1"));
    }


    /**
     * A stub ReadOnlyAddressBook whose persons list can violate interface constraints.
     */
    private static class AddressBookStub implements ReadOnlyAddressBook {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();

        AddressBookStub(Collection<Person> persons) {
            this.persons.setAll(persons);
        }

        @Override
        public ObservableList<Person> getPersonList() {
            return persons;
        }

        @Override
        public Username getUsername() {
            return ModelUtil.TEST_USERNAME;
        }
    }



}
