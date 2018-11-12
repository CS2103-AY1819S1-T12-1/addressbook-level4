package seedu.expensetracker.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static seedu.expensetracker.testutil.TypicalExpenses.GAME;
import static seedu.expensetracker.testutil.TypicalExpenses.IPHONE;
import static seedu.expensetracker.testutil.TypicalExpenses.TOY;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import seedu.expensetracker.testutil.ExpenseTrackerBuilder;

public class VersionedExpenseTrackerTest {

    private final ReadOnlyExpenseTracker expenseTrackerWithGame = new ExpenseTrackerBuilder().withExpense(GAME).build();
    private final ReadOnlyExpenseTracker expenseTrackerWithiPhone =
            new ExpenseTrackerBuilder().withExpense(IPHONE).build();
    private final ReadOnlyExpenseTracker expenseTrackerWithToy = new ExpenseTrackerBuilder().withExpense(TOY).build();
    private final ReadOnlyExpenseTracker emptyExpenseTracker = new ExpenseTrackerBuilder().build();

    @Test
    public void commit_singleExpenseTracker_noStatesRemovedCurrentStateSaved() {
        VersionedExpenseTracker versionedExpenseTracker = prepareExpenseTrackerList(emptyExpenseTracker);

        versionedExpenseTracker.commit();
        assertExpenseTrackerListStatus(versionedExpenseTracker,
                Collections.singletonList(emptyExpenseTracker),
                emptyExpenseTracker,
                Collections.emptyList());
    }

    @Test
    public void commit_multipleExpenseTrackerPointerAtEndOfStateList_noStatesRemovedCurrentStateSaved() {
        VersionedExpenseTracker versionedExpenseTracker = prepareExpenseTrackerList(
                emptyExpenseTracker, expenseTrackerWithGame, expenseTrackerWithiPhone);

        versionedExpenseTracker.commit();
        assertExpenseTrackerListStatus(versionedExpenseTracker,
                Arrays.asList(emptyExpenseTracker, expenseTrackerWithGame, expenseTrackerWithiPhone),
                expenseTrackerWithiPhone,
                Collections.emptyList());
    }

    @Test
    public void commit_multipleExpenseTrackerPointerNotAtEndOfStateList_statesAfterPointerRemovedCurrentStateSaved() {
        VersionedExpenseTracker versionedExpenseTracker = prepareExpenseTrackerList(
                emptyExpenseTracker, expenseTrackerWithGame, expenseTrackerWithiPhone);
        shiftCurrentStatePointerLeftwards(versionedExpenseTracker, 2);

        versionedExpenseTracker.commit();
        assertExpenseTrackerListStatus(versionedExpenseTracker,
                Collections.singletonList(emptyExpenseTracker),
                emptyExpenseTracker,
                Collections.emptyList());
    }

    @Test
    public void canUndo_multipleExpenseTrackerPointerAtEndOfStateList_returnsTrue() {
        VersionedExpenseTracker versionedExpenseTracker = prepareExpenseTrackerList(
                emptyExpenseTracker, expenseTrackerWithGame, expenseTrackerWithiPhone);

        assertTrue(versionedExpenseTracker.canUndo());
    }

    @Test
    public void canUndo_multipleExpenseTrackerPointerAtStartOfStateList_returnsTrue() {
        VersionedExpenseTracker versionedExpenseTracker = prepareExpenseTrackerList(
                emptyExpenseTracker, expenseTrackerWithGame, expenseTrackerWithiPhone);
        shiftCurrentStatePointerLeftwards(versionedExpenseTracker, 1);

        assertTrue(versionedExpenseTracker.canUndo());
    }

    @Test
    public void canUndo_singleExpenseTracker_returnsFalse() {
        VersionedExpenseTracker versionedExpenseTracker = prepareExpenseTrackerList(emptyExpenseTracker);

        assertFalse(versionedExpenseTracker.canUndo());
    }

    @Test
    public void canUndo_multipleExpenseTrackerPointerAtStartOfStateList_returnsFalse() {
        VersionedExpenseTracker versionedExpenseTracker = prepareExpenseTrackerList(
                emptyExpenseTracker, expenseTrackerWithGame, expenseTrackerWithiPhone);
        shiftCurrentStatePointerLeftwards(versionedExpenseTracker, 2);

        assertFalse(versionedExpenseTracker.canUndo());
    }

    @Test
    public void canRedo_multipleExpenseTrackerPointerNotAtEndOfStateList_returnsTrue() {
        VersionedExpenseTracker versionedExpenseTracker = prepareExpenseTrackerList(
                emptyExpenseTracker, expenseTrackerWithGame, expenseTrackerWithiPhone);
        shiftCurrentStatePointerLeftwards(versionedExpenseTracker, 1);

        assertTrue(versionedExpenseTracker.canRedo());
    }

