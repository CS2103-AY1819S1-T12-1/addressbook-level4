package seedu.expensetracker.storage;

import javax.xml.bind.annotation.XmlValue;

import seedu.expensetracker.model.user.Password;

//@@author JasonChong96
/**
 * JAXB-friendly adapted version of the Password.
 */
public class XmlAdaptedPassword {
    @XmlValue
    private String password;

    /**
     * Constructs an XmlAdaptedPassword.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedPassword() {
    }

    /**
     * Constructs a {@code XmlAdaptedPassword} with the given {@code passwordHash}.
     */
    public XmlAdaptedPassword(String passwordHash) {
        this.password = passwordHash;
    }

    /**
     * Converts a given Password into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created
     */
    public XmlAdaptedPassword(Password source) {
        this.password = source.toString();
    }

    /**
     * Converts this jaxb-friendly adapted tag object into the model's Password object.
     */
    public Password toModelType() {
        return new Password(password, false);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof XmlAdaptedPassword)) {
            return false;
        }

        return password.equals(((XmlAdaptedPassword) other).password);
    }
}
