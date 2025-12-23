package gr.greekbudget.export;

import java.awt.image.BufferedImage;

public interface ExportContentProvider {

    BufferedImage getRevenueChartImage();
    BufferedImage getExpenseChartImage();
    BufferedImage getSummaryBarsImage();
    BufferedImage getPieAllImage();
    BufferedImage getPieNoFinanceImage();
    BufferedImage getMinistryTrendImage(String ministry);
}
