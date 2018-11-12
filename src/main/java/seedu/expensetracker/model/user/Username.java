package seedu.expensetracker.model.user;

import static java.util.Objects.requireNonNull;
import static seedu.expensetracker.commons.util.AppUtil.checkArgument;

//@@author JasonChong96
/**
 * Represents the Username of a user.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Username implements Comparable<Username> {
    public static final String MESSAGE_NAME_CONSTRAINTS =
            "Names must be at most 250 characters long, cannot be empty and cannot contain white spaces or any of "
                    + "these characters: \" > < : \\ / | ? *";

    /*
     * Username cannot contain any of the following characters : " > < : \ / | ? *
     */
    private static final String USERNAME_VALIDATION_REGEX = ".*[/\\\\:*?\"<>|\\s].*";

    private final String name;

    /**
     * Constructs a {@code Username}.
     *
     * @param name A valid name.
     */
    public Username(String name) {
        requireNonNull(name);
        checkArgument(isValidName(name), MESSAGE_NAME_CONSTRAINTS);
        this.name = name;
    }

    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return !test.isEmpty() && !test.matches(USERNAME_VALIDATION_REGEX) && test.length() <= 250;
    }


    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Username // instanceof handles nulls
                && name.equalsIgnoreCase(((Username) other).name)); // state check
    }

    @Override
    public int hashCode() {
        return name.toLowerCase().hashCode();
    }

    @Override
    public int compareTo(Username o) {
        return String.CASE_INSENSITIVE_ORDER.compare(this.name, o.name);
    }
}
