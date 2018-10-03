package seedu.address.model;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_EXPENSES;
import static seedu.address.testutil.TypicalExpenses.ALICE;
import static seedu.address.testutil.TypicalExpenses.BENSON;

import java.nio.file.Paths;
import java.util.Arrays;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.model.exceptions.NonExistentUserException;
import seedu.address.model.exceptions.UserAlreadyExistsException;
import seedu.address.model.expense.NameContainsKeywordsPredicate;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.ModelUtil;

public class ModelManagerTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private ModelManager modelManager = (ModelManager) ModelUtil.modelWithTestUser();
    private ModelManager modelManagerLoggedOut = new ModelManager();

    public ModelManagerTest() throws UserAlreadyExistsException, NonExistentUserException {
    }

    @Test
    public void hasExpense_nullExpense_throwsNullPointerException() throws NoUserSelectedException {
        thrown.expect(NullPointerException.class);
        modelManager.hasExpense(null);
    }

    @Test
    public void hasExpense_expenseNotInAddressBook_returnsFalse() throws NoUserSelectedException {
        assertFalse(modelManager.hasExpense(ALICE));
    }

    @Test
    public void hasExpense_expenseInAddressBook_returnsTrue() throws NoUserSelectedException {
        modelManager.addExpense(ALICE);
        assertTrue(modelManager.hasExpense(ALICE));
    }

    @Test
    public void getFilteredExpenseList_modifyList_throwsUnsupportedOperationException() throws NoUserSelectedException {
        thrown.expect(UnsupportedOperationException.class);
        modelManager.getFilteredExpenseList().remove(0);
    }

    @Test
    public void getAddressBook_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.getAddressBook();
    }

    @Test
    public void indicateAddressBookChanged_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.indicateAddressBookChanged();
    }

    @Test
    public void hasExpense_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.hasExpense(ALICE);
    }

    @Test
    public void getFilteredExpenseList_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.getFilteredExpenseList();
    }

    @Test
    public void updateFilteredExpenseList_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.updateFilteredExpenseList(unused -> true);
    }

    @Test
    public void canUndoAddressBook_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.canUndoAddressBook();
    }

    @Test
    public void commitAddressBook_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.commitAddressBook();
    }

    @Test
    public void getExpenseStats_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.getExpenseStats();
    }

    @Test
    public void updateExpenseStats_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.updateExpenseStats(unused -> true);
    }

    @Test
    public void indicateUserLoggedIn_noUserSelected_throwsNoUserSelectedException() throws Exception {
        thrown.expect(NoUserSelectedException.class);
        modelManagerLoggedOut.indicateUserLoggedIn();
    }

    @Test
    public void equals() throws NoUserSelectedException {
        AddressBook addressBook = new AddressBookBuilder().withExpense(ALICE).withExpense(BENSON).build();
        AddressBook differentAddressBook = new AddressBook(ModelUtil.TEST_USERNAME);
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertFalse(modelManager.equals(null));

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().expenseName.split("\\s+");
        modelManager.updateFilteredExpenseList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredExpenseList(PREDICATE_SHOW_ALL_EXPENSES);

        // different userPrefs -> returns true
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookDirPath(Paths.get("differentFilePath"));
        assertTrue(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }
}
