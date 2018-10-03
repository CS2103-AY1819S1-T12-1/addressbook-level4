package seedu.address.model.expense;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import static seedu.address.logic.commands.CommandTestUtil.VALID_CATEGORY_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_COST_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.TypicalExpenses.ALICE;
import static seedu.address.testutil.TypicalExpenses.BOB;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.testutil.ExpenseBuilder;

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
        assertTrue(new ExpenseBuilder(ALICE).build().hashCode() == new ExpenseBuilder(ALICE).build().hashCode());
        // different expense -> return false
        assertFalse(new ExpenseBuilder(BOB).build().hashCode() == new ExpenseBuilder(ALICE).build().hashCode());
    }

    @Test
    public void isSameExpense() {
        // same object -> returns true
        assertTrue(ALICE.isSameExpense(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSameExpense(null));

        // different category and cost -> returns false
        Expense editedAlice = new ExpenseBuilder(ALICE)
                .withCategory(VALID_CATEGORY_BOB).withCost(VALID_COST_BOB).build();
        assertFalse(ALICE.isSameExpense(editedAlice));

        // different name -> returns false
        editedAlice = new ExpenseBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.isSameExpense(editedAlice));

        // same name, same category, different attributes -> returns true
        editedAlice = new ExpenseBuilder(ALICE).withCost(VALID_COST_BOB)
                .withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSameExpense(editedAlice));

        // same name, same category, different attributes -> returns true
        editedAlice = new ExpenseBuilder(ALICE).withCost(VALID_COST_BOB).withTags(VALID_TAG_HUSBAND).build();
        assertTrue(ALICE.isSameExpense(editedAlice));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Expense aliceCopy = new ExpenseBuilder(ALICE).build();
        assertEquals(ALICE, aliceCopy);

        // same object -> returns true
        assertEquals(ALICE, ALICE);

        // null -> returns false
        assertNotEquals(null, ALICE);

        // different type -> returns false
        assertNotEquals(5, ALICE);

        // different expense -> returns false
        assertNotEquals(ALICE, BOB);

        // different name -> returns false
        Expense editedAlice = new ExpenseBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertNotEquals(ALICE, editedAlice);

        // different category -> returns false
        editedAlice = new ExpenseBuilder(ALICE).withCategory(VALID_CATEGORY_BOB).build();
        assertNotEquals(ALICE, editedAlice);

        // different address -> returns false
        editedAlice = new ExpenseBuilder(ALICE).withCost(VALID_COST_BOB).build();
        assertNotEquals(ALICE, editedAlice);

        // different tags -> returns false
        editedAlice = new ExpenseBuilder(ALICE).withTags(VALID_TAG_HUSBAND).build();
        assertNotEquals(ALICE, editedAlice);
    }
}
