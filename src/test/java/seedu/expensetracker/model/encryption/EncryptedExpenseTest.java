package seedu.expensetracker.model.encryption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_CATEGORY_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_COST_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_NAME_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.expensetracker.model.encryption.EncryptionUtil.DEFAULT_ENCRYPTION_KEY;
import static seedu.expensetracker.model.encryption.EncryptionUtil.encryptExpense;
import static seedu.expensetracker.testutil.TypicalExpenses.IPHONE;
import static seedu.expensetracker.testutil.TypicalExpenses.SCHOOLFEE;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.expensetracker.commons.exceptions.IllegalValueException;
import seedu.expensetracker.testutil.ExpenseBuilder;

//@@author JasonChong96
public class EncryptedExpenseTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final EncryptedExpense encryptedSchoolFee;
    private final EncryptedExpense encryptedIphone;

    public EncryptedExpenseTest() throws IllegalValueException {
        encryptedSchoolFee = encryptExpense(SCHOOLFEE, DEFAULT_ENCRYPTION_KEY);
        encryptedIphone = encryptExpense(IPHONE, DEFAULT_ENCRYPTION_KEY);
    }

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() throws IllegalValueException {
        EncryptedExpense expense = new ExpenseBuilder().buildEncrypted();
        thrown.expect(UnsupportedOperationException.class);
        expense.getTags().remove(0);
    }

    @Test
    public void expenseHashCode() throws IllegalValueException {
        // same expense -> returns true
        assertEquals(new ExpenseBuilder(SCHOOLFEE).buildEncrypted().hashCode(),
                new ExpenseBuilder(SCHOOLFEE).buildEncrypted().hashCode());
        // different expenses are not tested as hash function is not guaranteed distinct hashes for different expenses
    }

    @Test
    public void isSameExpense() throws IllegalValueException {
        // same object -> returns true
        assertTrue(encryptedSchoolFee.isSameExpense(encryptedSchoolFee));

        // null -> returns false
        assertFalse(encryptedSchoolFee.isSameExpense(null));

        // different category and cost -> returns false
        EncryptedExpense editedAlice = new ExpenseBuilder(SCHOOLFEE)
                .withCategory(VALID_CATEGORY_IPHONE)
                .withCost(VALID_COST_IPHONE)
                .buildEncrypted();
        assertFalse(encryptedSchoolFee.isSameExpense(editedAlice));

        // different name -> returns false
        editedAlice = new ExpenseBuilder(SCHOOLFEE)
                .withName(VALID_NAME_IPHONE)
                .buildEncrypted();
        assertFalse(encryptedSchoolFee.isSameExpense(editedAlice));

        // same name, same category, different attributes -> returns true
        editedAlice = new ExpenseBuilder(SCHOOLFEE)
                .withCost(VALID_COST_IPHONE)
                .withTags(VALID_TAG_HUSBAND)
                .buildEncrypted();
        assertTrue(encryptedSchoolFee.isSameExpense(editedAlice));

        // same name, same category, different attributes -> returns true
        editedAlice = new ExpenseBuilder(SCHOOLFEE)
                .withCost(VALID_COST_IPHONE)
                .withTags(VALID_TAG_HUSBAND)
                .buildEncrypted();
        assertTrue(encryptedSchoolFee.isSameExpense(editedAlice));
    }

    @Test
    public void equals() throws IllegalValueException {
        // same values -> returns true
        EncryptedExpense aliceCopy = new ExpenseBuilder(SCHOOLFEE).buildEncrypted();
        assertEquals(encryptedSchoolFee, aliceCopy);

        // same object -> returns true
        assertEquals(encryptedSchoolFee, encryptedSchoolFee);

        // null -> returns false
        assertNotEquals(null, encryptedSchoolFee);

        // different type -> returns false
        assertNotEquals(5, encryptedSchoolFee);

        // different expense -> returns false
        assertNotEquals(encryptedSchoolFee, encryptedIphone);

        // different name -> returns false
        EncryptedExpense editedAlice = new ExpenseBuilder(SCHOOLFEE)
                .withName(VALID_NAME_IPHONE)
                .buildEncrypted();
        assertNotEquals(encryptedSchoolFee, editedAlice);

        // different category -> returns false
        editedAlice = new ExpenseBuilder(SCHOOLFEE)
                .withCategory(VALID_CATEGORY_IPHONE)
                .buildEncrypted();
        assertNotEquals(encryptedSchoolFee, editedAlice);

        // different expensetracker -> returns false
        editedAlice = new ExpenseBuilder(SCHOOLFEE)
                .withCost(VALID_COST_IPHONE)
                .buildEncrypted();
        assertNotEquals(encryptedSchoolFee, editedAlice);

        // different tags -> returns false
        editedAlice = new ExpenseBuilder(SCHOOLFEE)
                .withTags(VALID_TAG_HUSBAND)
                .buildEncrypted();
        assertNotEquals(encryptedSchoolFee, editedAlice);
    }
}
