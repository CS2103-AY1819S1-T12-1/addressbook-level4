package seedu.expensetracker.logic;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;

import seedu.expensetracker.logic.parser.ParserUtil;
import seedu.expensetracker.logic.parser.exceptions.ParseException;
import seedu.expensetracker.model.user.Password;
import seedu.expensetracker.model.user.Username;

/**
 * Encapsulates the credentials used when the user attempts to log in.
 * Guarantees: immutable, password is null if and only if plainPassword is null.
 */
public class LoginCredentials {
    private final Username username;
    private final Password password;
    private final String plainPassword;

    /**
     * Constructor for login credentials. Guarantees that password is null if and only if plainPassword is null.
     * @param username the username
     * @param plainPassword the password as a plain text String
     */
    public LoginCredentials(Username username, String plainPassword) throws ParseException {
        requireNonNull(username);
        this.username = username;
        this.plainPassword = plainPassword;
        if (plainPassword != null) {
            this.password = ParserUtil.parsePassword(plainPassword);
        } else {
            this.password = null;
        }
    }

    public Username getUsername() {
        return username;
    }

    public Optional<Password> getPassword() {
        return Optional.ofNullable(password);
    }

    public Optional<String> getPlainPassword() {
        return Optional.ofNullable(plainPassword);
    }

    @Override
    public boolean equals(Object obj) {
        // short circuit if same object
        if (obj == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(obj instanceof LoginCredentials)) {
            return false;
        }

        // state check
        LoginCredentials other = (LoginCredentials) obj;
        return username.equals(other.username)
                && getPassword().equals(other.getPassword())
                && getPlainPassword().equals(other.getPlainPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, plainPassword);
    }
}
