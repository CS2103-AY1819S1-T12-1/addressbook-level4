package seedu.expensetracker.ui;

import static org.junit.Assert.assertTrue;

import java.util.LinkedHashMap;

import org.junit.Before;
import org.junit.Test;

import guitests.guihandles.StatisticsPanelHandle;
import seedu.expensetracker.logic.commands.StatsCommand.StatsMode;
import seedu.expensetracker.logic.commands.StatsCommand.StatsPeriod;

//@@author jonathantjm
public class StatisticsPanelTest extends GuiUnitTest {
    private StatisticsPanelHandle statisticsPanelHandle;

    @Before
    public void setUp() throws InterruptedException {
        StatisticsPanel statisticsPanel = new StatisticsPanel(
                new LinkedHashMap<>(),
                StatsPeriod.DAY,
                StatsMode.TIME,
                7
        );
        Thread.sleep(1000);
        uiPartRule.setUiPart(statisticsPanel);
        statisticsPanelHandle = new StatisticsPanelHandle(statisticsPanel.getRoot());
    }

    @Test
    public void noExpensesTextAppearsWhenNoExpenses() {
        assertTrue(statisticsPanelHandle.isNoExpenseText());
    }

    @Test
    public void correctNoExpensesTextAppearsWhenNoExpenses() {
        setChartData(new LinkedHashMap<>(), StatsPeriod.DAY, StatsMode.CATEGORY, 1);
        assertTrue(statisticsPanelHandle.isMatchingText("There are no recorded expenditures in the past day"));

        setChartData(new LinkedHashMap<>(), StatsPeriod.DAY, StatsMode.CATEGORY, 7);
        assertTrue(statisticsPanelHandle.isMatchingText("There are no recorded expenditures in the past 7 days"));

        setChartData(new LinkedHashMap<>(), StatsPeriod.MONTH, StatsMode.CATEGORY, 1);
        assertTrue(statisticsPanelHandle.isMatchingText("There are no recorded expenditures in the past month"));

        setChartData(new LinkedHashMap<>(), StatsPeriod.MONTH, StatsMode.CATEGORY, 7);
        assertTrue(statisticsPanelHandle.isMatchingText("There are no recorded expenditures in the past 7 months"));
    }

    @Test
    public void categoryChartAppearsWhenStatsModeIsCategory() {
        LinkedHashMap<String, Double> mockData = new LinkedHashMap<>();
        mockData.put("Food", 6.00);
        setChartData(mockData, StatsPeriod.DAY, StatsMode.CATEGORY, 7);
        assertTrue(statisticsPanelHandle.isCategoryChart());
    }

    @Test
    public void timeChartAppearsWhenStatsModeIsTime() {
        LinkedHashMap<String, Double> mockData = new LinkedHashMap<>();
        mockData.put("26-10-18", 6.00);
        setChartData(mockData, StatsPeriod.DAY, StatsMode.TIME, 7);
        assertTrue(statisticsPanelHandle.isTimeChart());
    }

    private void setChartData(
            LinkedHashMap<String, Double> mockData,
            StatsPeriod statsPeriod,
            StatsMode statsMode,
            int periodAmount
    ) {
        StatisticsPanel statisticsPanel = new StatisticsPanel(mockData, statsPeriod, statsMode, periodAmount);
        statisticsPanelHandle = new StatisticsPanelHandle(statisticsPanel.getRoot());
    }

}
