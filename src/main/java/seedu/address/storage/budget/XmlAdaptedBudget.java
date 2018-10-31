package seedu.address.storage.budget;
//@@author winsonhys

import javax.xml.bind.annotation.XmlElement;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.budget.Budget;

/**
 * JAXB-friendly adapted version of the Budget. Used as a base class for other Budget types
 */

public abstract class XmlAdaptedBudget {
    @XmlElement
    protected String budgetCap;

    @XmlElement
    protected String currentExpenses;

    /**
     * Constructs an XmlAdaptedBudget.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedBudget() {
    }

    public XmlAdaptedBudget(String budget) {
        this.budgetCap = budget;
        this.currentExpenses = "0";

    }

    public XmlAdaptedBudget(String budget, String currentExpenses) throws IllegalValueException {

        this.budgetCap = budget;
        this.currentExpenses = currentExpenses;

    }

    public XmlAdaptedBudget(Budget source) {
        this.budgetCap = String.format("%.2f", source.getBudgetCap());
        this.currentExpenses = String.format("%.2f", source.getCurrentExpenses());
    }

    /**
     * Converts this jaxb-friendly adapted budget object into the model's Budget object.
     */
    public abstract Budget toModelType() throws IllegalValueException;
}
