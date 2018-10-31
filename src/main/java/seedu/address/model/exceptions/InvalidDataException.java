package seedu.address.model.exceptions;

//@@author JasonChong96
/**
 * Represents an error where a user has invalid data.
 */
public class InvalidDataException extends Exception {

    /**
     * Default constructor for InvalidDataException.
     */
    public InvalidDataException() {
        super("The data associated with ths user contains invalid values.");
    }
}
