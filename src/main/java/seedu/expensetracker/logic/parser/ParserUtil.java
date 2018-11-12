package seedu.expensetracker.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.expensetracker.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.expensetracker.logic.commands.FindCommand.MESSAGE_INVALID_COST_KEYWORDS_FORMAT;
import static seedu.expensetracker.logic.commands.FindCommand.MESSAGE_INVALID_DATE_KEYWORDS_FORMAT;
import static seedu.expensetracker.logic.commands.FindCommand.MESSAGE_INVALID_RANGE;
import static seedu.expensetracker.logic.commands.FindCommand.MESSAGE_MULTIPLE_KEYWORDS;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import seedu.expensetracker.commons.core.index.Index;
import seedu.expensetracker.commons.util.StringUtil;
import seedu.expensetracker.logic.parser.exceptions.ParseException;
import seedu.expensetracker.model.expense.Category;
import seedu.expensetracker.model.expense.Cost;
import seedu.expensetracker.model.expense.Date;
import seedu.expensetracker.model.expense.Name;
import seedu.expensetracker.model.tag.Tag;
import seedu.expensetracker.model.user.Password;
import seedu.expensetracker.model.user.Username;

/**
 * Contains utility methods used for parsing strings in the various *Parser classes.
 */
public class ParserUtil {

    public static final String MESSAGE_INVALID_INDEX = "Index is not a non-zero unsigned integer.";

    /**
     * Parses {@code oneBasedIndex} into an {@code Index} and returns it. Leading and trailing whitespaces will be
     * trimmed.
     * @throws ParseException if the specified index is invalid (not non-zero unsigned integer).
     */
    public static Index parseIndex(String oneBasedIndex) throws ParseException {
        String trimmedIndex = oneBasedIndex.trim();
        if (!StringUtil.isNonZeroUnsignedInteger(trimmedIndex)) {
            throw new ParseException(MESSAGE_INVALID_INDEX);
        }
        return Index.fromOneBased(Integer.parseInt(trimmedIndex));
    }

    /**
     * Parses a {@code String name} into a {@code Name}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code name} is invalid.
     */
    public static Name parseName(String name) throws ParseException {
        requireNonNull(name);
        String trimmedName = name.trim();
        if (!Name.isValidName(trimmedName)) {
            throw new ParseException(Name.MESSAGE_NAME_CONSTRAINTS);
        }
        return new Name(trimmedName);
    }

    /**
     * Parses a {@code String category} into a {@code Category}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code category} is invalid.
     */
    public static Category parseCategory(String category) throws ParseException {
        requireNonNull(category);
        String trimmedCategory = category.trim();
        if (!Category.isValidCategory(trimmedCategory)) {
            throw new ParseException(Category.MESSAGE_CATEGORY_CONSTRAINTS);
        }
        return new Category(trimmedCategory);
    }

    /**
     * Parses a {@code String cost} into an {@code Cost}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code expensetracker} is invalid.
     */
    public static Cost parseCost(String cost) throws ParseException {
        requireNonNull(cost);
        String trimmedCost = cost.trim();
        if (!Cost.isValidCost(trimmedCost)) {
            throw new ParseException(Cost.MESSAGE_COST_CONSTRAINTS);
        }
        return new Cost(trimmedCost);
    }

    /**
     * Parses a {@code String tag} into a {@code Tag}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code tag} is invalid.
     */
    public static Tag parseTag(String tag) throws ParseException {
        requireNonNull(tag);
        String trimmedTag = tag.trim();
        if (!Tag.isValidTagName(trimmedTag)) {
            throw new ParseException(Tag.MESSAGE_TAG_CONSTRAINTS);
        }
        return new Tag(trimmedTag);
    }

    /**
     * Parses a {@code String date} into a {@code Date}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code Date} is invalid.
     */
    public static Date parseDate(String date) throws ParseException {
        requireNonNull(date);
        String trimmedDate = date.trim();
        if (!Date.isValidDate(trimmedDate)) {
            throw new ParseException(Date.DATE_FORMAT_CONSTRAINTS);
        }
        return new Date(trimmedDate);
    }

