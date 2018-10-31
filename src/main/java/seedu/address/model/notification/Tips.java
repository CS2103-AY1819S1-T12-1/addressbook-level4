package seedu.address.model.notification;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a list of available tips.
 */
//@@Snookerballs
public class Tips {
    public static final Tip DEFAULT_TIP = new Tip("No Tips!", "There are no tips!");

    private List<Tip> tips;

    /**
     * Creates a Tips object using an existing list {@coee tips}
     * @param tips The list to create a Tips object with
     */
    public Tips(List<Tip> tips) {
        requireAllNonNull(tips);
        this.tips = tips;
    }

    public Tips() {
        this.tips = new ArrayList<>();
    }

    public Tips(Tip ... allTips) {
        this();
        for (Tip t: allTips) {
            requireAllNonNull(t);
            tips.add(t);
        }
    }

    /**
     * Choose a random tip from the list of tips
     * @return a randomly chosen tip
     */
    public Tip getRandomTip() {
        if (tips.size() == 0) {
            return DEFAULT_TIP;
        }

        Random rand = new Random();
        int index = rand.nextInt(tips.size());
        return tips.get(index);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!(obj instanceof Tips)) {
            return false;
        }

        Tips other = (Tips) obj;

        return this.tips.equals(other.tips);
    }

    @Override
    public int hashCode() {
        return tips.hashCode();
    }
}
