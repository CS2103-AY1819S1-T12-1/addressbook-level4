package seedu.expensetracker.commons.events.ui;

import seedu.expensetracker.commons.events.BaseEvent;

//@@author snookerballs
/**
 * An event requesting a panel swap.
 */
public class SwapLeftPanelEvent extends BaseEvent {

    /**
     * Represents the two types of panels to swap to.
     */
    public enum PanelType {
        LIST, STATISTIC
    }

    private PanelType panelType;

    public SwapLeftPanelEvent(PanelType type) {
        panelType = type;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName();
    }

    public PanelType getPanelType() {
        return panelType;
    }
}

