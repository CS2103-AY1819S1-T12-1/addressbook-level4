package seedu.address.model.budget;
//@@author winsonhys

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import seedu.address.model.exceptions.CategoryBudgetExceedTotalBudgetException;
import seedu.address.model.expense.Category;
import seedu.address.model.expense.Expense;


/**
 * Represents the total budget of an expense tracker
 * Guarantees: details are present and not null, field values are validated, mutable.
 */
public class TotalBudget extends Budget {

    protected long numberOfSecondsToRecurAgain;
    private LocalDateTime nextRecurrence;
    private HashSet<CategoryBudget> categoryBudgets;


    /**
     * Constructs a {@code TotalBudget}
     *
     * @param budget a valid double
     */
    public TotalBudget(String budget) {
        super(budget);
        this.nextRecurrence = null;
        this.numberOfSecondsToRecurAgain = 50000;
        this.categoryBudgets = new HashSet<>();
    }

    /**
     * Constructs a {@code TotalBudget} with modified current expenses and recurrence
     * @param budget
     * @param currentExpenses
     * @param nextRecurrence
     * @param numberOfSecondsToRecurAgain
     */
    public TotalBudget(double budget, double currentExpenses, LocalDateTime nextRecurrence,
                       long numberOfSecondsToRecurAgain) {
        super(budget, currentExpenses);
        this.nextRecurrence = nextRecurrence;
        this.numberOfSecondsToRecurAgain = numberOfSecondsToRecurAgain;
        this.categoryBudgets = new HashSet<>();
    }

    /**
     * Constructs a {@code TotalBudget} with modified current expenses, recurrence and category budgets
     * @param budget
     * @param currentExpenses
     * @param nextRecurrence
     * @param numberOfSecondsToRecurAgain
     */
    public TotalBudget(double budget, double currentExpenses, LocalDateTime nextRecurrence,
                       long numberOfSecondsToRecurAgain, HashSet<CategoryBudget> categoryBudgets) {
        super(budget, currentExpenses);
        this.nextRecurrence = nextRecurrence;
        this.numberOfSecondsToRecurAgain = numberOfSecondsToRecurAgain;
        this.categoryBudgets = categoryBudgets;
    }

    /**
     * Constructs a {@code TotalBudget} with modified current expenses
     * @param budget
     * @param currentExpenses
     */
    public TotalBudget(double budget, double currentExpenses) {
        super(budget, currentExpenses);
        this.nextRecurrence = null;
        this.numberOfSecondsToRecurAgain = 50000;
        this.categoryBudgets = new HashSet<>();
    }


    /**
     * Returns the current date in which the totalBudget is created
     *
     * @return a LocalDate object that consists of the most recent timestamp.
     */

    public LocalDateTime getNextRecurrence() {
        return this.nextRecurrence;
    }


    /**
     * Sets the recurrence frequency
     * @param seconds Number of seconds to recur again
     */
    public void setRecurrenceFrequency(long seconds) {
        this.numberOfSecondsToRecurAgain = seconds;
        if (this.nextRecurrence == null) {
            this.nextRecurrence = LocalDateTime.now().plusSeconds(seconds);
        }
    }



    public long getNumberOfSecondsToRecurAgain() {
        return this.numberOfSecondsToRecurAgain;
    }

    /**
     * Adds a category totalBudget. Total sum of all category budgets cannot exceed the totalBudget cap.
     * @param budget
     * @throws CategoryBudgetExceedTotalBudgetException throws this if adding a category totalBudget exceeds the current
     * total totalBudget.
     */
    public void setCategoryBudget(CategoryBudget budget) throws CategoryBudgetExceedTotalBudgetException {
        double sumOfCurrentCategoryBudgets =
            this.categoryBudgets.stream().mapToDouble(categoryBudget -> categoryBudget.getBudgetCap()).sum();
        if (sumOfCurrentCategoryBudgets + budget.getBudgetCap() > this.getBudgetCap()) {
            throw new CategoryBudgetExceedTotalBudgetException(budget, this);
        }
        this.categoryBudgets.remove(budget);
        this.categoryBudgets.add(budget);
    }

    public HashSet<CategoryBudget> getCategoryBudgets() {
        return new HashSet<>(this.categoryBudgets.stream()
            .map(cBudget -> new CategoryBudget(cBudget))
            .collect(Collectors.toSet()));
    }

    @Override
    public void clearSpending() {
        super.clearSpending();
        this.categoryBudgets.forEach(expense -> expense.clearSpending());
    }

    @Override
    public boolean addExpense(Expense expense) {

        this.currentExpenses += expense.getCost().getCostValue();

        AtomicInteger categoryBudgetNotExceeded = new AtomicInteger(0);
        this.categoryBudgets.forEach(cBudget -> {
            if (cBudget.getCategory().equals(expense.getCategory())) {
                if (!cBudget.addExpense(expense)) {
                    categoryBudgetNotExceeded.getAndIncrement();
                }
            }
        });
        return this.currentExpenses <= this.budgetCap && categoryBudgetNotExceeded.get() < 1;
    }

    @Override
    public void removeExpense(Expense expense) {

        super.removeExpense(expense);
        Category cat = expense.getCategory();
        CategoryBudget toDelete = null;
        List<CategoryBudget> cBudgetList = this.categoryBudgets.stream().collect(Collectors.toList());
        for (int i = 0; i < cBudgetList.size(); i++) {
            if (cBudgetList.get(i).getCategory().equals(cat)) {
                toDelete = cBudgetList.get(i);
            }
        }
        if (toDelete != null) {
            toDelete.removeExpense(expense);
        }
    }

    @Override
    public void alterSpending(Expense target, Expense editedExpense) {
        super.alterSpending(target, editedExpense);
        CategoryBudget toRemove = null;
        CategoryBudget toAdd = null;
        List<CategoryBudget> cBudgetArray = this.categoryBudgets.stream().collect(Collectors.toList());
        for (int i = 0; i < cBudgetArray.size(); i++) {
            if (cBudgetArray.get(i).getCategory().equals(target.getCategory())) {
                toRemove = cBudgetArray.get(i);
            }
            if (cBudgetArray.get(i).getCategory().equals(editedExpense.getCategory())) {
                toAdd = cBudgetArray.get(i);
            }
        }
        if (toRemove != null) {
            toRemove.removeExpense(target);
        }
        if (toAdd != null) {
            toAdd.addExpense(editedExpense);
        }
    }


    @Override
    public boolean equals(Object budget) {
        TotalBudget anotherTotalBudget = (TotalBudget) budget;
        return super.equals(budget)
            && this.numberOfSecondsToRecurAgain == anotherTotalBudget.numberOfSecondsToRecurAgain;
    }


    /**
     * Updates the current totalBudget with the new totalBudget if it is the start of a new month. Does nothing if not
     */
    public void checkBudgetRestart() {
        if (this.nextRecurrence == null) {
            //TODO: Notifies user that totalBudget recurrence has not been set
            LOGGER.info("Recurrence has not been set");
            return;
        }
        if (LocalDateTime.now().isAfter(this.nextRecurrence)) {
            this.nextRecurrence = LocalDateTime.now().plusSeconds(this.numberOfSecondsToRecurAgain);
            this.clearSpending();
            //TODO: Notifies user that totalBudget has been restarted
            LOGGER.info("TotalBudget has been restarted");
        }

    }
}
