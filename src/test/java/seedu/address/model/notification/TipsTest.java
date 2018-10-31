package seedu.address.model.notification;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import java.util.List;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;

import seedu.address.testutil.Assert;


//@@author Snookerballs
public class TipsTest {
    public static final Tip VALID_TIP = new Tip("HEADER", "BODY");

    private Tips tips = new Tips();

    /**
     * Initialize tips before each test
     */
    @BeforeEach
    public void initializeTips() {
        tips = new Tips();
    }

    @Test
    public void constructor_null_throwsNullPointerException() {
        Assert.assertThrows(NullPointerException.class, () -> new Tips((List<Tip>) null));
        Assert.assertThrows(NullPointerException.class, () -> new Tips((Tip) null));
    }

    @Test
    public void equals() {
        //Valid - empty tips
        assertEquals(tips, new Tips());

        //Valid - same tips
        tips = new Tips(VALID_TIP);
        assertEquals(tips, new Tips(VALID_TIP));

        //Valid - same object
        assertEquals(tips, tips);

        //Invalid - object not Tips
        assertNotEquals(tips, 0);

        //Invalid - differing lists
        assertNotEquals(tips, new Tips());

        //Invalid - null object
        assertNotEquals(tips, null);
    }

    @Test
    public void getRandomTip() {
        //No tips in list
        assertEquals(tips.getRandomTip(), Tips.DEFAULT_TIP);

        //A tip in list
        tips = new Tips(VALID_TIP);
        assertEquals(tips.getRandomTip(), VALID_TIP);
    }

}
