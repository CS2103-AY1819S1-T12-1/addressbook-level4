package seedu.address.model.notification;

import seedu.address.model.budget.Budget;

/**
 * Represents a notification that displays a warning.
 */
//@@Snookerballs
public class WarningNotification extends Notification {
    private static final double PERCENTAGE_TO_SEND_OVERBUDGET_MESSAGE = 1.0;
    private static final String MESSAGE_TO_SEND_WHEN_OVERBUDGET = "You are overbudget!";
    private static final String MESSAGE_TO_SEND_WHEN_NEARING_BUDGET = "You are about to go overbudget!";


    public WarningNotification(Budget budget) {
        header = "Warning";
        body = generateBody(budget);
        type = NotificationType.WARNING;

    }

    public WarningNotification(String header, String body) {
        super(header, body);
        type = NotificationType.WARNING;
    }

    public WarningNotification(WarningNotification notification) {
        super(notification);
    }

    /**
    * Generates a body depending on the budget level.
    * @param budget level to check
    * @return the body message  of the warning
    */
    private String generateBody(Budget budget) {
        if (budget.getBudgetPercentage() > PERCENTAGE_TO_SEND_OVERBUDGET_MESSAGE) {
            return MESSAGE_TO_SEND_WHEN_OVERBUDGET;
        }
        return MESSAGE_TO_SEND_WHEN_NEARING_BUDGET;
    }
}