    /**
     * Parses a {@code String Username} into a {@code Username}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code Username} is invalid.
     */
    public static Username parseUsername(String username) throws ParseException {
        requireNonNull(username);
        String trimmedUsername = username.trim();
        if (!Username.isValidName(trimmedUsername)) {
            throw new ParseException(Username.MESSAGE_NAME_CONSTRAINTS);
        }
        return new Username(trimmedUsername);
    }

    /**
     * Parses a {@code String password} into a {@code Password}.
     * Leading and trailing whitespaces will be trimmed.
     *
     * @throws ParseException if the given {@code Password} is invalid.
     */
    public static Password parsePassword(String password) throws ParseException {
        requireNonNull(password);
        String trimmedPassword = password.trim();
        if (!Password.isValidPassword(trimmedPassword)) {
            throw new ParseException(Password.MESSAGE_PASSWORD_CONSTRAINTS);
        }
        return new Password(trimmedPassword, true);
    }

    /**
     * Parses a {@code String hours} into seconds
     * @param hours Number of hours
     * @return Number of hours in seconds in long type
     * @throws IllegalArgumentException
     */
    public static long parseHours(String hours) throws IllegalArgumentException {
        if (hours == null) {
            return 0;
        }
        long returnHours = Long.parseLong(hours) * 60 * 60;
        if (returnHours < 0) {
            throw new IllegalArgumentException("Number of hours must be more than 0");
        }
        return returnHours;
    }

    /**
     * Parses a {@code String minutes} into seconds
     * @param minutes Number of minutes
     * @return Number of minutes in seconds in long type
     * @throws IllegalArgumentException
     */

    public static long parseMinutes(String minutes) {
        if (minutes == null) {
            return 0;
        }
        long returnMinutes = Long.parseLong(minutes) * 60;
        if (returnMinutes < 0) {
            throw new IllegalArgumentException("Number of minutes must be more than 0");
        }
        return returnMinutes;
    }

    /**
     * Parses a {@code String seconds} into seconds
     * @param seconds Number of seconds
     * @return Number of seconds in long type
     * @throws ParseException
     */
    public static long parseSeconds(String seconds) throws IllegalArgumentException {
        if (seconds == null) {
            return 0;
        }
        long returnSeconds = Long.parseLong(seconds);
        if (returnSeconds < 0) {
            throw new IllegalArgumentException("Number of minutes must be more than 0");
        }
        return returnSeconds;
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>}.
     */
    public static Set<Tag> parseTags(Collection<String> tags) throws ParseException {
        requireNonNull(tags);
        final Set<Tag> tagSet = new HashSet<>();
        for (String tagName : tags) {
            tagSet.add(parseTag(tagName));
        }
        return tagSet;
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    public static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    //@@author jcjxwy
    /**
     * Check whether all the keywords are valid.
     * @throws ParseException if any keyword entered by user does not conform the expected format, or user enters
     * multiple keywords for Name/Category/Cost/Date.
     * */
    public static void ensureKeywordsAreValid(ArgumentMultimap keywordsMap) throws ParseException {
        List<String> nameKeywords = keywordsMap.getAllValues(PREFIX_NAME);
        List<String> categoryKeywords = keywordsMap.getAllValues(PREFIX_CATEGORY);
        List<String> tagKeywords = keywordsMap.getAllValues(PREFIX_TAG);
        List<String> dateKeywords = keywordsMap.getAllValues(PREFIX_DATE);
        List<String> costKeywords = keywordsMap.getAllValues(PREFIX_COST);

        requireValidNameKeywords(nameKeywords);
        requireValidCategoryKeywords(categoryKeywords);
        requireValidTagKeywords(tagKeywords);
        requireValidDateKeywords(dateKeywords);
        requireValidCostKeywords(costKeywords);
    }

    /**
     * Check whether the name keyword is valid.
     * @throws ParseException if the user enters multiple name keywords or the name keyword is invalid
     * */
    public static void requireValidNameKeywords(List<String> nameKeywords) throws ParseException {
        //If the user enters multiple name keywords
        if (nameKeywords.size() > 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MESSAGE_MULTIPLE_KEYWORDS));
        }

