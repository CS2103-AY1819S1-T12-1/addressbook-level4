package seedu.address.model.expense;

import static seedu.address.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import seedu.address.commons.util.StringUtil;

import seedu.address.logic.parser.ArgumentMultimap;

//@@author jcjxwy
/**
 * Tests that a {@code Expense}'s {@code Name, Category, Cost, Date, Tag} matches all of the keywords given.
 */
public class ExpenseContainsKeywordsPredicate implements Predicate<Expense> {
    private final ArgumentMultimap keywords;

    public ExpenseContainsKeywordsPredicate(ArgumentMultimap keywords) {
        this.keywords = keywords;
    }

    @Override
    public boolean test(Expense expense) {
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
            List<String> splitNameKeywords = Arrays.asList(nameKeywords.trim().split("\\s+"));
            result = result && splitNameKeywords.stream()
                    .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(expense.getName().expenseName, keyword));
        }


        if (!categoryKeywords.equals("")) {
            List<String> separatedCategoryKeywords = Arrays.asList(categoryKeywords.trim().split("\\s+"));
            result = result && separatedCategoryKeywords.stream()
                    .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(
                            expense.getCategory().categoryName, keyword));
        }


        if (!costKeywords.equals("")) {
            String[] splitCost = costKeywords.split(":");
            if (splitCost.length == 1) { //if the user enters a particular cost
                double chosenCost = Double.parseDouble(splitCost[0]);
                result = result && expense.getCost().getCostValue() == chosenCost;
            } else { //if the user enters a range of dates
                double lowerBound = Double.parseDouble(splitCost[0]);
                double higherBound = Double.parseDouble(splitCost[1]);
                result = result && (lowerBound <= expense.getCost().getCostValue()
                        && expense.getCost().getCostValue() <= higherBound);
            }
        }


        if (!dateKeywords.equals("")) {
            String[] splitDate = dateKeywords.split(":");
            if (splitDate.length == 1) { //if the user only enter a particular date
                Date chosenDate = new Date(splitDate[0]);
                result = result && expense.getDate().equals(chosenDate);
            } else { //if the user enter a range of dates
                Date start = new Date(splitDate[0]);
                Date end = new Date(splitDate[1]);
                result = result && (start.equals(expense.getDate())
                        || end.equals(expense.getDate())
                        || (Date.compare(start, expense.getDate()) == 1
                        && Date.compare(expense.getDate(), end) == 1));
            }
        }


        if (!tagKeywords.isEmpty()) {
            List<String> separatedTagKeywordsList = new ArrayList<>();
            for (String tag : tagKeywords) {
                separatedTagKeywordsList.addAll(Arrays.asList(tag.split("\\s+")));
            }
            result = result
                    && tagKeywords.stream()
                    .anyMatch(keyword -> expense.getTags().stream()
                    .anyMatch(tag -> StringUtil.containsWordIgnoreCase(tag.tagName, keyword)));
        }
        return result;
    }



    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ExpenseContainsKeywordsPredicate // instanceof handles nulls
                && keywords.equals(((ExpenseContainsKeywordsPredicate) other).keywords)); // state check
    }

}