    @Test
    public void canRedo_multipleExpenseTrackerPointerAtStartOfStateList_returnsTrue() {
        VersionedExpenseTracker versionedExpenseTracker = prepareExpenseTrackerList(
                emptyExpenseTracker, expenseTrackerWithGame, expenseTrackerWithiPhone);
        shiftCurrentStatePointerLeftwards(versionedExpenseTracker, 2);

        assertTrue(versionedExpenseTracker.canRedo());
    }

    @Test
    public void canRedo_singleExpenseTracker_returnsFalse() {
        VersionedExpenseTracker versionedExpenseTracker = prepareExpenseTrackerList(emptyExpenseTracker);

        assertFalse(versionedExpenseTracker.canRedo());
    }

    @Test
    public void canRedo_multipleExpenseTrackerPointerAtEndOfStateList_returnsFalse() {
        VersionedExpenseTracker versionedExpenseTracker = prepareExpenseTrackerList(
                emptyExpenseTracker, expenseTrackerWithGame, expenseTrackerWithiPhone);

        assertFalse(versionedExpenseTracker.canRedo());
    }

    @Test
    public void undo_multipleExpenseTrackerPointerAtEndOfStateList_success() {
        VersionedExpenseTracker versionedExpenseTracker = prepareExpenseTrackerList(
                emptyExpenseTracker, expenseTrackerWithGame, expenseTrackerWithiPhone);

        versionedExpenseTracker.undo();
        assertExpenseTrackerListStatus(versionedExpenseTracker,
                Collections.singletonList(emptyExpenseTracker),
                expenseTrackerWithGame,
                Collections.singletonList(expenseTrackerWithiPhone));
    }

    @Test
    public void undo_multipleExpenseTrackerPointerNotAtStartOfStateList_success() {
        VersionedExpenseTracker versionedExpenseTracker = prepareExpenseTrackerList(
                emptyExpenseTracker, expenseTrackerWithGame, expenseTrackerWithiPhone);
        shiftCurrentStatePointerLeftwards(versionedExpenseTracker, 1);

        versionedExpenseTracker.undo();
        assertExpenseTrackerListStatus(versionedExpenseTracker,
                Collections.emptyList(),
                emptyExpenseTracker,
                Arrays.asList(expenseTrackerWithGame, expenseTrackerWithiPhone));
    }

    @Test
    public void undo_singleExpenseTracker_throwsNoUndoableStateException() {
        VersionedExpenseTracker versionedExpenseTracker = prepareExpenseTrackerList(emptyExpenseTracker);

        assertThrows(VersionedExpenseTracker.NoUndoableStateException.class, versionedExpenseTracker::undo);
    }

    @Test
    public void undo_multipleExpenseTrackerPointerAtStartOfStateList_throwsNoUndoableStateException() {
        VersionedExpenseTracker versionedExpenseTracker = prepareExpenseTrackerList(
                emptyExpenseTracker, expenseTrackerWithGame, expenseTrackerWithiPhone);
        shiftCurrentStatePointerLeftwards(versionedExpenseTracker, 2);

        assertThrows(VersionedExpenseTracker.NoUndoableStateException.class, versionedExpenseTracker::undo);
    }

    @Test
    public void redo_multipleExpenseTrackerPointerNotAtEndOfStateList_success() {
        VersionedExpenseTracker versionedExpenseTracker = prepareExpenseTrackerList(
                emptyExpenseTracker, expenseTrackerWithGame, expenseTrackerWithiPhone);
        shiftCurrentStatePointerLeftwards(versionedExpenseTracker, 1);

        versionedExpenseTracker.redo();
        assertExpenseTrackerListStatus(versionedExpenseTracker,
                Arrays.asList(emptyExpenseTracker, expenseTrackerWithGame),
                expenseTrackerWithiPhone,
                Collections.emptyList());
    }

    @Test
    public void redo_multipleExpenseTrackerPointerAtStartOfStateList_success() {
        VersionedExpenseTracker versionedExpenseTracker = prepareExpenseTrackerList(
                emptyExpenseTracker, expenseTrackerWithGame, expenseTrackerWithiPhone);
        shiftCurrentStatePointerLeftwards(versionedExpenseTracker, 2);

        versionedExpenseTracker.redo();
        assertExpenseTrackerListStatus(versionedExpenseTracker,
                Collections.singletonList(emptyExpenseTracker),
                expenseTrackerWithGame,
                Collections.singletonList(expenseTrackerWithiPhone));
    }

    @Test
    public void redo_singleExpenseTracker_throwsNoRedoableStateException() {
        VersionedExpenseTracker versionedExpenseTracker = prepareExpenseTrackerList(emptyExpenseTracker);

        assertThrows(VersionedExpenseTracker.NoRedoableStateException.class, versionedExpenseTracker::redo);
    }

