package seedu.address.model.user;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import java.util.Optional;

import seedu.address.logic.parser.ParserUtil;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Encapsulates the information passed when the user attempts to log in.
 * Guarantees: immutable, password is null if and only if plainPassword is null.
 */
public class LoginInformation {
    private final Username username;
    private final Password password;
    private final String plainPassword;

    /**
     * Constructor for login information. Guarantees that password is null if and only if plainPassword is null.
     * @param username the username
     * @param plainPassword the password as a plain text String
     */
    public LoginInformation(Username username, String plainPassword) throws ParseException {
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
        if (!(obj instanceof LoginInformation)) {
            return false;
        }

        // state check
        LoginInformation other = (LoginInformation) obj;
        return username.equals(other.username)
                && getPassword().equals(other.getPassword())
                && getPlainPassword().equals(other.getPlainPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, plainPassword);
    }
}
