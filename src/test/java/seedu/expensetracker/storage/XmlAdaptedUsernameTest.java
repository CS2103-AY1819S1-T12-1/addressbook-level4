package seedu.expensetracker.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.expensetracker.commons.exceptions.IllegalValueException;
import seedu.expensetracker.model.user.UsernameTest;

//@@author JasonChong96
public class XmlAdaptedUsernameTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testEquals() {
        XmlAdaptedUsername validXmlAdaptedUsername = new XmlAdaptedUsername(UsernameTest.VALID_USERNAME_STRING);

        assertEquals(new XmlAdaptedUsername(UsernameTest.VALID_USERNAME_STRING.toLowerCase()),
                new XmlAdaptedUsername(UsernameTest.VALID_USERNAME_STRING.toUpperCase()));
        assertNotEquals(validXmlAdaptedUsername,
                new XmlAdaptedUsername(UsernameTest.VALID_USERNAME_STRING + UsernameTest.VALID_USERNAME_STRING));
        assertEquals(validXmlAdaptedUsername, validXmlAdaptedUsername);
        assertNotEquals(new XmlAdaptedUsername(UsernameTest.VALID_USERNAME_STRING), new Object());
    }

    @Test
    public void testToModelType() throws Exception {
        thrown.expect(IllegalValueException.class);
        new XmlAdaptedUsername(UsernameTest.INVALID_USERNAME_STRING).toModelType();
    }
}
