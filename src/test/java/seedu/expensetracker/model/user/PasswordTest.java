package seedu.expensetracker.model.user;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PasswordTest {
    public static final String INVALID_PASSWORD_STRING_SPACE = " ______";
    public static final String INVALID_PASSWORD_STRING_SHORT = "_____";
    public static final String VALID_PASSWORD_STRING = "aaaaaa";
    public static final Password VALID_PASSWORD = new Password(VALID_PASSWORD_STRING, true);

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void constructor_nullPlainString_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new Password(null, true);
    }

    @Test
    public void constructor_nullHashString_throwsNullPointerException() {
        thrown.expect(NullPointerException.class);
        new Password(null, false);
    }

    @Test
    public void constructor_validPlainString_assertNoException() {
        new Password(VALID_PASSWORD_STRING, true);
    }

    @Test
    public void constructor_hashString_assertNoException() {
        new Password(INVALID_PASSWORD_STRING_SHORT, false);
        new Password(INVALID_PASSWORD_STRING_SPACE, false);
    }

    @Test
    public void constructor_hashString_assertNotRehashed() {
        assertEquals(new Password(INVALID_PASSWORD_STRING_SHORT, false).toString(),
                INVALID_PASSWORD_STRING_SHORT);
        assertEquals(new Password(INVALID_PASSWORD_STRING_SPACE, false).toString(),
                INVALID_PASSWORD_STRING_SPACE);
        assertEquals(new Password(VALID_PASSWORD_STRING, false).toString(),
                VALID_PASSWORD_STRING);
    }

    @Test
    public void testIsValidPassword() {
        assertFalse(Password.isValidPassword(""));
        assertFalse(Password.isValidPassword(INVALID_PASSWORD_STRING_SHORT));
        assertFalse(Password.isValidPassword(INVALID_PASSWORD_STRING_SPACE));
    }

    @Test
    public void toString_noPlainTextPassword() {
        assertNotEquals(VALID_PASSWORD.toString(), VALID_PASSWORD_STRING);
    }

    @Test
    public void testEquals() {
        assertNotEquals(new Object(), VALID_PASSWORD); // different class
        assertNotEquals(VALID_PASSWORD, new Password(INVALID_PASSWORD_STRING_SPACE, false));
        assertEquals(VALID_PASSWORD, new Password(VALID_PASSWORD_STRING, true)); // Same hash
        assertNotEquals(VALID_PASSWORD, new Password(VALID_PASSWORD_STRING.toUpperCase(), true));
        // Different case should be a different password
    }

    @Test
    public void testHashCode() {
        assertEquals(VALID_PASSWORD.hashCode(), new Password(VALID_PASSWORD_STRING, true).hashCode());
        // Same hash
    }
}
