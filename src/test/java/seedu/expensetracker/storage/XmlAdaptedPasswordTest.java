package seedu.expensetracker.storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.expensetracker.model.user.PasswordTest;

public class XmlAdaptedPasswordTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testEquals() {
        XmlAdaptedPassword validXmlAdaptedPassword = new XmlAdaptedPassword(PasswordTest.VALID_PASSWORD_STRING);

        assertNotEquals(new XmlAdaptedPassword(PasswordTest.VALID_PASSWORD_STRING.toLowerCase()),
                new XmlAdaptedPassword(PasswordTest.VALID_PASSWORD_STRING.toUpperCase()));
        assertEquals(validXmlAdaptedPassword, validXmlAdaptedPassword);
        assertEquals(new XmlAdaptedPassword(PasswordTest.VALID_PASSWORD_STRING),
                new XmlAdaptedPassword(PasswordTest.VALID_PASSWORD_STRING));
        assertNotEquals(new XmlAdaptedPassword(PasswordTest.VALID_PASSWORD_STRING), new Object());
    }

    @Test
    public void testToModelType_noArguments() {
        thrown.expect(NullPointerException.class);
        new XmlAdaptedPassword().toModelType();
    }

    @Test
    public void testToModelType() {
        assertEquals(new XmlAdaptedPassword(PasswordTest.VALID_PASSWORD_STRING).toModelType().toString(),
                PasswordTest.VALID_PASSWORD_STRING);
    }

    @Test
    public void testConstructor_withSource() {
        assertEquals(new XmlAdaptedPassword(PasswordTest.VALID_PASSWORD).toModelType(),
                PasswordTest.VALID_PASSWORD);
    }
}
