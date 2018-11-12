package seedu.expensetracker.ui;

import static org.junit.Assert.assertEquals;

import java.text.DecimalFormat;
import java.util.HashSet;
import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.CategoriesPanelHandle;
import seedu.expensetracker.model.budget.CategoryBudget;

//@author Snookerballs
public class CategoriesPanelTest extends GuiUnitTest {
    private static final HashSet<CategoryBudget> CATEGORY_BUDGETS = new HashSet<>();
    private CategoriesPanelHandle categoriesPanelHandle;

    @Before
    public void setUp() throws InterruptedException {
        CategoriesPanel categoriesPanel = new CategoriesPanel(CATEGORY_BUDGETS.iterator());
        categoriesPanelHandle = new CategoriesPanelHandle(categoriesPanel.getRoot());
    }

    @Test
    public void addCategoryBudget_valid() {
        DecimalFormat df = new DecimalFormat("#.##");

        //Adding 1 Category
        CATEGORY_BUDGETS.add(new CategoryBudget("Cat", "10.00"));
        CATEGORY_BUDGETS.add(new CategoryBudget("Cat2", "10.00"));
        setIterator();
        Iterator<CategoryBudget> iterator = CATEGORY_BUDGETS.iterator();
        while (iterator.hasNext()) {
            CategoryBudget budget = iterator.next();
            assertEquals(df.format(budget.getBudgetRatio()),
                    df.format(categoriesPanelHandle.getCategoryIconHandle(budget).getCategoryPercentage()));
        }
        assertEquals(categoriesPanelHandle.size(), 2);

        //Adding >4 categories
        CATEGORY_BUDGETS.add(new CategoryBudget("Cat2", "10.00"));
        CATEGORY_BUDGETS.add(new CategoryBudget("Cat3", "20.00"));
        CATEGORY_BUDGETS.add(new CategoryBudget("Cat4", "30.00"));
        CATEGORY_BUDGETS.add(new CategoryBudget("Cat5", "40.00"));

        setIterator();
        iterator = CATEGORY_BUDGETS.iterator();
        int counter = 0;
        while (iterator.hasNext() && counter < 3) {
            CategoryBudget budget = iterator.next();
            assertEquals(df.format(budget.getBudgetRatio()),
                    df.format(categoriesPanelHandle.getCategoryIconHandle(budget).getCategoryPercentage()));
            counter++;
        }
        assertEquals(categoriesPanelHandle.size(), 4);

        //No Categories
        CATEGORY_BUDGETS.clear();
        setIterator();
        iterator = CATEGORY_BUDGETS.iterator();
        while (iterator.hasNext()) {
            CategoryBudget budget = iterator.next();
        }
        assertEquals(categoriesPanelHandle.size(), 0);
    }

    private void setIterator() {
        Iterator<CategoryBudget> iterator = CATEGORY_BUDGETS.iterator();
        CategoriesPanel categoriesPanel = new CategoriesPanel(iterator);
        uiPartRule.setUiPart(categoriesPanel);
        categoriesPanelHandle = new CategoriesPanelHandle(categoriesPanel.getRoot());
    }

}
