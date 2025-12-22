package gr.greekbudget.export;

import org.apache.pdfbox.pdmodel.*;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class PdfExportService {

    private static final float MARGIN = 40f;
    private static final float TITLE_HEIGHT = 30f;
    private static final float SECTION_SPACING = 20f;

    public static void export(
            File file,
            ExportOptions options,
            ExportContentProvider provider
    ) throws IOException {

        try (PDDocument document = new PDDocument()) {

            // ✅ Unicode font (Greek safe)
            InputStream fontStream =
                    PdfExportService.class.getResourceAsStream(
                            "/fonts/DejaVuSans.ttf"
                    );

            if (fontStream == null) {
                throw new IOException("Font DejaVuSans.ttf not found in resources/fonts");
            }

            PDFont font = PDType0Font.load(document, fontStream);

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            float y = page.getMediaBox().getHeight() - MARGIN;

            if (options.exportRevenueChart) {
                y = drawSection(document, page, font,
                        "Έσοδα ανά Έτος",
                        provider.getRevenueChartImage(), y);
            }

            if (options.exportExpenseChart) {
                y = drawSection(document, page, font,
                        "Έξοδα ανά Έτος",
                        provider.getExpenseChartImage(), y);
            }

            if (options.exportSummaryBars) {
                y = drawSection(document, page, font,
                        "Σύνοψη Προϋπολογισμού",
                        provider.getSummaryBarsImage(), y);
            }

            if (options.exportPieAll) {
                y = drawSection(document, page, font,
                        "Δαπάνες ανά Υπουργείο",
                        provider.getPieAllImage(), y);
            }

            if (options.exportPieNoFinance) {
                y = drawSection(document, page, font,
                        "Χωρίς Υπουργείο Οικονομικών",
                        provider.getPieNoFinanceImage(), y);
            }

            if (options.exportMinistryTrends) {
                for (String ministry : options.selectedMinistries) {
                    y = drawSection(document, page, font,
                            "Εξέλιξη: " + ministry,
                            provider.getMinistryTrendImage(ministry), y);
                }
            }

            document.save(file);
        }
    }

    private static float drawSection(
            PDDocument document,
            PDPage page,
            PDFont font,
            String title,
            BufferedImage image,
            float y
    ) throws IOException {

        float pageHeight = page.getMediaBox().getHeight();

        float availableWidth =
                page.getMediaBox().getWidth() - 2 * MARGIN;

        float scale = availableWidth / image.getWidth();
        float imageHeight = image.getHeight() * scale;

        float requiredHeight =
                TITLE_HEIGHT + imageHeight + SECTION_SPACING;

        if (y - requiredHeight < MARGIN) {
            page = new PDPage(PDRectangle.A4);
            document.addPage(page);
            y = pageHeight - MARGIN;
        }

        try (PDPageContentStream content =
                     new PDPageContentStream(document, page,
                             PDPageContentStream.AppendMode.APPEND, true)) {

            content.beginText();
            content.setFont(font, 14);
            content.newLineAtOffset(MARGIN, y);
            content.showText(title);
            content.endText();

            y -= TITLE_HEIGHT;

            PDImageXObject pdfImage =
                    LosslessFactory.createFromImage(document, image);

            content.drawImage(
                    pdfImage,
                    MARGIN,
                    y - imageHeight,
                    availableWidth,
                    imageHeight
            );
        }

        return y - imageHeight - SECTION_SPACING;
    }
}
