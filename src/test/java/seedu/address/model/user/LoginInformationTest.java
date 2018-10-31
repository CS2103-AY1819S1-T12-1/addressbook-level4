package seedu.address.model.user;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static seedu.address.model.user.PasswordTest.VALID_PASSWORD_STRING;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalExpenses.SAMPLE_USERNAME;

import org.junit.jupiter.api.Test;

import seedu.address.logic.parser.exceptions.ParseException;

//@@author JasonChong96
class LoginInformationTest {

    @Test
    public void constructor_nullUsername_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new LoginInformation(null, ""));
        assertThrows(NullPointerException.class, () -> new LoginInformation(null, null));
    }

    @Test
    public void constructor_nullPassword_success() throws ParseException {
        LoginInformation toTest = new LoginInformation(SAMPLE_USERNAME, null);
        assertNotNull(toTest.getUsername());
        assertFalse(toTest.getPassword().isPresent());
        assertFalse(toTest.getPlainPassword().isPresent());
    }

    @Test
    public void constructor_noNull_success() throws ParseException {
        LoginInformation toTest = new LoginInformation(SAMPLE_USERNAME, VALID_PASSWORD_STRING);
        assertNotNull(toTest.getUsername());
        assertTrue(toTest.getPassword().isPresent());
        assertEquals(toTest.getPlainPassword().get(), VALID_PASSWORD_STRING);
    }

    @Test
    void testEquals() throws ParseException {
        LoginInformation sampleUserWithPassword = new LoginInformation(SAMPLE_USERNAME, VALID_PASSWORD_STRING);
        LoginInformation sampleUserWithNoPassword = new LoginInformation(SAMPLE_USERNAME, null);
        LoginInformation sampleUserLowercaseWithPassword =
                new LoginInformation(new Username(SAMPLE_USERNAME.toString().toLowerCase()), VALID_PASSWORD_STRING);
        LoginInformation sampleUserUppercaseWithPassword =
                new LoginInformation(new Username(SAMPLE_USERNAME.toString().toUpperCase()), VALID_PASSWORD_STRING);

        assertEquals(sampleUserWithPassword, new LoginInformation(SAMPLE_USERNAME, VALID_PASSWORD_STRING));
        assertEquals(sampleUserWithNoPassword, new LoginInformation(SAMPLE_USERNAME, null));
        assertNotEquals(sampleUserWithPassword, sampleUserWithNoPassword);
        // Username should be case-insensitive
        assertEquals(sampleUserLowercaseWithPassword, sampleUserUppercaseWithPassword);
    }

    @Test
    public void testHashCode() throws ParseException {
        // Only equality is test as there is no guarantee of no hash collision
        assertEquals(new LoginInformation(SAMPLE_USERNAME, VALID_PASSWORD_STRING).hashCode(),
                new LoginInformation(SAMPLE_USERNAME, VALID_PASSWORD_STRING).hashCode());
        assertEquals(new LoginInformation(SAMPLE_USERNAME, null).hashCode(),
                new LoginInformation(SAMPLE_USERNAME, null).hashCode());
    }
}
