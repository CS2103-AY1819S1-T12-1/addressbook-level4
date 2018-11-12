package seedu.expensetracker.commons.exceptions;
//@@author winsonhys

import seedu.expensetracker.logic.parser.exceptions.ParseException;

/**
 * Exception that is thrown when user enters a negative value.
 */
public class NegativeValueParseException extends ParseException {
    public static final String NEGATIVE_EXCEPTION_MESSAGE = "Only values more than 0 are allowed";

    public NegativeValueParseException() {
        super(NEGATIVE_EXCEPTION_MESSAGE);
    }
}
