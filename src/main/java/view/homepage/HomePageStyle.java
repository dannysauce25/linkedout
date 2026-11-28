package view.homepage;

import javax.swing.*;
import javax.swing.plaf.basic.BasicButtonUI;
import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public final class HomePageStyle {
    public static final int WIDTH = 414;
    public static final int HEIGHT = 896;
    public static final Color PAPER = new Color(0xfcfcfc);
    public static final Color GREEN = new Color(0xdceedf);
    public static final Color CARD_GREEN = new Color(0xe2efe3);
    public static final Color PHOTO_BUTTON = new Color(0xdff0e2);
    public static final Color PROFILE_MUTED = new Color(0x8a918c);
    public static final Color XP_TRACK = new Color(0xf0f6ee);
    public static final Color XP_FILL = new Color(0xf3eaa6);
    public static final Color PENCIL_GREEN = new Color(0x2f5d3a);
    public static final Color RECOMMENDED_CARD = new Color(0xfcfdd6);
    public static final Color RECOMMENDED_TEXT = new Color(0x9a9d82);
    public static final Color ARROW_GRAY = new Color(0x8a8a8a);
    public static final Color HEART_OUTLINE = new Color(0x8a8d72);
    public static final Color HEART_FILLED = new Color(0xe0555f);
    public static final Color INK = new Color(0x202624);
    public static final Font UI_FONT = loadFont("DM-Sans");
    public static final Font HANDWRITING = loadFont("Gochi-Hand");

    private HomePageStyle() {
    }

    private static Font loadFont(String name) {
        try (InputStream stream = Objects.requireNonNull(
                HomePageStyle.class.getResourceAsStream("/journal/fonts/" + name + ".ttf"), name)) {
            return Font.createFont(Font.TRUETYPE_FONT, stream);
        } catch (IOException | FontFormatException exception) {
            throw new IllegalStateException("Could not load " + name, exception);
        }
    }

    public static JLabel label(String text, Font font) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.putClientProperty("html.disable", true);
        label.setFont(font);
        label.setForeground(INK);
        return label;
    }

    public static ImageIcon loadScaledIcon(String assetName, int iconWidth, int iconHeight) {
        ImageIcon sourceIcon = new ImageIcon(Objects.requireNonNull(
                HomePageStyle.class.getResource("/journal/assets/" + assetName + ".png"), assetName));
        return new ImageIcon(sourceIcon.getImage().getScaledInstance(iconWidth, iconHeight, Image.SCALE_SMOOTH));
    }

    public static JButton iconButton(String label, ImageIcon icon, Runnable onClick) {
        JButton button = new JButton(icon);
        button.getAccessibleContext().setAccessibleName(label);
        button.setToolTipText(label);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(event -> onClick.run());
        return button;
    }

    public static JButton roundedButton(String label, Color fill, Runnable onClick) {
        JButton button = new JButton(label) {
            @Override
            protected void paintComponent(Graphics graphics) {
                paintRounded(graphics, fill, getWidth(), getHeight(), getHeight());
                super.paintComponent(graphics);
            }
        };
        button.setUI(new BasicButtonUI());
        button.setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
        button.setFont(UI_FONT.deriveFont(12f));
        button.setForeground(INK);
        button.setMargin(new Insets(0, 0, 0, 0));
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(event -> onClick.run());
        return button;
    }

    /** Draws an icon in code, so no PNG asset is needed yet. */
    public interface IconPainter {
        void paint(Graphics2D graphics, int width, int height);
    }

    public static JButton paintedButton(String label, IconPainter painter, Runnable onClick) {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics2D iconGraphics = (Graphics2D) graphics.create();
                iconGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                painter.paint(iconGraphics, getWidth(), getHeight());
                iconGraphics.dispose();
            }
        };
        button.getAccessibleContext().setAccessibleName(label);
        button.setToolTipText(label);
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        button.addActionListener(event -> onClick.run());
        return button;
    }

    public static void paintCameraBadge(Graphics2D graphics, int width, int height) {
        graphics.setColor(Color.WHITE);
        graphics.fillOval(0, 0, width, height);
        graphics.setColor(INK);
        graphics.fill(new RoundRectangle2D.Float(width * .22f, height * .34f, width * .56f, height * .38f, 8, 8));
        graphics.fill(new RoundRectangle2D.Float(width * .38f, height * .26f, width * .24f, height * .14f, 4, 4));
        float lensSize = width * .26f;
        graphics.setColor(Color.WHITE);
        graphics.fill(new Ellipse2D.Float(width * .5f - lensSize / 2, height * .53f - lensSize / 2, lensSize, lensSize));
        graphics.setColor(INK);
        float pupilSize = lensSize * .5f;
        graphics.fill(new Ellipse2D.Float(width * .5f - pupilSize / 2, height * .53f - pupilSize / 2, pupilSize, pupilSize));
    }

    public static void paintPencil(Graphics2D graphics, int width, int height) {
        graphics.translate(width / 2.0, height / 2.0);
        graphics.scale(width / 28.0, height / 28.0);
        graphics.rotate(Math.toRadians(45));
        graphics.setColor(PENCIL_GREEN);
        graphics.fill(new RoundRectangle2D.Float(-3, -9, 6, 14, 2, 2));
        Path2D pencilTip = new Path2D.Float();
        pencilTip.moveTo(-3, 5);
        pencilTip.lineTo(3, 5);
        pencilTip.lineTo(0, 11);
        pencilTip.closePath();
        graphics.fill(pencilTip);
        graphics.setColor(PENCIL_GREEN.brighter());
        graphics.fill(new RoundRectangle2D.Float(-3, -12, 6, 4, 2, 2));
    }

    public static void paintNextArrow(Graphics2D graphics, int width, int height) {
        graphics.setColor(ARROW_GRAY);
        graphics.fillOval(0, 0, width, height);
        graphics.setColor(Color.WHITE);
        graphics.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        graphics.drawLine((int) (width * .28), height / 2, (int) (width * .72), height / 2);
        graphics.drawLine((int) (width * .54), (int) (height * .30), (int) (width * .72), height / 2);
        graphics.drawLine((int) (width * .54), (int) (height * .70), (int) (width * .72), height / 2);
    }

    public static void paintHeart(Graphics2D graphics, int width, int height, boolean filled) {
        float heartWidth = width - 2;
        float heartHeight = height - 2;
        graphics.translate(1, 1);
        Path2D heartShape = new Path2D.Float();
        heartShape.moveTo(heartWidth * .5, heartHeight * .88);
        heartShape.curveTo(-heartWidth * .08, heartHeight * .45, heartWidth * .12, heartHeight * .02,
                heartWidth * .5, heartHeight * .3);
        heartShape.curveTo(heartWidth * .88, heartHeight * .02, heartWidth * 1.08, heartHeight * .45,
                heartWidth * .5, heartHeight * .88);
        if (filled) {
            graphics.setColor(HEART_FILLED);
            graphics.fill(heartShape);
        } else {
            graphics.setColor(HEART_OUTLINE);
            graphics.setStroke(new BasicStroke(1.4f));
            graphics.draw(heartShape);
        }
    }

    public static void paintRounded(Graphics graphics, Color fill, int width, int height, int cornerDiameter) {
        Graphics2D roundedShapeGraphics = (Graphics2D) graphics.create();
        roundedShapeGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        roundedShapeGraphics.setColor(fill);
        roundedShapeGraphics.fillRoundRect(0, 0, width, height, cornerDiameter, cornerDiameter);
        roundedShapeGraphics.dispose();
    }

    public static void addPageHeader(JPanel panel, String title, Runnable onBack) {
        JButton backButton = iconButton("Back", loadScaledIcon("back", 32, 32), onBack);
        backButton.setBounds(17, 22, 36, 36);
        panel.add(backButton);
        JLabel heading = label(title, UI_FONT.deriveFont(Font.BOLD, 26f));
        heading.setBounds(65, 22, WIDTH - 130, 36);
        panel.add(heading);
    }
}