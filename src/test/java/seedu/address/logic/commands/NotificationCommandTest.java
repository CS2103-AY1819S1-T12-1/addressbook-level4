package seedu.address.logic.commands;

import static junit.framework.TestCase.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.ModelUtil.getTypicalModel;
import static seedu.address.testutil.TypicalExpenses.getTypicalExpenseTracker;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.commands.NotificationCommand.NotificationCommandDescriptor;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.exceptions.NoUserSelectedException;


//@@author Snookerballs

public class NotificationCommandTest {
    private Model model = new ModelManager(getTypicalExpenseTracker(), new UserPrefs(), null);
    private CommandHistory commandHistory = new CommandHistory();

    /**
     * Initializes the model manager and command history after each test
     */
    @BeforeEach
    public void resetModelsAndCommandHistory() {
        this.model = getTypicalModel();
        this.commandHistory = new CommandHistory();
    }

    @Test
    public void execute_toggleBoth_successful() throws NoUserSelectedException, CommandException {
        Model expectedModel = new ModelManager(model.getExpenseTracker(), new UserPrefs(), null);
        expectedModel.toggleBothNotification(false);
        expectedModel.commitExpenseTracker();

        NotificationCommandDescriptor descriptor = new NotificationCommandDescriptor();
        String expectedMessage = NotificationCommand.MESSAGE_SUCCESS;

        // Toggle off test
        descriptor.setToggle(NotificationCommand.OPTION_OFF);
        NotificationCommand notificationCommand = new NotificationCommand(descriptor);
        assertCommandSuccess(notificationCommand, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(model.getNotificationHandler().isTipEnabled(),
                expectedModel.getNotificationHandler().isTipEnabled());
        assertEquals(model.getNotificationHandler().isWarningEnabled(),
                expectedModel.getNotificationHandler().isWarningEnabled());

        assertFalse(model.getNotificationHandler().isTipEnabled());
        assertFalse(model.getNotificationHandler().isWarningEnabled());

        //Toggle on test
        expectedModel.toggleBothNotification(true);
        expectedModel.commitExpenseTracker();
        descriptor = new NotificationCommandDescriptor();
        descriptor.setToggle(NotificationCommand.OPTION_ON);
        notificationCommand = new NotificationCommand(descriptor);

        assertCommandSuccess(notificationCommand, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(model.getNotificationHandler().isTipEnabled(),
                expectedModel.getNotificationHandler().isTipEnabled());
        assertEquals(model.getNotificationHandler().isWarningEnabled(),
                expectedModel.getNotificationHandler().isWarningEnabled());
        assertTrue(model.getNotificationHandler().isTipEnabled());
        assertTrue(model.getNotificationHandler().isWarningEnabled());
    }

    @Test
    public void execute_toggleTip_successful() throws NoUserSelectedException {
        NotificationCommandDescriptor descriptor = new NotificationCommandDescriptor();
        String expectedMessage = NotificationCommand.MESSAGE_SUCCESS;

        Model expectedModel = new ModelManager(model.getExpenseTracker(), new UserPrefs(), null);
        expectedModel.toggleTipNotification(false);
        expectedModel.commitExpenseTracker();

        // Toggle off test
        descriptor.setToggle(NotificationCommand.OPTION_OFF);
        descriptor.setNotificationType(NotificationCommand.OPTION_TIP);
        NotificationCommand notificationCommand = new NotificationCommand(descriptor);

        assertCommandSuccess(notificationCommand, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(model.getNotificationHandler().isTipEnabled(),
                expectedModel.getNotificationHandler().isTipEnabled());

        assertFalse(model.getNotificationHandler().isTipEnabled());
        assertTrue(model.getNotificationHandler().isWarningEnabled());

        // Toggle off test
        expectedModel.toggleTipNotification(false);
        expectedModel.commitExpenseTracker();

        descriptor.setToggle(NotificationCommand.OPTION_ON);
        descriptor.setNotificationType(NotificationCommand.OPTION_TIP);
        notificationCommand = new NotificationCommand(descriptor);

        assertCommandSuccess(notificationCommand, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(model.getNotificationHandler().isTipEnabled(),
                expectedModel.getNotificationHandler().isTipEnabled());

        assertTrue(model.getNotificationHandler().isTipEnabled());
        assertTrue(model.getNotificationHandler().isWarningEnabled());
    }

    @Test
    public void execute_toggleWarning_successful() throws NoUserSelectedException {
        NotificationCommandDescriptor descriptor = new NotificationCommandDescriptor();
        String expectedMessage = NotificationCommand.MESSAGE_SUCCESS;

        Model expectedModel = new ModelManager(model.getExpenseTracker(), new UserPrefs(), null);
        expectedModel.toggleWarningNotification(false);
        expectedModel.commitExpenseTracker();

        // Toggle off test
        descriptor.setToggle(NotificationCommand.OPTION_OFF);
        descriptor.setNotificationType(NotificationCommand.OPTION_WARNING);
        NotificationCommand notificationCommand = new NotificationCommand(descriptor);

        assertCommandSuccess(notificationCommand, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(model.getNotificationHandler().isWarningEnabled(),
                expectedModel.getNotificationHandler().isWarningEnabled());

        assertTrue(model.getNotificationHandler().isTipEnabled());
        assertFalse(model.getNotificationHandler().isWarningEnabled());

        // Toggle off test
        expectedModel.toggleWarningNotification(false);
        expectedModel.commitExpenseTracker();

        descriptor.setToggle(NotificationCommand.OPTION_ON);
        notificationCommand = new NotificationCommand(descriptor);

        assertCommandSuccess(notificationCommand, model, commandHistory, expectedMessage, expectedModel);
        assertEquals(model.getNotificationHandler().isWarningEnabled(),
                expectedModel.getNotificationHandler().isWarningEnabled());

        assertTrue(model.getNotificationHandler().isTipEnabled());
        assertTrue(model.getNotificationHandler().isWarningEnabled());
    }
}
