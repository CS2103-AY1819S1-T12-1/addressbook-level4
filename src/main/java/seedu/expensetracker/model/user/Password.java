package seedu.expensetracker.model.user;

import static com.google.common.hash.Hashing.sha256;
import static java.util.Objects.requireNonNull;
import static seedu.expensetracker.commons.util.AppUtil.checkArgument;
import static seedu.expensetracker.model.encryption.EncryptionUtil.DEFAULT_CHARSET;

//@@author JasonChong96
/**
 * Represents the Password of a user.
 * Guarantees: immutable; is valid as declared in {@link #isValidPassword(String)}
 */
public class Password {
    public static final String MESSAGE_PASSWORD_CONSTRAINTS =
            "Passwords cannot contain spaces and must be 6 to 100 characters long.";

    /*
     * The first character of the expensetracker must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    private static final String PASSWORD_VALIDATION_REGEX = "[^\\s]{6,100}+$";

    private final String passwordHash;

    /**
     * Constructs a {@code Password}.
     *
     * @param password A valid password or password hash.
     * @param isPlainText true if the input password is in plain text, false if it's already been hashed
     */
    public Password(String password, boolean isPlainText) {
        requireNonNull(password);
        if (isPlainText) {
            checkArgument(isValidPassword(password), MESSAGE_PASSWORD_CONSTRAINTS);
            this.passwordHash = sha256().hashString(password, DEFAULT_CHARSET).toString();
        } else {
            this.passwordHash = password;
        }
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidPassword(String test) {
        return !test.isEmpty() && test.matches(PASSWORD_VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return passwordHash;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Password // instanceof handles nulls
                && passwordHash.equals(((Password) other).passwordHash)); // state check
    }

    @Override
    public int hashCode() {
        return passwordHash.hashCode();
    }
}
