package seedu.address.model.encryption;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.expense.Expense;
import seedu.address.model.tag.Tag;

//@@author JasonChong96
/**
 * Represents an expense in Expense Tracker in it's encrypted form.
 * Guarantees: immutable;
 */
public class EncryptedExpense {
    // Identity fields
    private final EncryptedName name;
    private final EncryptedDate date;
    private final EncryptedCategory category;

    // Data fields
    private final EncryptedCost cost;
    private final Set<EncryptedTag> tags = new HashSet<>();

    /**
     * Every field must be present and not null.
     */

    public EncryptedExpense(EncryptedName name, EncryptedCategory category, EncryptedCost cost, EncryptedDate date,
                            Set<EncryptedTag> tags) {
        requireAllNonNull(name, category, cost, date, tags);
        this.name = name;
        this.category = category;
        this.cost = cost;
        this.date = date;
        this.tags.addAll(tags);
    }

    public Expense getDecryptedExpense(String key) throws IllegalValueException {
        Set<Tag> decryptedTags = new HashSet<>();
        for (EncryptedTag tag : tags) {
            decryptedTags.add(tag.getDecrypted(key));
        }
        return new Expense(name.getDecrypted(key), category.getDecrypted(key), cost.getDecrypted(key),
                date.getDecrypted(key), decryptedTags);
    }

    public EncryptedName getName() {
        return name;
    }

    public EncryptedDate getDate() {
        return date;
    }

    public EncryptedCategory getCategory() {
        return category;
    }

    public EncryptedCost getCost() {
        return cost;
    }

    /**
     * Returns an immutable tag set, which throws {@code UnsupportedOperationException}
     * if modification is attempted.
     * @return an immutable tag set
     */
    public Set<EncryptedTag> getTags() {
        return Collections.unmodifiableSet(tags);
    }

    /**
     * Returns true if both expenses of the same name have at least one other identity field that is the same.
     * This defines a weaker notion of equality between two expenses.
     */
    public boolean isSameExpense(EncryptedExpense otherExpense) {
        if (otherExpense == this) {
            return true;
        }

        return otherExpense != null
                && otherExpense.name.equals(this.name)
                && (otherExpense.category.equals(category)
                || otherExpense.cost.equals(cost));
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

        if (!(other instanceof EncryptedExpense)) {
            return false;
        }

        EncryptedExpense otherExpense = (EncryptedExpense) other;
        return otherExpense.getName().equals(getName())
                && otherExpense.getCategory().equals(getCategory())
                && otherExpense.getCost().equals(getCost())
                && otherExpense.getDate().equals(getDate())
                && otherExpense.getTags().equals(getTags());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, category, cost, date, tags);
    }
}