        //If the name keyword is invalid
        if (!nameKeywords.isEmpty() && !Name.isValidName(nameKeywords.get(0))) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, Name.MESSAGE_NAME_CONSTRAINTS));
        }
    }

    /**
     * Check whether the category keyword is valid.
     * @throws ParseException if the user enters multiple category keywords or the category keyword is invalid
     * */
    public static void requireValidCategoryKeywords(List<String> categoryKeywords) throws ParseException {
        //If the user enters multiple category keywords
        if (categoryKeywords.size() > 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MESSAGE_MULTIPLE_KEYWORDS));
        }

        //If the category keyword is invalid
        if (!categoryKeywords.isEmpty() && !Category.isValidCategory(categoryKeywords.get(0))) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    Category.MESSAGE_CATEGORY_CONSTRAINTS));
        }
    }

    /**
     * Check whether the tag keywords are valid.
     * @throws ParseException any of the tag keyword is invalid
     * */
    public static void requireValidTagKeywords(List<String> tagKeywords) throws ParseException {
        if (!tagKeywords.isEmpty()) {
            for (String tag : tagKeywords) {
                if (!Tag.isValidTagName(tag)) {
                    throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                            Tag.MESSAGE_TAG_CONSTRAINTS));
                }
            }
        }
    }

    /**
     * Check whether the date keyword is valid.
     * @throws ParseException if the user enters multiple date keywords or the date keyword is invalid
     * */
    public static void requireValidDateKeywords(List<String> dateKeywords) throws ParseException {
        //If the user enters multiple date keywords
        if (dateKeywords.size() > 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MESSAGE_MULTIPLE_KEYWORDS));
        }

        if (!dateKeywords.isEmpty()) {
            String[] dates = dateKeywords.get(0).split(":");
            //If the user enters one date keyword and it is invalid
            if (dates.length == 1 && !Date.isValidDate(dates[0])) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        Date.DATE_FORMAT_CONSTRAINTS + "\n" + MESSAGE_INVALID_DATE_KEYWORDS_FORMAT));
            }

            //If the user enters a range of date keywords and any of the two dates is invalid
            if (dates.length == 2 && (!Date.isValidDate(dates[0]) || !Date.isValidDate(dates[1]))) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        Date.DATE_FORMAT_CONSTRAINTS));
            }

            //If the ending date is earlier than the starting date
            if (dates.length == 2 && Date.compare(new Date(dates[0]), new Date(dates[1])) < 0) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        MESSAGE_INVALID_RANGE));
            }

            //If the user enters more than one colon
            if (dates.length > 2) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        MESSAGE_INVALID_DATE_KEYWORDS_FORMAT));
            }

        }
    }

    /**
     * Check whether the cost keyword is valid.
     * @throws ParseException if the user enters multiple cost keywords or the cost keyword is invalid
     * */
    public static void requireValidCostKeywords(List<String> costKeywords) throws ParseException {
        //If the user enters multiple cost keywords
        if (costKeywords.size() > 1) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    MESSAGE_MULTIPLE_KEYWORDS));
        }

        if (!costKeywords.isEmpty()) {
            String[] costs = costKeywords.get(0).split(":");
            //If the user enters one keyword and it is invalid
            if (costs.length == 1 && !Cost.isValidCost(costs[0])) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        Cost.MESSAGE_COST_CONSTRAINTS + "\n" + MESSAGE_INVALID_COST_KEYWORDS_FORMAT));
            }

            //If the user enters two keywords and any of them is invalid
            if (costs.length == 2 && (!Cost.isValidCost(costs[0]) || !Cost.isValidCost(costs[1]))) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        Cost.MESSAGE_COST_CONSTRAINTS));
            }

            //If the higher bound is smaller than the lower bound
            if (costs.length == 2 && Double.parseDouble(costs[1]) < Double.parseDouble(costs[0])) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        MESSAGE_INVALID_RANGE));
            }

            //If the user enters more than one colon
            if (costs.length > 2) {
                throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                        MESSAGE_INVALID_COST_KEYWORDS_FORMAT));

            }
        }
    }

}
