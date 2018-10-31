package seedu.address.model.encryption;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_COST_IPHONE;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.model.encryption.EncryptionUtil.DEFAULT_ENCRYPTION_KEY;
import static seedu.address.model.encryption.EncryptionUtil.encryptExpense;
import static seedu.address.testutil.TypicalExpenses.SCHOOLFEE;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.testutil.ExpenseBuilder;
import seedu.address.testutil.ModelUtil;

//@@author JasonChong96
public class EncryptedExpenseTrackerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final EncryptedExpenseTracker expenseTracker;
    private final EncryptedExpense encryptedSchoolFee;

    public EncryptedExpenseTrackerTest() throws IllegalValueException {
        expenseTracker = new EncryptedExpenseTracker(ModelUtil.TEST_USERNAME, null);
        encryptedSchoolFee = encryptExpense(SCHOOLFEE, DEFAULT_ENCRYPTION_KEY);
    }

    @Test
    public void constructor() {
        assertEquals(expenseTracker.getEncryptedExpenses().size(), 0);
    }

    @Test
    public void hasExpense_nullExpense_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        expenseTracker.hasExpense(null);
    }

    @Test
    public void hasExpense_expenseNotInExpenseTracker_returnsFalse() {
        assertFalse(expenseTracker.hasExpense(encryptedSchoolFee));
    }

    @Test
    public void hasExpense_expenseInExpenseTracker_returnsTrue() {
        expenseTracker.addExpense(encryptedSchoolFee);
        assertTrue(expenseTracker.hasExpense(encryptedSchoolFee));
    }

    @Test
    public void hasExpense_expenseWithSameIdentityFieldsInExpenseTracker_returnsTrue() throws IllegalValueException {
        expenseTracker.addExpense(encryptedSchoolFee);
        EncryptedExpense editedAlice =
                new ExpenseBuilder(SCHOOLFEE).withCost(VALID_COST_IPHONE).withTags(VALID_TAG_HUSBAND).buildEncrypted();
        assertTrue(expenseTracker.hasExpense(editedAlice));
    }
}