    @Test
    public void redo_multipleExpenseTrackerPointerAtEndOfStateList_throwsNoRedoableStateException() {
        VersionedExpenseTracker versionedExpenseTracker = prepareExpenseTrackerList(
                emptyExpenseTracker, expenseTrackerWithGame, expenseTrackerWithiPhone);

        assertThrows(VersionedExpenseTracker.NoRedoableStateException.class, versionedExpenseTracker::redo);
    }

    @Test
    public void equals() {
        VersionedExpenseTracker versionedExpenseTracker =
                prepareExpenseTrackerList(expenseTrackerWithGame, expenseTrackerWithiPhone);

        // same values -> returns true
        VersionedExpenseTracker copy = prepareExpenseTrackerList(expenseTrackerWithGame, expenseTrackerWithiPhone);
        assertTrue(versionedExpenseTracker.equals(copy));

        // same object -> returns true
        assertTrue(versionedExpenseTracker.equals(versionedExpenseTracker));

        // null -> returns false
        assertFalse(versionedExpenseTracker.equals(null));

        // different types -> returns false
        assertFalse(versionedExpenseTracker.equals(1));

        // different state list -> returns false
        VersionedExpenseTracker differentExpenseTrackerList =
                prepareExpenseTrackerList(expenseTrackerWithiPhone, expenseTrackerWithToy);
        assertFalse(versionedExpenseTracker.equals(differentExpenseTrackerList));

        // different current pointer index -> returns false
        VersionedExpenseTracker differentCurrentStatePointer = prepareExpenseTrackerList(
                expenseTrackerWithGame, expenseTrackerWithiPhone);
        shiftCurrentStatePointerLeftwards(versionedExpenseTracker, 1);
        assertFalse(versionedExpenseTracker.equals(differentCurrentStatePointer));
    }

    /**
     * Asserts that {@code versionedExpenseTracker} is currently pointing at {@code expectedCurrentState},
     * states before {@code versionedExpenseTracker#currentStatePointer}
     * is equal to {@code expectedStatesBeforePointer},
     * and states after {@code versionedExpenseTracker#currentStatePointer}
     * is equal to {@code expectedStatesAfterPointer}.
     */
    private void assertExpenseTrackerListStatus(VersionedExpenseTracker versionedExpenseTracker,
                                                List<ReadOnlyExpenseTracker> expectedStatesBeforePointer,
                                                ReadOnlyExpenseTracker expectedCurrentState,
                                                List<ReadOnlyExpenseTracker> expectedStatesAfterPointer) {
        // check state currently pointing at is correct
        assertEquals(new ExpenseTracker(versionedExpenseTracker), expectedCurrentState);

        // shift pointer to start of state list
        while (versionedExpenseTracker.canUndo()) {
            versionedExpenseTracker.undo();
        }

        // check states before pointer are correct
        for (ReadOnlyExpenseTracker expectedExpenseTracker : expectedStatesBeforePointer) {
            assertEquals(expectedExpenseTracker, new ExpenseTracker(versionedExpenseTracker));
            versionedExpenseTracker.redo();
        }

        // check states after pointer are correct
        for (ReadOnlyExpenseTracker expectedExpenseTracker : expectedStatesAfterPointer) {
            versionedExpenseTracker.redo();
            assertEquals(expectedExpenseTracker, new ExpenseTracker(versionedExpenseTracker));
        }

        // check that there are no more states after pointer
        assertFalse(versionedExpenseTracker.canRedo());

        // revert pointer to original position
        expectedStatesAfterPointer.forEach(unused -> versionedExpenseTracker.undo());
    }

    /**
     * Creates and returns a {@code VersionedExpenseTracker}
     * with the {@code expenseTrackerStates} added into it, and the
     * {@code VersionedExpenseTracker#currentStatePointer} at the end of list.
     */
    private VersionedExpenseTracker prepareExpenseTrackerList(ReadOnlyExpenseTracker... expenseTrackerStates) {
        assertFalse(expenseTrackerStates.length == 0);

        VersionedExpenseTracker versionedExpenseTracker = new VersionedExpenseTracker(expenseTrackerStates[0]);
        for (int i = 1; i < expenseTrackerStates.length; i++) {
            versionedExpenseTracker.resetData(expenseTrackerStates[i]);
            versionedExpenseTracker.commit();
        }

        return versionedExpenseTracker;
    }

    /**
     * Shifts the {@code versionedExpenseTracker#currentStatePointer} by {@code count} to the left of its list.
     */
    private void shiftCurrentStatePointerLeftwards(VersionedExpenseTracker versionedExpenseTracker, int count) {
        for (int i = 0; i < count; i++) {
            versionedExpenseTracker.undo();
        }
    }
}
