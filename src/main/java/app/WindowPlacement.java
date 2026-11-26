package app;

import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import javax.swing.JFrame;

/** Places a packed window within the desktop area available to applications. */
final class WindowPlacement {
    private WindowPlacement() {
    }

    static void fitAndCenter(JFrame frame) {
        Rectangle usableBounds = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        int width = Math.min(frame.getWidth(), usableBounds.width);
        int height = Math.min(frame.getHeight(), usableBounds.height);
        frame.setBounds(usableBounds.x + (usableBounds.width - width) / 2,
                usableBounds.y + (usableBounds.height - height) / 2, width, height);
    }
}
