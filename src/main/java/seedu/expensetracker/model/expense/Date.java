package seedu.expensetracker.model.expense;

import static seedu.expensetracker.commons.util.AppUtil.checkArgument;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//@@author jonathantjm
/**
 * Represents the date the Expense was added into the Expense tracker.
 * Guarantees: immutable; is valid as declared in {@link #isValidDate(String)}
 */
public class Date extends ExpenseField {

    /**
     * Message to show user when date is not in the correct format
     */
    public static final String DATE_FORMAT_CONSTRAINTS =
            "Date should be valid. Format dd-MM-yyyy";

    /**
     * Regex for a valid date.
     */
    public static final String DATE_VALIDATION_REGEX = "(\\d{1,2})(\\-)(\\d{1,2})(\\-)(\\d{4})";

    /**
     * The date to be stored
     */
    private LocalDateTime fullDate;

    /**
     * The format of date to be printed
     */
    private final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy");


    /**
     * Constructs a {@code Date} with current date.
     */
    public Date() {
        super("");
        fullDate = LocalDateTime.now();
    }

    /**
     * Constructs a {@code Date} with the specified {@code date}, which is
     * in the form of a {@code String}.
     *
     * @param date date the date to be initialised. The date should be in
     *             format "dd-MM-yyyy"
     */
    public Date(String date) {
        super(date);
        checkArgument(isValidDate(date), DATE_FORMAT_CONSTRAINTS);
        String [] parsedDate = date.split("-");
        setFullDate(
                Integer.parseInt(parsedDate[2]),
                Integer.parseInt(parsedDate[1]),
                Integer.parseInt(parsedDate[0])
        );
    }

    /**
     * Gets {@code fullDate}
     *
     */
    public LocalDateTime getFullDate() {
        return fullDate;
    }

    /**
     * Sets {@code fullDate} to the date information passed through the parameters.
     * Since {@code LocalDateTime} requires a time, it is defaulted to 0 hours and
     * 0 seconds when using this method.
     * @param year the int representing the year to be set
     * @param month the int representing the month to be set
     * @param day the int representing the day of the month to be set
     */
    public void setFullDate(int year, int month, int day) {
        fullDate = LocalDateTime.of(year, month, day, 0, 0);

    }

    /**
     * Checks if the date given is in the correct format and is a valid date.
     * @param test a string to be checked for validity
     * @return true if the date is valid and false if the date is in valid
     */
    public static boolean isValidDate(String test) {
        if (test.matches(DATE_VALIDATION_REGEX)) {
            String [] parsedDate = test.split("-");
            try {
                LocalDateTime date = LocalDateTime.of(Integer.parseInt(parsedDate[2]),
                        Integer.parseInt(parsedDate[1]),
                        Integer.parseInt(parsedDate[0]),
                        0,
                        0
                );
                return true;
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return fullDate.format(dateFormat);
    }

    /**
     * Checks equality between this object and {@code other} based on their calendar dates
     * @param other an object to be compared to
     * @return true if both are Dates which represent the same calendar date
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Date)) {
            return false;
        }

        Date otherDate = (Date) other;
        return
            fullDate.getDayOfMonth() == otherDate.fullDate.getDayOfMonth()
            && fullDate.getMonthValue() == otherDate.fullDate.getMonthValue()
            && fullDate.getYear() == otherDate.fullDate.getYear();
    }

    /**
     * Compares two {@code Date} objects based on their calendar dates.
     * Uses {@link LocalDateTime#compareTo} function to compare dates
     *
     * @param a - First Date to compare
     * @param b - Second Date to compare
     * @return  if b is after a, returns a negative number
     *          if b is before a, returns a positive number
     *          and returns 0 if they are equal
     */
    public static int compare(Date a, Date b) {
        LocalDateTime first = a.fullDate;
        LocalDateTime second = b.fullDate;

        return second.compareTo(first);
    }
}
