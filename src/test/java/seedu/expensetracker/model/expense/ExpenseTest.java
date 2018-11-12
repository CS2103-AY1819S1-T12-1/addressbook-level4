package seedu.expensetracker.model.expense;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_CATEGORY_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_COST_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_NAME_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.expensetracker.testutil.TypicalExpenses.IPHONE;
import static seedu.expensetracker.testutil.TypicalExpenses.SCHOOLFEE;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.expensetracker.testutil.ExpenseBuilder;

public class ExpenseTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Expense expense = new ExpenseBuilder().build();
        thrown.expect(UnsupportedOperationException.class);
        expense.getTags().remove(0);
    }

    @Test
    public void expenseHashCode() {
        // same expense -> returns true
        assertTrue(
                new ExpenseBuilder(SCHOOLFEE).build().hashCode() == new ExpenseBuilder(SCHOOLFEE).build().hashCode());
        // different expense -> return false
        assertFalse(new ExpenseBuilder(IPHONE).build().hashCode() == new ExpenseBuilder(SCHOOLFEE).build().hashCode());
    }

    @Test
    public void isSameExpense() {
        // same object -> returns true
        assertTrue(SCHOOLFEE.isSameExpense(SCHOOLFEE));

        // null -> returns false
        assertFalse(SCHOOLFEE.isSameExpense(null));

        // different category and cost -> returns false
        Expense editedAlice = new ExpenseBuilder(SCHOOLFEE)
                .withCategory(VALID_CATEGORY_IPHONE).withCost(VALID_COST_IPHONE).build();
        assertFalse(SCHOOLFEE.isSameExpense(editedAlice));

        // different name -> returns false
        editedAlice = new ExpenseBuilder(SCHOOLFEE).withName(VALID_NAME_IPHONE).build();
        assertFalse(SCHOOLFEE.isSameExpense(editedAlice));

        // same name, same category, different attributes -> returns true
        editedAlice = new ExpenseBuilder(SCHOOLFEE).withCost(VALID_COST_IPHONE)
                .withTags(VALID_TAG_HUSBAND).build();
        assertTrue(SCHOOLFEE.isSameExpense(editedAlice));

        // same name, same category, different attributes -> returns true
        editedAlice = new ExpenseBuilder(SCHOOLFEE).withCost(VALID_COST_IPHONE).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(SCHOOLFEE.isSameExpense(editedAlice));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Expense aliceCopy = new ExpenseBuilder(SCHOOLFEE).build();
        assertEquals(SCHOOLFEE, aliceCopy);

        // same object -> returns true
        assertEquals(SCHOOLFEE, SCHOOLFEE);

        // null -> returns false
        assertNotEquals(null, SCHOOLFEE);

        // different type -> returns false
        assertNotEquals(5, SCHOOLFEE);

        // different expense -> returns false
        assertNotEquals(SCHOOLFEE, IPHONE);

        // different name -> returns false
        Expense editedAlice = new ExpenseBuilder(SCHOOLFEE).withName(VALID_NAME_IPHONE).build();
        assertNotEquals(SCHOOLFEE, editedAlice);

        // different category -> returns false
        editedAlice = new ExpenseBuilder(SCHOOLFEE).withCategory(VALID_CATEGORY_IPHONE).build();
        assertNotEquals(SCHOOLFEE, editedAlice);

        // different expensetracker -> returns false
        editedAlice = new ExpenseBuilder(SCHOOLFEE).withCost(VALID_COST_IPHONE).build();
        assertNotEquals(SCHOOLFEE, editedAlice);

        // different tags -> returns false
        editedAlice = new ExpenseBuilder(SCHOOLFEE).withTags(VALID_TAG_HUSBAND).build();
        assertNotEquals(SCHOOLFEE, editedAlice);
    }
}
