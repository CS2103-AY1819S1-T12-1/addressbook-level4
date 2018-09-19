package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

public class Date {

    public static final String MESSAGE_DATE_CONSTRAINTS =
            "Date should only contain numbers and slashes, and it should not be blank";

    public static final String DATE_VALIDATION_REGEX = "(\\d{1,2})(\\-)(\\d{1,2})(\\-)(\\d{4})";
    public final String fullDate;

    /**
     * Constructs a {@code Date}.
     *
     * @param date A valid date.
     */
    public Date(String date){
        requireNonNull(date);
        checkArgument(isValidDate(date), MESSAGE_DATE_CONSTRAINTS);
        fullDate = date;
    }

    /**
     * return true is the given date is a valid date.
     * */
    private boolean isValidDate(String test){
        return test.matches(DATE_VALIDATION_REGEX);
    }


}
