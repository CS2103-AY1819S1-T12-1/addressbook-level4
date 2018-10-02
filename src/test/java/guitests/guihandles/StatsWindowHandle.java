package guitests.guihandles;

import guitests.GuiRobot;
import javafx.stage.Stage;

/**
 * A handle to the {@code HelpWindow} of the application.
 */
public class StatsWindowHandle extends StageHandle {

    public static final String STATS_WINDOW_TITLE = "Stats";

    public StatsWindowHandle(Stage statsWindowStage) {
        super(statsWindowStage);
    }

    /**
     * Returns true if a help window is currently present in the application.
     */
    public static boolean isWindowPresent() {
        return new GuiRobot().isWindowShown(STATS_WINDOW_TITLE);
    }
}
