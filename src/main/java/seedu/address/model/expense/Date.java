package seedu.address.model.expense;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

//@@author jonathantjm
/**
 * Represents the date the ExpenseTemp was added into the ExpenseTemp tracker.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class Date {
    public static final String MESSAGE_FORMAT_CONSTRAINTS =
            "Date should be valid, only contain numbers and slashes, and it should not be blank";

    public static final String DATE_VALIDATION_REGEX = "(\\d{1,2})(\\-)(\\d{1,2})(\\-)(\\d{4})";
    public final Calendar fullDate = Calendar.getInstance();


    /**
     * Constructs a {@code Date}.
     *
     * @param date A valid date.
     */
    public Date(String date) {
        requireNonNull(date);
        checkArgument(isValidDate(date), MESSAGE_FORMAT_CONSTRAINTS);
        String [] parsedDate = date.split("-");
        fullDate.set(Integer.parseInt(parsedDate[2]),
                Integer.parseInt(parsedDate[1]) - 1,
                Integer.parseInt(parsedDate[0]));
    }

    /**
     * Constructs a {@code Date} with current date.
     *
     */
    public Date() {}

    /**
     * return true is the given date is in the valid format.
     * */
    public static boolean isValidDate(String test) {
        if (test.matches(DATE_VALIDATION_REGEX)) {
            Calendar date = new GregorianCalendar();
            String [] parsedDate = test.split("-");
            date.setLenient(false);
            date.set(Integer.parseInt(parsedDate[2]),
                    Integer.parseInt(parsedDate[1]) - 1,
                    Integer.parseInt(parsedDate[0]));
            try {
                date.getTime();
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        return dateFormat.format(fullDate.getTime());
    }
}
