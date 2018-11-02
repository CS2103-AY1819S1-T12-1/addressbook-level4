package seedu.address.storage.budget;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.stream.Collectors;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.budget.TotalBudget;
import seedu.address.storage.storageutil.LocalDateTimeAdapter;


//@author winsonhys
/**
 * JAXB-friendly adapted version of the TotalBudget.
 */

public class XmlAdaptedTotalBudget extends XmlAdaptedBudget {


    @XmlElement
    @XmlJavaTypeAdapter(value = LocalDateTimeAdapter.class)
    private LocalDateTime nextRecurrence;
    @XmlElement
    private long numberOfSecondsToRecurAgain;

    @XmlElement
    private HashSet<XmlAdaptedCategoryBudget> categoryBudgets;


    /**
     * Constructs an XmlAdaptedTotalBudget.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedTotalBudget() {
        super();
        this.categoryBudgets = new HashSet<>();
    }

    /**
     * Converts a given TotalBudget into this class for JAXB use.
     *
     * @param source source totalBudget
     */
    public XmlAdaptedTotalBudget(TotalBudget source) {
        super(source);
        this.nextRecurrence = source.getNextRecurrence();
        this.numberOfSecondsToRecurAgain = source.getNumberOfSecondsToRecurAgain();
        this.categoryBudgets =
            source.getCategoryBudgets()
                .stream()
                .map(categoryBudget -> new XmlAdaptedCategoryBudget(categoryBudget))
                .collect(Collectors.toCollection(HashSet::new));
    }

    /**
     * Converts this jaxb-friendly totalBudget tag object into the model's TotalBudget object.
     */
    public TotalBudget toModelType() throws IllegalValueException {
        try {
            if (this.categoryBudgets != null) {
                return new TotalBudget(Double.parseDouble(this.budgetCap), Double.parseDouble(this.currentExpenses),
                    this.nextRecurrence,
                    this.numberOfSecondsToRecurAgain,
                    this.categoryBudgets.stream()
                        .map(xmlBudget -> {
                            try {
                                return xmlBudget.toModelType();
                            } catch (IllegalValueException e) {
                                throw new RuntimeException();
                            }
                        })
                        .collect(Collectors.toCollection(HashSet::new)));
            }
            return new TotalBudget(Double.parseDouble(this.budgetCap), Double.parseDouble(this.currentExpenses),
                this.nextRecurrence,
                this.numberOfSecondsToRecurAgain);
        } catch (RuntimeException e) {
            throw new IllegalValueException("Some values in TotalBudget are not valid");
        }

    }

}
