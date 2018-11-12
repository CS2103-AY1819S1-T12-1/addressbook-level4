package seedu.expensetracker.model.encryption;

import static java.util.Objects.requireNonNull;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

//@@author JasonChong96
/**
 * A list of encrypted expenses that allows for duplication but does not allow null.
 * Supports a minimal set of list operations.
 *
 * @see EncryptedExpense#isSameExpense(EncryptedExpense)
 */
public class EncryptedExpenseList implements Iterable<EncryptedExpense> {

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
     */
    public void add(EncryptedExpense toAdd) {
        requireNonNull(toAdd);
        internalList.add(toAdd);
    }

    @Override
    public Iterator<EncryptedExpense> iterator() {
        return internalList.iterator();
    }

    /**
     * Returns a stream of EncryptedExpenses contained within this EncryptedExpenseList.
     * @return a stream of EncryptedExpenses contained within this EncryptedExpenseList.
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
