package seedu.expensetracker.logic.commands;
//@@author winsonhys

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static seedu.expensetracker.testutil.ModelUtil.getTypicalModel;
import static seedu.expensetracker.testutil.TypicalIndexes.INDEX_FIRST_EXPENSE;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import seedu.expensetracker.logic.CommandHistory;
import seedu.expensetracker.logic.commands.exceptions.CommandException;
import seedu.expensetracker.model.Model;
import seedu.expensetracker.model.budget.CategoryBudget;
import seedu.expensetracker.model.budget.TotalBudget;
import seedu.expensetracker.model.exceptions.NoUserSelectedException;
import seedu.expensetracker.model.expense.Category;
import seedu.expensetracker.model.expense.EditExpenseDescriptor;
import seedu.expensetracker.model.expense.Expense;
import seedu.expensetracker.testutil.EditExpenseDescriptorBuilder;
import seedu.expensetracker.testutil.ExpenseBuilder;



public class BudgetIntegrationTest {
    private Model model = getTypicalModel();
    private CommandHistory commandHistory = new CommandHistory();

    @BeforeEach
    public void setUp() {
        model = getTypicalModel();
        commandHistory = new CommandHistory();
    }

    @Test
    public void addExpense_budgetUpdated() throws CommandException, NoUserSelectedException {
        double originalExpenses = this.model.getMaximumBudget().getCurrentExpenses();
        Expense toAdd = new ExpenseBuilder().withCost("2.00").build();
        new AddCommand(toAdd).execute(this.model, this.commandHistory);
        assertTrue(this.model.getMaximumBudget().getCurrentExpenses() == originalExpenses + 2.00);
    }

    @Test
    public void setCategoryBudget_hasExpenses_spendingUpdated() throws CommandException, NoUserSelectedException {
        Category selectedCategory = model.getFilteredExpenseList().get(0).getCategory();
        double totalExpensesOfSelectedCategory =
            model.getFilteredExpenseList().stream().filter(x -> x.getCategory().equals(selectedCategory))
                .mapToDouble(x -> x.getCost().getCostValue()).sum();
        this.model.modifyMaximumBudget(new TotalBudget("99999.00"));
        new SetCategoryBudgetCommand(new CategoryBudget(selectedCategory.categoryName, "999.00"))
            .execute(this.model, this.commandHistory);
        CategoryBudget cBudget = this.model.getMaximumBudget().getCategoryBudgets().toArray(new CategoryBudget[1])[0];
        assertEquals(cBudget.getCurrentExpenses(), totalExpensesOfSelectedCategory);


    }

    @Test
    public void addExpense_categoryBudgetUpdated() throws CommandException, NoUserSelectedException {
        String categoryName = "NewCategory";
        double originalExpenses = this.model.getMaximumBudget().getCurrentExpenses();
        this.model.modifyMaximumBudget(new TotalBudget("9999.00"));
        Expense toAdd = new ExpenseBuilder().withCost("2.00").withCategory(categoryName).build();
        new SetCategoryBudgetCommand(new CategoryBudget(categoryName, "999.00"))
            .execute(this.model, this.commandHistory);
        new AddCommand(toAdd).execute(this.model, this.commandHistory);
        assertTrue(this.model.getMaximumBudget().getCurrentExpenses() == originalExpenses + 2.00);
        CategoryBudget cBudget = this.model.getMaximumBudget().getCategoryBudgets().toArray(new CategoryBudget[1])[0];
        assertTrue(cBudget.getCurrentExpenses() == 2);

    }

    @Test
    public void editExpense_budgetUpdated() throws CommandException, NoUserSelectedException {
        double initialExpenses = this.model.getMaximumBudget().getCurrentExpenses();
        double initialFirstExpenseCost = model.getFilteredExpenseList().get(0).getCost().getCostValue();
        Expense editedExpense = new ExpenseBuilder().withCost("3.00").build();
        EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder(editedExpense).build();
        new EditCommand(INDEX_FIRST_EXPENSE, descriptor).execute(this.model, this.commandHistory);
        assertTrue(this.model.getMaximumBudget().getCurrentExpenses() == initialExpenses - initialFirstExpenseCost + 3);

    }

    @Test
    public void editExpense_categoryBudgetUpdated_categoryEdited() throws CommandException,
        NoUserSelectedException {
        this.model.modifyMaximumBudget(new TotalBudget("9999.00"));
        String categoryName = "NewCategory";
        String oldCategory = "OldCategory";
        Expense expenseWithCategory = new ExpenseBuilder().withCategory(oldCategory).withCost("50.00").build();
        this.model.updateExpense(this.model.getFilteredExpenseList().get(0), expenseWithCategory);
        Expense editedExpense = new ExpenseBuilder().withCategory(categoryName).withCost("3.00").build();
        EditExpenseDescriptor descriptor = new EditExpenseDescriptorBuilder(editedExpense).build();
        new SetCategoryBudgetCommand(new CategoryBudget(oldCategory, "999.00"))
            .execute(this.model, this.commandHistory);
        new SetCategoryBudgetCommand(new CategoryBudget(categoryName, "999.00"))
            .execute(this.model, this.commandHistory);
        new EditCommand(INDEX_FIRST_EXPENSE, descriptor).execute(this.model, this.commandHistory);
        CategoryBudget[] cBudgetArray =
            this.model.getMaximumBudget().getCategoryBudgets().toArray(new CategoryBudget[1]);
        for (CategoryBudget cBudget : cBudgetArray) {
            if (cBudget.getCategory().categoryName == categoryName) {
                assertEquals(cBudget.getCurrentExpenses(), 3.0);
            } else {
                assertEquals(cBudget.getCurrentExpenses(), 0.0);
            }
        }
    }

    @Test
    public void clear_budgetSpendingCleared() throws NoUserSelectedException {
        new ClearCommand().execute(this.model, this.commandHistory);
        assertEquals(this.model.getMaximumBudget().getCurrentExpenses(), 0.0);
        assertEquals(this.model.getMaximumBudget().getCategoryBudgets()
            .stream().mapToDouble(expense -> expense.getCurrentExpenses()).sum(), 0.0);
    }

    @Test
    public void delete_spendingRemoved() throws NoUserSelectedException, CommandException {
        this.model.modifyMaximumBudget(new TotalBudget("9999.00"));
        String oldCategory = "OldCategory";
        Expense expenseWithCategory = new ExpenseBuilder().withCategory(oldCategory).withCost("50.00").build();
        this.model.updateExpense(this.model.getFilteredExpenseList().get(0), expenseWithCategory);
        double initialSpending = this.model.getMaximumBudget().getCurrentExpenses();
        new SetCategoryBudgetCommand(new CategoryBudget(oldCategory, "999.00"))
            .execute(this.model, this.commandHistory);
        new DeleteCommand(INDEX_FIRST_EXPENSE).execute(this.model, this.commandHistory);
        assertEquals(this.model.getMaximumBudget().getCurrentExpenses(), initialSpending - 50.0);
        assertEquals(this.model.getMaximumBudget()
            .getCategoryBudgets()
            .toArray(new CategoryBudget[1])[0]
            .getCurrentExpenses(), 0.0);

    }

}
