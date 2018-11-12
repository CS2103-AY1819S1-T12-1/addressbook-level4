package seedu.expensetracker.model.user;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static seedu.expensetracker.model.user.PasswordTest.VALID_PASSWORD_STRING;
import static seedu.expensetracker.testutil.Assert.assertThrows;
import static seedu.expensetracker.testutil.TypicalExpenses.SAMPLE_USERNAME;

import org.junit.jupiter.api.Test;

import seedu.expensetracker.logic.LoginCredentials;
import seedu.expensetracker.logic.parser.exceptions.ParseException;

//@@author JasonChong96
class LoginCredentialsTest {

    @Test
    public void constructor_nullUsername_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new LoginCredentials(null, ""));
        assertThrows(NullPointerException.class, () -> new LoginCredentials(null, null));
    }

    @Test
    public void constructor_nullPassword_success() throws ParseException {
        LoginCredentials toTest = new LoginCredentials(SAMPLE_USERNAME, null);
        assertNotNull(toTest.getUsername());
        assertFalse(toTest.getPassword().isPresent());
        assertFalse(toTest.getPlainPassword().isPresent());
    }

    @Test
    public void constructor_noNull_success() throws ParseException {
        LoginCredentials toTest = new LoginCredentials(SAMPLE_USERNAME, VALID_PASSWORD_STRING);
        assertNotNull(toTest.getUsername());
        assertTrue(toTest.getPassword().isPresent());
        assertEquals(toTest.getPlainPassword().get(), VALID_PASSWORD_STRING);
    }

    @Test
    void testEquals() throws ParseException {
        LoginCredentials sampleUserWithPassword = new LoginCredentials(SAMPLE_USERNAME, VALID_PASSWORD_STRING);
        LoginCredentials sampleUserWithNoPassword = new LoginCredentials(SAMPLE_USERNAME, null);
        LoginCredentials sampleUserLowercaseWithPassword =
                new LoginCredentials(new Username(SAMPLE_USERNAME.toString().toLowerCase()), VALID_PASSWORD_STRING);
        LoginCredentials sampleUserUppercaseWithPassword =
                new LoginCredentials(new Username(SAMPLE_USERNAME.toString().toUpperCase()), VALID_PASSWORD_STRING);

        assertEquals(sampleUserWithPassword, new LoginCredentials(SAMPLE_USERNAME, VALID_PASSWORD_STRING));
        assertEquals(sampleUserWithNoPassword, new LoginCredentials(SAMPLE_USERNAME, null));
        assertNotEquals(sampleUserWithPassword, sampleUserWithNoPassword);
        // Username should be case-insensitive
        assertEquals(sampleUserLowercaseWithPassword, sampleUserUppercaseWithPassword);
    }

    @Test
    public void testHashCode() throws ParseException {
        // Only equality is test as there is no guarantee of no hash collision
        assertEquals(new LoginCredentials(SAMPLE_USERNAME, VALID_PASSWORD_STRING).hashCode(),
                new LoginCredentials(SAMPLE_USERNAME, VALID_PASSWORD_STRING).hashCode());
        assertEquals(new LoginCredentials(SAMPLE_USERNAME, null).hashCode(),
                new LoginCredentials(SAMPLE_USERNAME, null).hashCode());
    }
}
