package seedu.expensetracker.model.expense;

import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import seedu.expensetracker.commons.util.StringUtil;

import seedu.expensetracker.logic.parser.ArgumentMultimap;

//@@author jcjxwy
/**
 * Tests that a {@code Expense}'s {@code Name, Category, Cost, Date, Tag} matches all of the keywords given.
 */
public class ExpenseContainsKeywordsPredicate implements Predicate<Expense> {
    private final ArgumentMultimap keywords;

    public ExpenseContainsKeywordsPredicate(ArgumentMultimap keywords) {
        assert keywords != null : "keywords should not be null.";
        this.keywords = keywords;
    }

    @Override
    public boolean test(Expense expense) {
        assert expense != null : "Expense should not be null.";

        String nameKeywords = keywords.getValue(PREFIX_NAME).orElse("");
        String categoryKeywords = keywords.getValue(PREFIX_CATEGORY).orElse("");
        List<String> tagKeywords = keywords.getAllValues(PREFIX_TAG);
        String dateKeywords = keywords.getValue(PREFIX_DATE).orElse("");
        String costKeywords = keywords.getValue(PREFIX_COST).orElse("");

        //if all keywords are absent, return false
        if (nameKeywords.equals("") && categoryKeywords.equals("") && tagKeywords.isEmpty()
                && dateKeywords.equals("") && costKeywords.equals("")) {
            return false;
        }

        //if one or more keywords are present
        boolean result = true;
        if (!nameKeywords.equals("")) {
            result = result && containsNameKeywords(nameKeywords, expense);
        }

        if (!categoryKeywords.equals("")) {
            result = result && containsCategoryKeywords(categoryKeywords, expense);
        }

        if (!costKeywords.equals("")) {
            result = result && isWithinCostRange(costKeywords, expense);
        }

        if (!dateKeywords.equals("")) {
            result = result && isWithinDateRange(dateKeywords, expense);
        }

        if (!tagKeywords.isEmpty()) {
            result = result && checkTagKeywords(tagKeywords, expense);
        }
        return result;
    }

    /**
     * Return true if the {@code Name} of {@code expense} contains {@code nameKeywords}.
     * */
    public boolean containsNameKeywords(String nameKeywords, Expense expense) {
        assert nameKeywords != null : "nameKeywords should not be null.";
        List<String> splitNameKeywords = Arrays.asList(nameKeywords.trim().split("\\s+"));
        boolean result = splitNameKeywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(expense.getName().expenseName, keyword));
        return result;
    }

    /**
     * Return true if the {@code Category} of {@code expense} contains {@code categoryKeywords}.
     * */
    public boolean containsCategoryKeywords(String categoryKeywords, Expense expense) {
        assert categoryKeywords != null : "categoryKeywords should not be null.";
        List<String> separatedCategoryKeywords = Arrays.asList(categoryKeywords.trim().split("\\s+"));
        boolean result = separatedCategoryKeywords.stream()
                .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(
                        expense.getCategory().categoryName, keyword));
        return result;
    }

    /**
     * Return true if the {@code Cost} of {@code expense} is within the range denoted by {@code costKeywords}.
     * */
    public boolean isWithinCostRange(String costKeywords, Expense expense) {
        assert costKeywords != null : "costKeywords should not be null.";
        boolean result;
        String[] splitCost = costKeywords.split(":");
        if (splitCost.length == 1) { //if the user enters a particular cost
            double chosenCost = Double.parseDouble(splitCost[0]);
            result = expense.getCost().getCostValue() == chosenCost;
        } else { //if the user enters a range of dates
            double lowerBound = Double.parseDouble(splitCost[0]);
            double higherBound = Double.parseDouble(splitCost[1]);
            result = lowerBound <= expense.getCost().getCostValue()
                    && expense.getCost().getCostValue() <= higherBound;
        }
        return result;
    }

    /**
     * Return true if the {@code Date} of {@code expense} is within the range denoted by {@code dateKeywords}.
     * */
    public boolean isWithinDateRange(String dateKeywords, Expense expense) {
        assert dateKeywords != null : "dateKeywords should not be null.";
        boolean result;
        String[] splitDate = dateKeywords.split(":");
        if (splitDate.length == 1) { //if the user only enter a particular date
            Date chosenDate = new Date(splitDate[0]);
            result = expense.getDate().equals(chosenDate);
        } else { //if the user enter a range of dates
            Date start = new Date(splitDate[0]);
            Date end = new Date(splitDate[1]);
            boolean isWithinRange = Date.compare(start, expense.getDate()) > 0
                    && Date.compare(expense.getDate(), end) > 0;
            result = start.equals(expense.getDate())
                    || end.equals(expense.getDate())
                    || isWithinRange;
        }
        return result;
    }

    /**
     * Return true if any of the {@code Tag} of {@code expense} contains any element of {@code tagKeywords}.
     * */
    public boolean checkTagKeywords(List<String> tagKeywords, Expense expense) {
        assert tagKeywords != null : "dateKeywords should not be null.";
        List<String> separatedTagKeywordsList = new ArrayList<>();
        for (String tag : tagKeywords) {
            separatedTagKeywordsList.addAll(Arrays.asList(tag.split("\\s+")));
        }
        boolean result = tagKeywords.stream()
                .anyMatch(keyword -> expense.getTags().stream()
                        .anyMatch(tag -> StringUtil.containsWordIgnoreCase(tag.tagName, keyword)));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ExpenseContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((ExpenseContainsKeywordsPredicate) other).keywords)); // state check
    }
}
