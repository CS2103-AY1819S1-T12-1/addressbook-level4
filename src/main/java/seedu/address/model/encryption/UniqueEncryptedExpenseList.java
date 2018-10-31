package seedu.address.model.encryption;

import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import seedu.address.model.expense.exceptions.DuplicateExpenseException;

//@@author JasonChong96
/**
 * A list of encrypted expenses that enforces uniqueness between its elements and does not allow nulls.
 * An encrypted expense is considered unique by comparing using {EncryptedExpense#isSameExpense(EncryptedExpense)}.
 * As such, adding and updating of expenses uses EncryptedExpense#isSameExpense(EncryptedExpense)
 * for equality so as to ensure that the expense being added or updated is
 * unique in terms of identity in the UniqueEncryptedExpenseList.
 * However, the removal of a expense uses EncryptedExpense#equals(Object) so as to ensure that the expense
 * with exactly the same fields will be removed.
 *
 * Supports a minimal set of list operations.
 *
 * @see EncryptedExpense#isSameExpense(EncryptedExpense)
 */
public class UniqueEncryptedExpenseList implements Iterable<EncryptedExpense> {

    private final List<EncryptedExpense> internalList = new LinkedList<>();

    /**
     * Returns true if the list contains an equivalent expense as the given argument.
     */
    public boolean contains(EncryptedExpense toCheck) {
        requireNonNull(toCheck);
        return internalList.stream().anyMatch(toCheck::isSameExpense);
    }

    /**
     * Adds a expense to the list.
     * The expense must not already exist in the list.
     */
    public void add(EncryptedExpense toAdd) {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateExpenseException();
        }
        internalList.add(toAdd);
    }

    @Override
    public Iterator<EncryptedExpense> iterator() {
        return internalList.iterator();
    }

    /**
     * Returns a stream of EncryptedExpenses contained within this UniqueEncryptedExpenseList.
     * @return a stream of EncryptedExpenses contained within this UniqueEncryptedExpenseList.
     */
    public Stream<EncryptedExpense> stream() {
        return internalList.stream();
    }

    /**
     * Gets the size of the list as an int.
     * @return the size of this list as an int
     */
    public int size() {
        return internalList.size();
    }
}
