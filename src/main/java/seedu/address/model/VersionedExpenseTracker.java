package seedu.address.model;

import java.util.ArrayList;
import java.util.List;

/**
 * {@code ExpenseTracker} that keeps track of its own history.
 */
public class VersionedExpenseTracker extends ExpenseTracker {

    private final List<ReadOnlyExpenseTracker> expenseTrackerStateList;
    private int currentStatePointer;

    public VersionedExpenseTracker(ReadOnlyExpenseTracker initialState) {
        super(initialState);

        expenseTrackerStateList = new ArrayList<>();
        expenseTrackerStateList.add(new ExpenseTracker(initialState));
        currentStatePointer = 0;
    }

    /**
     * Saves a copy of the current {@code ExpenseTracker} state at the end of the state list.
     * Undone states are removed from the state list.
     */
    public void commit() {
        removeStatesAfterCurrentPointer();

        expenseTrackerStateList.add(new ExpenseTracker(this));
        currentStatePointer++;
    }

    private void removeStatesAfterCurrentPointer() {
        expenseTrackerStateList.subList(currentStatePointer + 1, expenseTrackerStateList.size()).clear();
    }

    /**
     * Restores the expense tracker to its previous state.
     */
    public void undo() {
        if (!canUndo()) {
            throw new NoUndoableStateException();
        }
        currentStatePointer--;
        resetData(expenseTrackerStateList.get(currentStatePointer));
    }

    /**
     * Restores the expense tracker to its previously undone state.
     */
    public void redo() {
        if (!canRedo()) {
            throw new NoRedoableStateException();
        }
        currentStatePointer++;
        resetData(expenseTrackerStateList.get(currentStatePointer));
    }

    /**
     * Returns true if {@code undo()} has expense tracker states to undo.
     */
    public boolean canUndo() {
        return currentStatePointer > 0;
    }

    /**
     * Returns true if {@code redo()} has expense tracker states to redo.
     */
    public boolean canRedo() {
        return currentStatePointer < expenseTrackerStateList.size() - 1;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof VersionedExpenseTracker)) {
            return false;
        }

        VersionedExpenseTracker otherVersionedExpenseTracker = (VersionedExpenseTracker) other;
        System.out.println(expenseTrackerStateList.equals(otherVersionedExpenseTracker.expenseTrackerStateList));
        // state check
        return super.equals(otherVersionedExpenseTracker)
                && expenseTrackerStateList.equals(otherVersionedExpenseTracker.expenseTrackerStateList)
                && currentStatePointer == otherVersionedExpenseTracker.currentStatePointer;
    }

    /**
     * Thrown when trying to {@code undo()} but can't.
     */
    public static class NoUndoableStateException extends RuntimeException {
        private NoUndoableStateException() {
            super("Current state pointer at start of expenseTrackerState list, unable to undo.");
        }
    }

    /**
     * Thrown when trying to {@code redo()} but can't.
     */
    public static class NoRedoableStateException extends RuntimeException {
        private NoRedoableStateException() {
            super("Current state pointer at end of expenseTrackerState list, unable to redo.");
        }
    }
}
