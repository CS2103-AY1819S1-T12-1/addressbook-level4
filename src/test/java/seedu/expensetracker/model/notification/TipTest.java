package seedu.expensetracker.model.notification;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import seedu.expensetracker.testutil.Assert;

//@@author Snookerballs
public class TipTest {
    private static final String VALID_HEADER = "HEADER";
    private static final String VALID_BODY = "BODY";

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Tip(null, null));
    }

    @Test
    public void constructor_valid() {
        assertEquals(new Tip(VALID_HEADER, VALID_BODY).getBody(), VALID_BODY);
        assertEquals(new Tip(VALID_HEADER, VALID_BODY).getHeader(), VALID_HEADER);
    }

    @Test
    public void toString_valid() {
        assertEquals(new Tip(VALID_HEADER, VALID_BODY).toString(), VALID_HEADER + " " + VALID_BODY);
    }

}
