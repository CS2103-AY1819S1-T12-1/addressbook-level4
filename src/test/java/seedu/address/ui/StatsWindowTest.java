package seedu.address.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assume.assumeFalse;

import java.util.LinkedHashMap;

import org.junit.Before;
import org.junit.Test;

import org.testfx.api.FxToolkit;

import guitests.guihandles.StatsWindowHandle;

import javafx.stage.Stage;

public class StatsWindowTest extends GuiUnitTest {

    private StatsWindow statsWindow;
    private StatsWindowHandle statsWindowHandle;

    @Before
    public void setUp() throws Exception {
        LinkedHashMap<String, Double> testData = new LinkedHashMap<>();
        testData.put("01-10-2018", 2.7);
        guiRobot.interact(() -> statsWindow = new StatsWindow(testData));
        FxToolkit.registerStage(statsWindow::getRoot);
        statsWindowHandle = new StatsWindowHandle(statsWindow.getRoot());
    }

    @Test
    public void display() throws Exception {
        FxToolkit.showStage();
        assertEquals(1, statsWindow.getChartArea().getChildren().size());
    }

    @Test
    public void isNotShowing_after_closed() throws Exception {
        guiRobot.interact(statsWindow::close);
        assertFalse(statsWindow.isShowing());
    }

    @Test
    public void isShowing_helpWindowIsShowing_returnsTrue() {
        guiRobot.interact(statsWindow::show);
        assertTrue(statsWindow.isShowing());
    }

    @Test
    public void isShowing_helpWindowIsHiding_returnsFalse() {
        assertFalse(statsWindow.isShowing());
    }

    @Test
    public void focus_helpWindowNotFocused_focused() throws Exception {
        // TODO: This test skip can be removed once this bug is fixed:
        // https://github.com/javafxports/openjdk-jfx/issues/50
        //
        // When there are two stages (stage1 and stage2) shown,
        // stage1 is in focus and stage2.requestFocus() is called,
        // we expect that stage1.isFocused() will return false while
        // stage2.isFocused() returns true. However, as reported in the bug report,
        // both stage1.isFocused() and stage2.isFocused() returns true,
        // which fails the test.
        assumeFalse("Test skipped in headless mode: Window focus behavior is buggy.", guiRobot.isHeadlessMode());
        guiRobot.interact(statsWindow::show);

        // Focus on another stage to remove focus from the helpWindow
        guiRobot.interact(() -> {
            Stage temporaryStage = new Stage();
            temporaryStage.show();
            temporaryStage.requestFocus();
        });
        assertFalse(statsWindow.getRoot().isFocused());

        guiRobot.interact(statsWindow::focus);
        assertTrue(statsWindow.getRoot().isFocused());
    }
}
