package seedu.expensetracker.model.encryption;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_COST_IPHONE;
import static seedu.expensetracker.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.expensetracker.model.encryption.EncryptionUtil.DEFAULT_ENCRYPTION_KEY;
import static seedu.expensetracker.model.encryption.EncryptionUtil.encryptExpense;
import static seedu.expensetracker.testutil.TypicalExpenses.SCHOOLFEE;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.expensetracker.commons.exceptions.IllegalValueException;
import seedu.expensetracker.testutil.ExpenseBuilder;

//@@author JasonChong96
public class EncryptedExpenseListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final EncryptedExpenseList expenseList = new EncryptedExpenseList();
    private final EncryptedExpense encryptedSchoolFee;

    public EncryptedExpenseListTest() throws IllegalValueException {
        encryptedSchoolFee = encryptExpense(SCHOOLFEE, DEFAULT_ENCRYPTION_KEY);
    }

    @Test
    public void contains_nullExpense_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        expenseList.contains(null);
    }

    @Test
    public void contains_expenseNotInList_returnsFalse() {
        assertFalse(expenseList.contains(encryptedSchoolFee));
    }

    @Test
    public void contains_expenseInList_returnsTrue() {
        expenseList.add(encryptedSchoolFee);
        assertTrue(expenseList.contains(encryptedSchoolFee));
    }

    @Test
    public void contains_expenseWithSameIdentityFieldsInList_returnsTrue() throws IllegalValueException {
        expenseList.add(encryptedSchoolFee);
        EncryptedExpense editedAlice =
                new ExpenseBuilder(SCHOOLFEE).withCost(VALID_COST_IPHONE).withTags(VALID_TAG_HUSBAND)
                .buildEncrypted();
        assertTrue(expenseList.contains(editedAlice));
    }

    @Test
    public void add_nullExpense_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        expenseList.add(null);
    }

}
