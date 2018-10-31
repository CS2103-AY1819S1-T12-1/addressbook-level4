package seedu.address.model.budget;
//@@author winsonhys

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import seedu.address.testutil.Assert;


public class TotalBudgetTest {
    public static final String VALID_BUDGET = "2.00";
    public static final String INVALID_BUDGET = "A";
    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new TotalBudget(null));
    }

    @Test
    public void constructor_invalidAddress_throwsIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new TotalBudget(TotalBudgetTest.INVALID_BUDGET));
    }

    @Test
    public void isValidBudget() {
        // null address
        Assert.assertThrows(NullPointerException.class, () -> TotalBudget.isValidBudget(null));

        // invalid costs
        assertFalse(TotalBudget.isValidBudget("")); // empty string
        assertFalse(TotalBudget.isValidBudget(" ")); // spaces only
        assertFalse(TotalBudget.isValidBudget("200")); // number only

        // valid costs
        assertTrue(TotalBudget.isValidBudget("255.00"));
        assertTrue(TotalBudget.isValidBudget("1.00")); // one dollar
        assertTrue(TotalBudget.isValidBudget("231231232131231.00")); // high ses totalBudget
    }

    @Test
    public void equals() {
        assertTrue(new TotalBudget(0, 0).equals(new TotalBudget("0.00")));
        assertTrue(new TotalBudget(0, 0).equals(new TotalBudget(0, 0, null, 50000)));
    }
}
