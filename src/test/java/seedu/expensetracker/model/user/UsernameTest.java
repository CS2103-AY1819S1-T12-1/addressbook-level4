package seedu.expensetracker.model.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import seedu.expensetracker.testutil.Assert;

//@@author JasonChong96
public class UsernameTest {
    public static final String INVALID_USERNAME_STRING = "*";
    public static final String VALID_USERNAME_STRING = "AAA";

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Username(null));
    }

    @Test
    public void constructor_invalidUsername_throwsIllegalArgumentException() {
        Assert.assertThrows(IllegalArgumentException.class, () -> new Username(INVALID_USERNAME_STRING));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Username(VALID_USERNAME_STRING + " "));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Username(VALID_USERNAME_STRING + "/"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Username(VALID_USERNAME_STRING + "\""));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Username(VALID_USERNAME_STRING + "|"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Username(VALID_USERNAME_STRING + ">"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Username(VALID_USERNAME_STRING + "<"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Username(VALID_USERNAME_STRING + "?"));
        Assert.assertThrows(IllegalArgumentException.class, () -> new Username(VALID_USERNAME_STRING + ":"));
        StringBuilder sb = new StringBuilder();
        sb.setLength(251);
        Assert.assertThrows(IllegalArgumentException.class, () -> new Username(sb.toString()));
    }

    @Test
    public void isValidUsername() {
        StringBuilder sb = new StringBuilder();
        sb.setLength(250);
        assertTrue(Username.isValidName(sb.toString()));
        // null user name
        Assert.assertThrows(NullPointerException.class, () -> Username.isValidName(null));
    }

    @Test
    public void testEquals() {
        assertEquals(new Username(VALID_USERNAME_STRING), new Username(VALID_USERNAME_STRING));
        assertEquals(
                new Username(VALID_USERNAME_STRING.toUpperCase()), new Username(VALID_USERNAME_STRING.toLowerCase()));
        assertNotEquals(
                new Username(VALID_USERNAME_STRING), new Username(VALID_USERNAME_STRING + VALID_USERNAME_STRING));
    }
}
