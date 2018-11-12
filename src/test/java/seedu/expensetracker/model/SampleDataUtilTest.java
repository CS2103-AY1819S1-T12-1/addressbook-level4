package seedu.expensetracker.model;

import org.junit.Assert;
import org.junit.jupiter.api.Test;

import seedu.expensetracker.model.expense.Expense;
import seedu.expensetracker.model.util.SampleDataUtil;

public class SampleDataUtilTest {
    @Test
    public void testGetSampleExpenses() {
        Assert.assertEquals(SampleDataUtil.getSampleExpenses().length, 6);
    }

    @Test
    public void testGetSampleExpenseTracker() {
        ReadOnlyExpenseTracker book = SampleDataUtil.getSampleExpenseTracker();
        Expense[] sampleExpenses = SampleDataUtil.getSampleExpenses();
        for (Expense p : sampleExpenses) {
            Assert.assertTrue(book.getExpenseList().contains(p));
        }
        Assert.assertEquals(SampleDataUtil.getSampleExpenses().length, sampleExpenses.length);
    }
}
