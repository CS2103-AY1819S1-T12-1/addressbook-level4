package seedu.expensetracker.commons.exceptions;
//@@author winsonhys

import seedu.expensetracker.logic.parser.exceptions.ParseException;

/**
 * Exception that is thrown when user enters a too high of a value.
 */
public class TooRichException extends ParseException {
    public static final String TOO_RICH_EXCEPTION_MESSAGE = "Are you sure you are richer than Bill Gates?";

    public TooRichException () {
        super(TOO_RICH_EXCEPTION_MESSAGE);
    }
}
