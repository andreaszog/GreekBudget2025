package gr.greekbudget.export;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.transform.Transform;

import java.awt.image.BufferedImage;

public class ChartSnapshotUtil {

    // ✅ ΝΕΑ – καθαρή, υψηλής ποιότητας
    public static BufferedImage snapshot(Node node) {

        // αν είναι Parent (Chart, VBox, HBox κλπ)
        if (node instanceof Parent parent) {
            parent.applyCss();
            parent.layout();
        }

        double scale = 2.5; // 🔥 ποιότητα PDF

        SnapshotParameters params = new SnapshotParameters();
        params.setTransform(Transform.scale(scale, scale));

        WritableImage fxImage = new WritableImage(
                (int) Math.ceil(node.getBoundsInParent().getWidth() * scale),
                (int) Math.ceil(node.getBoundsInParent().getHeight() * scale)
        );

        node.snapshot(params, fxImage);

        return SwingFXUtils.fromFXImage(fxImage, null);
    }

    // ✅ ΠΑΛΙΑ SIGNATURE για να ΜΗΝ ΣΠΑΣΕΙ ΤΙΠΟΤΑ
    public static BufferedImage snapshot(Node node, int width, int height) {
        return snapshot(node);
    }
}
