package seedu.expensetracker.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_NOTIFICATION_TYPE;
import static seedu.expensetracker.logic.parser.CliSyntax.PREFIX_TOGGLE;

import java.util.Optional;

import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.logic.commands.exceptions.CommandException;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;

/**
 * Toggles the notifications on and off.
 */
//@Snookerballs
public class NotificationCommand extends Command {
    public static final String COMMAND_WORD = "notification";
    public static final String COMMAND_ALIAS = "n";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Toggles notification messages."
            + "Parameters: "
            + PREFIX_NOTIFICATION_TYPE + "[NOTIFICATION TYPE] "
            + PREFIX_TOGGLE + "on/off \n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_NOTIFICATION_TYPE + "warning "
            + PREFIX_TOGGLE + "off";

    public static final String MESSAGE_SUCCESS = "Notifications successfully toggled %1$s.";

    public static final String OPTION_ON = "on";
    public static final String OPTION_OFF = "off";

    public static final String OPTION_WARNING = "warning";
    public static final String OPTION_TIP = "tip";

    private NotificationCommandDescriptor descriptor;
    /**
     * Creates an AddCommand to add the specified {@code Expense}
     */
    public NotificationCommand(NotificationCommandDescriptor descriptor) {
        this.descriptor = descriptor;
    }

    @Override
    public CommandResult execute(Model model, CommandHistory history) throws CommandException, NoUserSelectedException {
        requireNonNull(model);

        boolean toggleOption;
        String option;
        if (descriptor.getToggle().get().equals(OPTION_ON)) {
            option = OPTION_ON;
            toggleOption = true;
        } else {
            option = OPTION_OFF;
            toggleOption = false;
        }

        if (descriptor.getNotificationType().isPresent()) {
            toggleSpecificNotification(model, descriptor.getNotificationType().get(), toggleOption);
        } else {
            model.toggleBothNotification(toggleOption);
        }
        model.commitExpenseTracker();
        return new CommandResult(String.format(MESSAGE_SUCCESS, option));
    }

    /**
     * Toggles a specific type of notification
     * @param model to modify
     * @param type of notification to generate
     * @param option to toggle to
     * @throws NoUserSelectedException
     */
    public void toggleSpecificNotification(Model model, String type, boolean option) throws NoUserSelectedException {
        if (type.equals(OPTION_WARNING)) {
            model.toggleWarningNotification(option);
        } else if (type.equals(OPTION_TIP)) {
            model.toggleTipNotification(option);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof NotificationCommand); // instanceof handles nulls
    }

    /**
     * Stores the variables of the NotificationCommand as a class
     */
    public static class NotificationCommandDescriptor {
        private String toggle;
        private String notificationType;

        public Optional<String> getToggle() {
            return Optional.ofNullable(toggle);
        }

        /**
         * Sets toggle
         * @param toggle to set to
         * @return true if successfully set, false otherwise.
         */
        public boolean setToggle(String toggle) {
            if (toggle.equals(OPTION_ON) || toggle.equals(OPTION_OFF)) {
                this.toggle = toggle;
                return true;
            }
            return false;
        }

        public Optional<String> getNotificationType() {
            return Optional.ofNullable(notificationType);
        }

        /**
         * Sets notificationType
         * @param notificationType to set to
         * @return true if successfully set, false otherwise.
         */
        public boolean setNotificationType(String notificationType) {
            if (notificationType.equals(OPTION_WARNING) || notificationType.equals(OPTION_TIP)) {
                this.notificationType = notificationType;
                return true;
            }
            return false;
        }
    }
}
