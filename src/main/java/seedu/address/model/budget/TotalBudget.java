package seedu.address.model.budget;
//@@author winsonhys

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

import seedu.address.model.exceptions.CategoryBudgetDoesNotExist;
import seedu.address.model.exceptions.CategoryBudgetExceedTotalBudgetException;
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
     * Adds a category totalBudget. Total sum of all category budgets cannnot exceed the totalBudget cap.
     * @param budget
     * @throws CategoryBudgetExceedTotalBudgetException throws this if adding a category totalBudget exceeds the current
     * total totalBudget.
     */
    public void addCategoryBudget(CategoryBudget budget) throws CategoryBudgetExceedTotalBudgetException {
        double sumOfCurrentCategoryBudgets =
            this.categoryBudgets.stream().mapToDouble(categoryBudget -> categoryBudget.getBudgetCap()).sum();
        System.out.println(sumOfCurrentCategoryBudgets);
        System.out.println(budget.getBudgetCap());
        if (sumOfCurrentCategoryBudgets + budget.getBudgetCap() > this.getBudgetCap()) {
            throw new CategoryBudgetExceedTotalBudgetException(budget, this);
        }
        this.categoryBudgets.add(budget);
        System.out.println("Category budget added");
        System.out.println(this.getCategoryBudgets());
    }

    /**
     * Modifies a category totalBudget currently in the set of category budgets.
     * @param budget
     */
    public void modifyCategoryBudget(CategoryBudget budget) throws CategoryBudgetDoesNotExist {
        if (this.categoryBudgets.remove(budget)) {
            throw new CategoryBudgetDoesNotExist(budget);
        }
        this.categoryBudgets.add(budget);
    }

    public HashSet<CategoryBudget> getCategoryBudgets() {
        return new HashSet<>(this.categoryBudgets);
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
