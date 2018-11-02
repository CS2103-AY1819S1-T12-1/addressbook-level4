package seedu.address.model.budget;
//@@author winsonhys

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import static seedu.address.model.budget.TotalBudgetTest.INVALID_BUDGET;
import static seedu.address.model.budget.TotalBudgetTest.VALID_BUDGET;
import static seedu.address.model.expense.CategoryTest.INVALID_CATEGORY;
import static seedu.address.model.expense.CategoryTest.VALID_CATEGORY;

import org.junit.Test;

import seedu.address.testutil.Assert;

public class CategoryBudgetTest {
    @Test
    public void constructor_validCategoryInvalidBudget_illegalArgumentExceptionThrown() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new CategoryBudget(VALID_CATEGORY, INVALID_BUDGET));
    }

    @Test
    public void constructor_invalidCategoryValidBudget_illegalArgumentExceptionThrown() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new CategoryBudget(INVALID_CATEGORY,
            VALID_BUDGET));
    }

    @Test
    public void equals() {
        //Most of the tests are covered by those in category and those in totalBudget
        assertTrue(new CategoryBudget(VALID_CATEGORY, VALID_BUDGET).equals(new CategoryBudget(VALID_CATEGORY,
            VALID_BUDGET)));
        assertFalse(new CategoryBudget(VALID_CATEGORY, VALID_BUDGET).equals(new CategoryBudget("hohohaha",
            VALID_BUDGET)));
        assertTrue(new CategoryBudget(VALID_CATEGORY, VALID_BUDGET).equals(new CategoryBudget(VALID_CATEGORY,
            "222.00")));
    }

}
