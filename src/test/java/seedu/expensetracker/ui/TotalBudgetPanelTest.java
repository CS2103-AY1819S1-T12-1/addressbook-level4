package seedu.expensetracker.ui;

import static org.junit.Assert.assertTrue;
import static seedu.expensetracker.testutil.EventsUtil.postNow;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.BudgetPanelHandle;
import seedu.expensetracker.commons.events.ui.UpdateBudgetPanelEvent;
import seedu.expensetracker.model.budget.TotalBudget;

//@@author Snookerballs
public class TotalBudgetPanelTest extends GuiUnitTest {
    private BudgetPanelHandle budgetPanelHandle;

    @Before
    public void setUp() throws InterruptedException {
        BudgetPanel budgetPanel = new BudgetPanel(new TotalBudget(0, 0));
        Thread.sleep(1000);
        uiPartRule.setUiPart(budgetPanel);
        budgetPanelHandle = new BudgetPanelHandle(budgetPanel.getRoot());
    }

    @Test
    public void addPanel_updateBudgetPanelEventPosted() {
        run(100, 200);
        assertTrue(budgetPanelHandle.isColorGreen());
    }

    @Test
    public void colorChangeToRedWhenOverBudget_updateBudgetPanelEventPosted () {
        run(200, 100);
        assertTrue(budgetPanelHandle.isColorRed());
    }


    /**
     * Tests if the UI elements, expenseDisplay, budgetDisplay and budgetBar have the corrct values
     * @param expense to set
     * @param budget to set
     */
    private void run(double expense, double budget) {
        postNow(generateUpdateBudgetPanelEvent(expense, budget));

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertTrue(budgetPanelHandle.isExpenseCorrect(String.format("%.2f", expense)));
        assertTrue(budgetPanelHandle.isBudgetBarProgressAccurate(expense / budget));
        assertTrue(budgetPanelHandle.isBudgetCorrect(String.format("%.2f", budget)));
    }

    private UpdateBudgetPanelEvent generateUpdateBudgetPanelEvent(double expense, double budget) {
        return new UpdateBudgetPanelEvent(new TotalBudget(budget, expense));
    }

}
