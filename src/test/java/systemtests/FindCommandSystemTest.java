package systemtests;

import static org.junit.Assert.assertFalse;
import static seedu.address.commons.core.Messages.MESSAGE_EXPENSES_LISTED_OVERVIEW;
import static seedu.address.commons.core.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.testutil.TypicalExpenses.BENSON;
import static seedu.address.testutil.TypicalExpenses.CARL;
import static seedu.address.testutil.TypicalExpenses.DANIEL;
import static seedu.address.testutil.TypicalExpenses.KEYWORD_MATCHING_MEIER;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.RedoCommand;
import seedu.address.logic.commands.UndoCommand;
import seedu.address.model.Model;
import seedu.address.model.exceptions.NoUserSelectedException;
import seedu.address.model.tag.Tag;

public class FindCommandSystemTest extends AddressBookSystemTest {

    @Test
    public void find() throws NoUserSelectedException {
        /* Case: find multiple expenses in address book, command with leading spaces and trailing spaces
         * -> 2 expenses found
         */
        String command = "   " + FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER + "   ";
        Model expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, BENSON, DANIEL); // first names of Benson and Daniel are "Meier"
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: repeat previous find command where expense list is displaying the expenses we are finding
         * -> 2 expenses found
         */
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find expense where expense list is not displaying the expense we are finding -> 1 expense found */
        command = FindCommand.COMMAND_WORD + " Carl";
        ModelHelper.setFilteredList(expectedModel, CARL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple expenses in address book, 2 keywords -> 2 expenses found */
        command = FindCommand.COMMAND_WORD + " Benson Daniel";
        ModelHelper.setFilteredList(expectedModel, BENSON, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple expenses in address book, 2 keywords in reversed order -> 2 expenses found */
        command = FindCommand.COMMAND_WORD + " Daniel Benson";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple expenses in address book, 2 keywords with 1 repeat -> 2 expenses found */
        command = FindCommand.COMMAND_WORD + " Daniel Benson Daniel";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find multiple expenses in address book, 2 matching keywords and 1 non-matching keyword
         * -> 2 expenses found
         */
        command = FindCommand.COMMAND_WORD + " Daniel Benson NonMatchingKeyWord";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: undo previous find command -> rejected */
        command = UndoCommand.COMMAND_WORD;
        String expectedResultMessage = UndoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: redo previous find command -> rejected */
        command = RedoCommand.COMMAND_WORD;
        expectedResultMessage = RedoCommand.MESSAGE_FAILURE;
        assertCommandFailure(command, expectedResultMessage);

        /* Case: find same expenses in address book after deleting 1 of them -> 1 expense found */
        executeCommand(DeleteCommand.COMMAND_WORD + " 1");
        assertFalse(getModel().getAddressBook().getExpenseList().contains(BENSON));
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER;
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find expense in address book, keyword is same as name but of different case -> 1 expense found */
        command = FindCommand.COMMAND_WORD + " MeIeR";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find expense in address book, keyword is substring of name -> 0 expenses found */
        command = FindCommand.COMMAND_WORD + " Mei";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find expense in address book, name is substring of keyword -> 0 expenses found */
        command = FindCommand.COMMAND_WORD + " Meiers";
        ModelHelper.setFilteredList(expectedModel);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find expense not in address book -> 0 expenses found */
        command = FindCommand.COMMAND_WORD + " Mark";
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find category name of expense in address book -> 0 expenses found */
        command = FindCommand.COMMAND_WORD + " " + DANIEL.getCategory().getName();
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find address of expense in address book -> 0 expenses found */
        command = FindCommand.COMMAND_WORD + " " + DANIEL.getCost().value;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find tags of expense in address book -> 0 expenses found */
        List<Tag> tags = new ArrayList<>(DANIEL.getTags());
        command = FindCommand.COMMAND_WORD + " " + tags.get(0).tagName;
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: find while a expense is selected -> selected card deselected */
        showAllExpenses();
        selectExpense(Index.fromOneBased(1));
        assertFalse(getExpenseListPanel().getHandleToSelectedCard().getName().equals(DANIEL.getName().expenseName));
        command = FindCommand.COMMAND_WORD + " Daniel";
        ModelHelper.setFilteredList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardDeselected();

        /* Case: find expense in empty address book -> 0 expenses found */
        deleteAllExpenses();
        command = FindCommand.COMMAND_WORD + " " + KEYWORD_MATCHING_MEIER;
        expectedModel = getModel();
        ModelHelper.setFilteredList(expectedModel, DANIEL);
        assertCommandSuccess(command, expectedModel);
        assertSelectedCardUnchanged();

        /* Case: mixed case command word -> rejected */
        command = "FiNd Meier";
        assertCommandFailure(command, MESSAGE_UNKNOWN_COMMAND);
    }

    /**
     * Executes {@code command} and verifies that the command box displays an empty string, the result display
     * box displays {@code Messages#MESSAGE_EXPENSES_LISTED_OVERVIEW} with the number of people in the filtered list,
     * and the model related components equal to {@code expectedModel}.
     * These verifications are done by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the status bar remains unchanged, and the command box has the default style class, and the
     * selected card updated accordingly, depending on {@code cardStatus}.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandSuccess(String command, Model expectedModel) throws NoUserSelectedException {
        String expectedResultMessage = String.format(
                MESSAGE_EXPENSES_LISTED_OVERVIEW, expectedModel.getFilteredExpenseList().size());

        executeCommand(command);
        assertApplicationDisplaysExpected("", expectedResultMessage, expectedModel);
        assertCommandBoxShowsDefaultStyle();
        assertStatusBarUnchanged();
    }

    /**
     * Executes {@code command} and verifies that the command box displays {@code command}, the result display
     * box displays {@code expectedResultMessage} and the model related components equal to the current model.
     * These verifications are done by
     * {@code AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)}.<br>
     * Also verifies that the browser url, selected card and status bar remain unchanged, and the command box has the
     * error style.
     * @see AddressBookSystemTest#assertApplicationDisplaysExpected(String, String, Model)
     */
    private void assertCommandFailure(String command, String expectedResultMessage) throws NoUserSelectedException {
        Model expectedModel = getModel();

        executeCommand(command);
        assertApplicationDisplaysExpected(command, expectedResultMessage, expectedModel);
        assertSelectedCardUnchanged();
        assertCommandBoxShowsErrorStyle();
        assertStatusBarUnchanged();
    }
}
