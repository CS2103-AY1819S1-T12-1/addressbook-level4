package seedu.expensetracker.model.expense;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.time.LocalDateTime;

import org.junit.Test;

import seedu.expensetracker.testutil.Assert;

//@@author jonathantjm
public class DateTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Date(null));
    }

    @Test
    public void constructor_invalidDate_throwsIllegalArgumentException() {
        String invalidDate = "";
        Assert.assertThrows(IllegalArgumentException.class, () -> new Date(invalidDate));
    }

    @Test
    public void constructor_validDate() {
        Date test = new Date();
        test.setFullDate(2018, 2, 15);
        Date testDate = new Date("15-02-2018");
        assertTrue(testDate.equals(test));

        LocalDateTime now = LocalDateTime.now();
        testDate = new Date();
        assertTrue(now.getDayOfMonth() == testDate.getFullDate().getDayOfMonth());
        assertTrue(now.getMonthValue() == testDate.getFullDate().getMonthValue());
        assertTrue(now.getYear() == testDate.getFullDate().getYear());

    }

    @Test
    public void isValidDate() {
        // null date
        Assert.assertThrows(NullPointerException.class, () -> Date.isValidDate(null));

        // blank date
        assertFalse(Date.isValidDate("")); // empty string
        assertFalse(Date.isValidDate(" ")); // spaces only

        // missing parts
        assertFalse(Date.isValidDate("09-2018")); // missing day
        assertFalse(Date.isValidDate("31-2018")); // missing month
        assertFalse(Date.isValidDate("15-09")); // missing year

        // invalid parts
        assertFalse(Date.isValidDate("49-02-2018")); // invalid day
        assertFalse(Date.isValidDate("15-13-2018")); // invalid month
        assertFalse(Date.isValidDate("15-09-20192")); // invalid year
        assertFalse(Date.isValidDate("31-02-2018")); // invalid date
        assertFalse(Date.isValidDate("31-04-2018")); // invalid date
        assertFalse(Date.isValidDate("13/02/2018")); // invalid separator

        // valid Date
        assertTrue(Date.isValidDate("15-02-2018"));
        assertTrue(Date.isValidDate("15-02-2017"));
        assertTrue(Date.isValidDate("31-12-2017"));
        assertTrue(Date.isValidDate("1-12-2017"));
        assertTrue(Date.isValidDate("31-1-2017"));
    }

    @Test
    public void validToString() {
        Date test = new Date("15-02-2018");
        assertTrue("15-02-2018".equals("" + test));
    }

    @Test
    public void equals() {
        Date testDate = new Date("02-10-2018");
        assertFalse(testDate.equals(null));
        assertFalse(testDate.equals(" "));

        assertTrue(testDate.equals(testDate));
        assertTrue(testDate.equals(new Date("02-10-2018")));
    }

    @Test
    public void compare() {
        Date date1 = new Date ("2-10-2018");
        assertTrue(Date.compare(date1, new Date ("3-10-2018")) > 0);
        assertTrue(Date.compare(date1, new Date ("2-10-2018")) == 0);
        assertTrue(Date.compare(date1, new Date ("1-10-2018")) < 0);
    }
}
