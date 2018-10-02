package seedu.address.model.expense;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Calendar;

import org.junit.Test;

import seedu.address.testutil.Assert;

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
        test.fullDate.set(2018, 1, 15);
        Date testDate = new Date("15-02-2018");
        assertTrue(testDate.equals(test));

        Calendar now = Calendar.getInstance();
        testDate = new Date();
        assertTrue(now.get(Calendar.DATE) == testDate.fullDate.get(Calendar.DATE));
        assertTrue(now.get(Calendar.MONTH) == testDate.fullDate.get(Calendar.MONTH));
        assertTrue(now.get(Calendar.YEAR) == testDate.fullDate.get(Calendar.YEAR));

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
}
