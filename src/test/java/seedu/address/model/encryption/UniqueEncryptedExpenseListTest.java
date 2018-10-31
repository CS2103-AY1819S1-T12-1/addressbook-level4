package seedu.address.model.encryption;

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
import seedu.address.model.expense.exceptions.DuplicateExpenseException;
import seedu.address.testutil.ExpenseBuilder;

//@@author JasonChong96
public class UniqueEncryptedExpenseListTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private final UniqueEncryptedExpenseList uniqueExpenseList = new UniqueEncryptedExpenseList();
    private final EncryptedExpense encryptedSchoolFee;

    public UniqueEncryptedExpenseListTest() throws IllegalValueException {
        encryptedSchoolFee = encryptExpense(SCHOOLFEE, DEFAULT_ENCRYPTION_KEY);
    }

    @Test
    public void contains_nullExpense_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueExpenseList.contains(null);
    }

    @Test
    public void contains_expenseNotInList_returnsFalse() {
        assertFalse(uniqueExpenseList.contains(encryptedSchoolFee));
    }

    @Test
    public void contains_expenseInList_returnsTrue() {
        uniqueExpenseList.add(encryptedSchoolFee);
        assertTrue(uniqueExpenseList.contains(encryptedSchoolFee));
    }

    @Test
    public void contains_expenseWithSameIdentityFieldsInList_returnsTrue() throws IllegalValueException {
        uniqueExpenseList.add(encryptedSchoolFee);
        EncryptedExpense editedAlice =
                new ExpenseBuilder(SCHOOLFEE).withCost(VALID_COST_IPHONE).withTags(VALID_TAG_HUSBAND)
                .buildEncrypted();
        assertTrue(uniqueExpenseList.contains(editedAlice));
    }

    @Test
    public void add_nullExpense_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        uniqueExpenseList.add(null);
    }

    @Test
    public void add_duplicateExpense_throwsDuplicateExpenseException() {
        uniqueExpenseList.add(encryptedSchoolFee);
        thrown.expect(DuplicateExpenseException.class);
        uniqueExpenseList.add(encryptedSchoolFee);
    }
}
