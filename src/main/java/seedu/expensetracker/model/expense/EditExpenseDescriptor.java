package seedu.expensetracker.model.expense;

import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_CATEGORY;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_COST;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import seedu.expensetracker.commons.util.CollectionUtil;
import seedu.expensetracker.logic.commands.EditCommand;
import seedu.expensetracker.logic.parser.ArgumentMultimap;
import seedu.expensetracker.logic.parser.ParserUtil;
import seedu.expensetracker.logic.parser.exceptions.ParseException;
import seedu.expensetracker.model.tag.Tag;

/**
 * Stores the details to edit the expense with. Each non-empty field value will replace the
 * corresponding field value of the expense.
 */
public class EditExpenseDescriptor {
    private Name name;
    private Category category;
    private Cost cost;
    private Set<Tag> tags;
    private Date date;

    public EditExpenseDescriptor() {}

    /**
     * Copy constructor.
     * A defensive copy of {@code tags} is used internally.
     */
    public EditExpenseDescriptor(EditExpenseDescriptor toCopy) {
        setName(toCopy.name);
        setCategory(toCopy.category);
        setCost(toCopy.cost);
        setTags(toCopy.tags);
        setDate(toCopy.date);
    }

    /**
     * Returns true if at least one field is edited.
     */
    public boolean isAnyFieldEdited() {
        return CollectionUtil.isAnyNonNull(name, category, cost, tags, date);
    }

    public void setName(Name name) {
        this.name = name;
    }

    public Optional<Name> getName() {
        return Optional.ofNullable(name);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Optional<Date> getDate() {
        return Optional.ofNullable(date);
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Optional<Category> getCategory() {
        return Optional.ofNullable(category);
    }

    public void setCost(Cost cost) {
        this.cost = cost;
    }

    public Optional<Cost> getCost() {
        return Optional.ofNullable(cost);
    }

    /**
     * Sets {@code tags} to this object's {@code tags}.
     * A defensive copy of {@code tags} is used internally.
     */
    public void setTags(Set<Tag> tags) {
        this.tags = (tags != null) ? new HashSet<>(tags) : null;
    }

    /**
     * Returns an unmodifiable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     * Returns {@code Optional#empty()} if {@code tags} is null.
     */
    public Optional<Set<Tag>> getTags() {
        return (tags != null) ? Optional.of(Collections.unmodifiableSet(tags)) : Optional.empty();
    }

    /**
     * Creates and returns a {@code Expense} with the details of {@code expenseToEdit}
     * edited with {@code editExpenseDescriptor}.
     */
    public static Expense createEditedExpense(Expense expenseToEdit, EditExpenseDescriptor editExpenseDescriptor) {
        assert expenseToEdit != null;

        Name updatedName = editExpenseDescriptor.getName().orElse(expenseToEdit.getName());
        Category updatedCategory = editExpenseDescriptor.getCategory().orElse(expenseToEdit.getCategory());
        Cost updatedCost = editExpenseDescriptor.getCost().orElse(expenseToEdit.getCost());
        Set<Tag> updatedTags = editExpenseDescriptor.getTags().orElse(expenseToEdit.getTags());
        Date updatedDate = editExpenseDescriptor.getDate().orElse(expenseToEdit.getDate());

        return new Expense(updatedName, updatedCategory, updatedCost, updatedDate, updatedTags);
    }

    /**
     * Create {@code EditExpenseDescriptor} based on the input {@code ArgumentMultimap}
     * @throws ParseException if all attributes in the {@code editExpenseDescriptor} are null.
     * */
    public static EditExpenseDescriptor createEditExpenseDescriptor(ArgumentMultimap argMultimap)
            throws ParseException {
        EditExpenseDescriptor editExpenseDescriptor = new EditExpenseDescriptor();
        if (argMultimap.getValue(PREFIX_NAME).isPresent()) {
            editExpenseDescriptor.setName(ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get()));
        }
        if (argMultimap.getValue(PREFIX_CATEGORY).isPresent()) {
            editExpenseDescriptor.setCategory(ParserUtil.parseCategory(argMultimap.getValue(PREFIX_CATEGORY).get()));
        }
        if (argMultimap.getValue(PREFIX_COST).isPresent()) {
            editExpenseDescriptor.setCost(ParserUtil.parseCost(argMultimap.getValue(PREFIX_COST).get()));
        }
        if (argMultimap.getValue(PREFIX_DATE).isPresent()) {
            editExpenseDescriptor.setDate(ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get()));
        }
        parseTagsForEdit(argMultimap.getAllValues(PREFIX_TAG)).ifPresent(editExpenseDescriptor::setTags);

        if (!editExpenseDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditCommand.MESSAGE_NOT_EDITED);
        }
        return editExpenseDescriptor;
    }

    /**
     * Parses {@code Collection<String> tags} into a {@code Set<Tag>} if {@code tags} is non-empty.
     * If {@code tags} contain only one element which is an empty string, it will be parsed into a
     * {@code Set<Tag>} containing zero tags.
     */
    private static Optional<Set<Tag>> parseTagsForEdit(Collection<String> tags) throws ParseException {
        assert tags != null;

        if (tags.isEmpty()) {
            return Optional.empty();
        }
        Collection<String> tagSet = tags.size() == 1 && tags.contains("") ? Collections.emptySet() : tags;
        return Optional.of(ParserUtil.parseTags(tagSet));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditExpenseDescriptor)) {
            return false;
        }

        // state check
        EditExpenseDescriptor e = (EditExpenseDescriptor) other;

        return getName().equals(e.getName())
                && getCategory().equals(e.getCategory())
                && getCost().equals(e.getCost())
                && getTags().equals(e.getTags())
                && getDate().equals(e.getDate());
    }
}
