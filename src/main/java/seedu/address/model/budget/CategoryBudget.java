package seedu.address.model.budget;
//@@author winsonhys

import static seedu.address.commons.util.AppUtil.checkArgument;

import seedu.address.model.expense.Category;

/**
 * Represents maximum totalBudget of a category in the expense tracker
 * Guarantees: details are present and not null, field values are validated, mutable. TotalBudget here never exceeds the
 * total totalBudget of the expense tracker
 */
public class CategoryBudget extends Budget {

    private Category belongsToCategory;


    public CategoryBudget (String category, String budget) {
        super(budget);
        checkArgument(Category.isValidCategory(category), Category.MESSAGE_CATEGORY_CONSTRAINTS);
        this.belongsToCategory = new Category(category);
    }

    public CategoryBudget (CategoryBudget cBudget) {
        super(cBudget);
        this.belongsToCategory = cBudget.getCategory();
    }

    public Category getCategory() {
        return this.belongsToCategory;
    }

    @Override
    public boolean equals(Object categoryBudget) {
        CategoryBudget otherCategoryBudget = (CategoryBudget) categoryBudget;
        return this.belongsToCategory.equals(otherCategoryBudget.getCategory());
    }

    @Override
    public int hashCode () {
        return belongsToCategory.hashCode();
    }
}
