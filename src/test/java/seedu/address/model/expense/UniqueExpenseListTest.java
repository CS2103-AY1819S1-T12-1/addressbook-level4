package seedu.address.model.expense;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_COST_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.TypicalExpenses.ALICE;
import static seedu.address.testutil.TypicalExpenses.BOB;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.model.expense.exceptions.DuplicateExpenseException;
import seedu.address.model.expense.exceptions.ExpenseNotFoundException;
import seedu.address.testutil.ExpenseBuilder;

public class UniqueExpenseListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final UniqueExpenseList uniqueExpenseList = new UniqueExpenseList();

    @Test
    public void contains_nullExpense_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueExpenseList.contains(null);
    }

    @Test
    public void contains_expenseNotInList_returnsFalse() {
        assertFalse(uniqueExpenseList.contains(ALICE));
    }

    @Test
    public void contains_expenseInList_returnsTrue() {
        uniqueExpenseList.add(ALICE);
        assertTrue(uniqueExpenseList.contains(ALICE));
    }

    @Test
    public void contains_expenseWithSameIdentityFieldsInList_returnsTrue() {
        uniqueExpenseList.add(ALICE);
        Expense editedAlice = new ExpenseBuilder(ALICE).withCost(VALID_COST_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(uniqueExpenseList.contains(editedAlice));
    }

    @Test
    public void add_nullExpense_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueExpenseList.add(null);
    }

    @Test
    public void add_duplicateExpense_throwsDuplicateExpenseException() {
        uniqueExpenseList.add(ALICE);
        thrown.expect(DuplicateExpenseException.class);
        uniqueExpenseList.add(ALICE);
    }

    @Test
    public void setExpense_nullTargetExpense_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueExpenseList.setExpense(null, ALICE);
    }

    @Test
    public void setExpense_nullEditedExpense_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueExpenseList.setExpense(ALICE, null);
    }

    @Test
    public void setExpense_targetExpenseNotInList_throwsExpenseNotFoundException() {
        thrown.expect(ExpenseNotFoundException.class);
        uniqueExpenseList.setExpense(ALICE, ALICE);
    }

    @Test
    public void setExpense_editedExpenseIsSameExpense_success() {
        uniqueExpenseList.add(ALICE);
        uniqueExpenseList.setExpense(ALICE, ALICE);
        UniqueExpenseList expectedUniqueExpenseList = new UniqueExpenseList();
        expectedUniqueExpenseList.add(ALICE);
        assertEquals(expectedUniqueExpenseList, uniqueExpenseList);
    }

    @Test
    public void setExpense_editedExpenseHasSameIdentity_success() {
        uniqueExpenseList.add(ALICE);
        Expense editedAlice = new ExpenseBuilder(ALICE).withCost(VALID_COST_BOB).withTags(VALID_TAG_HUSBAND)
                .build();
        uniqueExpenseList.setExpense(ALICE, editedAlice);
        UniqueExpenseList expectedUniqueExpenseList = new UniqueExpenseList();
        expectedUniqueExpenseList.add(editedAlice);
        assertEquals(expectedUniqueExpenseList, uniqueExpenseList);
    }

    @Test
    public void setExpense_editedExpenseHasDifferentIdentity_success() {
        uniqueExpenseList.add(ALICE);
        uniqueExpenseList.setExpense(ALICE, BOB);
        UniqueExpenseList expectedUniqueExpenseList = new UniqueExpenseList();
        expectedUniqueExpenseList.add(BOB);
        assertEquals(expectedUniqueExpenseList, uniqueExpenseList);
    }

    @Test
    public void setExpense_editedExpenseHasNonUniqueIdentity_throwsDuplicateExpenseException() {
        uniqueExpenseList.add(ALICE);
        uniqueExpenseList.add(BOB);
        thrown.expect(DuplicateExpenseException.class);
        uniqueExpenseList.setExpense(ALICE, BOB);
    }

    @Test
    public void remove_nullExpense_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueExpenseList.remove(null);
    }

    @Test
    public void remove_expenseDoesNotExist_throwsExpenseNotFoundException() {
        thrown.expect(ExpenseNotFoundException.class);
        uniqueExpenseList.remove(ALICE);
    }

    @Test
    public void remove_existingExpense_removesExpense() {
        uniqueExpenseList.add(ALICE);
        uniqueExpenseList.remove(ALICE);
        UniqueExpenseList expectedUniqueExpenseList = new UniqueExpenseList();
        assertEquals(expectedUniqueExpenseList, uniqueExpenseList);
    }

    @Test
    public void setExpenses_nullUniqueExpenseList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueExpenseList.setExpenses((UniqueExpenseList) null);
    }

    @Test
    public void setExpenses_uniqueExpenseList_replacesOwnListWithProvidedUniqueExpenseList() {
        uniqueExpenseList.add(ALICE);
        UniqueExpenseList expectedUniqueExpenseList = new UniqueExpenseList();
        expectedUniqueExpenseList.add(BOB);
        uniqueExpenseList.setExpenses(expectedUniqueExpenseList);
        assertEquals(expectedUniqueExpenseList, uniqueExpenseList);
    }

    @Test
    public void setExpenses_nullList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueExpenseList.setExpenses((List<Expense>) null);
    }

    @Test
    public void setExpenses_list_replacesOwnListWithProvidedList() {
        uniqueExpenseList.add(ALICE);
        List<Expense> expenseList = Collections.singletonList(BOB);
        uniqueExpenseList.setExpenses(expenseList);
        UniqueExpenseList expectedUniqueExpenseList = new UniqueExpenseList();
        expectedUniqueExpenseList.add(BOB);
        assertEquals(expectedUniqueExpenseList, uniqueExpenseList);
    }

    @Test
    public void setExpenses_listWithDuplicateExpenses_throwsDuplicateExpenseException() {
        List<Expense> listWithDuplicateExpenses = Arrays.asList(ALICE, ALICE);
        thrown.expect(DuplicateExpenseException.class);
        uniqueExpenseList.setExpenses(listWithDuplicateExpenses);
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        uniqueExpenseList.asUnmodifiableObservableList().remove(0);
    }
}
