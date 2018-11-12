package seedu.expensetracker.model.expense;

import static seedu.expensetracker.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.expensetracker.model.tag.Tag;

/**
 * Represents a Expense in the expense tracker.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Expense {

    // Identity fields
    private final Name name;
    private final Date date;
    private final Category category;

    // Data fields
    private final Cost cost;
    private final Set<Tag> tags = new HashSet<>();

    /**
     * Every field must be present and not null.
     */

    public Expense(Name name, Category category, Cost cost, Date date, Set<Tag> tags) {
        requireAllNonNull(name, category, cost, date, tags);
        this.name = name;
        this.category = category;
        this.cost = cost;
        this.date = date;
        this.tags.addAll(tags);
    }

    /**
     * Every field must be present and not null.
     */
    public Expense(Name name, Category category, Cost cost, Set<Tag> tags) {
        this(name, category, cost, new Date(), tags);
    }

    public Name getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    public Cost getCost() {
        return cost;
    }

    public Date getDate() {
        return date;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     */
    public Set<Tag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both expenses of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two expenses.
     */
    public boolean isSameExpense(Expense otherExpense) {

        if (otherExpense == this) {
            return true;
        }

        return otherExpense != null
                && otherExpense.getName().equals(this.getName())
                && (otherExpense.getCategory().equals(this.getCategory())
                || otherExpense.getCost().equals(this.getCost()));
    }

    /**
     * Returns true if both expenses have the same identity and data fields.
     * This defines a stronger notion of equality between two expenses.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (!(other instanceof Expense)) {
            return false;
        }

        Expense otherExpense = (Expense) other;
        return otherExpense.getName().equals(getName())
                && otherExpense.getCategory().equals(getCategory())
                && otherExpense.getCost().equals(getCost())
                && otherExpense.getDate().equals(getDate())
                && otherExpense.getTags().equals(getTags());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, category, date, cost, tags);
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getName())
                .append(" Category: ")
                .append(getCategory())
                .append(" Cost: ")
                .append(getCost())
                .append(" Date: ")
                .append(getDate())
                .append(" Tags: ");
        getTags().forEach(builder::append);
        return builder.toString();
    }

}
