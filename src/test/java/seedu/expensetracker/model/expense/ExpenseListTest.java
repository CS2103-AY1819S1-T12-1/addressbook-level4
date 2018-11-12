package seedu.expensetracker.model.expense;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_COST_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.expensetracker.testutil.TypicalExpenses.IPHONE;
import static seedu.expensetracker.testutil.TypicalExpenses.SCHOOLFEE;

import java.util.Collections;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.expensetracker.model.expense.exceptions.ExpenseNotFoundException;
import seedu.expensetracker.testutil.ExpenseBuilder;

public class ExpenseListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final ExpenseList expenseList = new ExpenseList();

    @Test
    public void contains_nullExpense_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        expenseList.contains(null);
    }

    @Test
    public void contains_expenseNotInList_returnsFalse() {
        assertFalse(expenseList.contains(SCHOOLFEE));
    }

    @Test
    public void contains_expenseInList_returnsTrue() {
        expenseList.add(SCHOOLFEE);
        assertTrue(expenseList.contains(SCHOOLFEE));
    }

    @Test
    public void contains_expenseWithSameIdentityFieldsInList_returnsTrue() {
        expenseList.add(SCHOOLFEE);
        Expense editedAlice = new ExpenseBuilder(SCHOOLFEE).withCost(VALID_COST_IPHONE).withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(expenseList.contains(editedAlice));
    }

    @Test
    public void add_nullExpense_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        expenseList.add(null);
    }

    @Test
    public void setExpense_nullTargetExpense_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        expenseList.setExpense(null, SCHOOLFEE);
    }

    @Test
    public void setExpense_nullEditedExpense_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        expenseList.setExpense(SCHOOLFEE, null);
    }

    @Test
    public void setExpense_targetExpenseNotInList_throwsExpenseNotFoundException() {
        thrown.expect(ExpenseNotFoundException.class);
        expenseList.setExpense(SCHOOLFEE, SCHOOLFEE);
    }

    @Test
    public void setExpense_editedExpenseIsSameExpense_success() {
        expenseList.add(SCHOOLFEE);
        expenseList.setExpense(SCHOOLFEE, SCHOOLFEE);
        ExpenseList expectedExpenseList = new ExpenseList();
        expectedExpenseList.add(SCHOOLFEE);
        assertEquals(expectedExpenseList, expenseList);
    }

    @Test
    public void setExpense_editedExpenseHasSameIdentity_success() {
        expenseList.add(SCHOOLFEE);
        Expense editedAlice = new ExpenseBuilder(SCHOOLFEE).withCost(VALID_COST_IPHONE).withTags(VALID_TAG_HUSBAND)
                .build();
        expenseList.setExpense(SCHOOLFEE, editedAlice);
        ExpenseList expectedExpenseList = new ExpenseList();
        expectedExpenseList.add(editedAlice);
        assertEquals(expectedExpenseList, expenseList);
    }

    @Test
    public void setExpense_editedExpenseHasDifferentIdentity_success() {
        expenseList.add(SCHOOLFEE);
        expenseList.setExpense(SCHOOLFEE, IPHONE);
        ExpenseList expectedExpenseList = new ExpenseList();
        expectedExpenseList.add(IPHONE);
        assertEquals(expectedExpenseList, expenseList);
    }

    @Test
    public void remove_nullExpense_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        expenseList.remove(null);
    }

    @Test
    public void remove_expenseDoesNotExist_throwsExpenseNotFoundException() {
        thrown.expect(ExpenseNotFoundException.class);
        expenseList.remove(SCHOOLFEE);
    }

    @Test
    public void remove_existingExpense_removesExpense() {
        expenseList.add(SCHOOLFEE);
        expenseList.remove(SCHOOLFEE);
        ExpenseList expectedExpenseList = new ExpenseList();
        assertEquals(expectedExpenseList, expenseList);
    }

    @Test
    public void setExpenses_nullExpenseList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        expenseList.setExpenses((ExpenseList) null);
    }

    @Test
    public void setExpenses_expenseList_replacesOwnListWithProvidedExpenseList() {
        expenseList.add(SCHOOLFEE);
        ExpenseList expectedExpenseList = new ExpenseList();
        expectedExpenseList.add(IPHONE);
        expenseList.setExpenses(expectedExpenseList);
        assertEquals(expectedExpenseList, expenseList);
    }

    @Test
    public void setExpenses_nullList_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        expenseList.setExpenses((List<Expense>) null);
    }

    @Test
    public void setExpenses_list_replacesOwnListWithProvidedList() {
        expenseList.add(SCHOOLFEE);
        List<Expense> expenseList = Collections.singletonList(IPHONE);
        this.expenseList.setExpenses(expenseList);
        ExpenseList expectedExpenseList = new ExpenseList();
        expectedExpenseList.add(IPHONE);
        assertEquals(expectedExpenseList, this.expenseList);
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        thrown.expect(UnsupportedOperationException.class);
        expenseList.asUnmodifiableObservableList().remove(0);
    }
}
