package view.journal;

import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FileDialog;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;

import interface_adapter.journal.JournalEntryForm;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/** Edits the contents of a single quest's journal page. */
public final class JournalEntryView extends JPanel {
    private static final int CONTENT_HEIGHT = 712;

    private final JournalTextArea entryTextArea;
    private final JLabel photoPreview;
    private BufferedImage photo;

    public JournalEntryView(final String questName, final JournalEntryForm initialContents,
                            final Consumer<JournalEntryForm> saveEntry, final Runnable returnToJournal) {
        setLayout(null);
        setBackground(JournalStyle.PAPER);
        setPreferredSize(new Dimension(JournalStyle.WIDTH, CONTENT_HEIGHT));
        JournalStyle.addPageHeader(this, "Journal Entry", returnToJournal);

        final JLabel questNameLabel = JournalStyle.label(questName, JournalStyle.UI_FONT.deriveFont(14f));
        questNameLabel.setBounds(55, 57, 304, 23);
        questNameLabel.setToolTipText(questName);
        add(questNameLabel);

        final JButton insertPhotoButton = JournalStyle.roundedButton("Insert Photo", JournalStyle.PHOTO_BUTTON,
                this::choosePhoto);
        insertPhotoButton.setBounds(142, 84, 130, 24);
        add(insertPhotoButton);

        photo = initialContents.getPhoto();
        photoPreview = createPhotoPreview();
        add(photoPreview);

        entryTextArea = new JournalTextArea("Journal entry", initialContents.getReflection(),
                "Add a description for your side quest...", 366, 275);
        add(createEntryTextPanel(entryTextArea));

        JButton saveButton = JournalStyle.roundedButton("Save", JournalStyle.PHOTO_BUTTON,
                () -> saveEntry.accept(new JournalEntryForm(entryTextArea.getText(), photo)));
        saveButton.setBounds(142, 664, 130, 32);
        add(saveButton);

        JButton removePhotoButton = JournalStyle.roundedButton("Remove Photo", JournalStyle.PAPER, () -> {
            photo = null;
            photoPreview.repaint();
        });
        removePhotoButton.setBounds(142, 315, 130, 24);
        add(removePhotoButton);
    }

    private JLabel createPhotoPreview() {
        final JLabel photoPreviewLabel = new JLabel(JournalStyle.loadScaledIcon("polaroid", 168, 196)) {
            @Override
            protected void paintComponent(Graphics graphics) {
                super.paintComponent(graphics);
                if (photo != null) {
                    paintPhoto(graphics);
                }
            }
        };
        photoPreviewLabel.getAccessibleContext().setAccessibleName("Journal photo preview");
        photoPreviewLabel.setBounds(123, 119, 168, 196);
        return photoPreviewLabel;
    }

    private static JPanel createEntryTextPanel(final JournalTextArea entryTextArea) {
        final JPanel entryTextPanel = createRoundedGreenPanel(42);
        entryTextPanel.setBounds(10, 347, 394, 313);
        entryTextArea.setBounds(14, 19, 366, 275);
        entryTextPanel.add(entryTextArea);
        return entryTextPanel;
    }

    private void choosePhoto() {
        Window owner = SwingUtilities.getWindowAncestor(this);
        FileDialog photoPicker = owner instanceof Dialog
                ? new FileDialog((Dialog) owner, "Insert Photo", FileDialog.LOAD)
                : new FileDialog((Frame) owner, "Insert Photo", FileDialog.LOAD);
        photoPicker.setDirectory(new File(System.getProperty("user.home"), "Downloads").getPath());
        photoPicker.setVisible(true);
        File[] selectedFiles = photoPicker.getFiles();
        photoPicker.dispose();
        if (selectedFiles.length == 0) {
            return;
        }
        try {
            BufferedImage selectedImage = ImageIO.read(selectedFiles[0]);
            if (selectedImage == null) {
                throw new IOException("Unsupported image");
            }
            photo = selectedImage;
            photoPreview.repaint();
        } catch (IOException exception) {
            showPhotoError();
        }
    }

    private void paintPhoto(Graphics graphics) {
        Graphics2D photoGraphics = (Graphics2D) graphics.create();
        int width = 151;
        int height = 144;
        double scale = Math.max((double) width / photo.getWidth(), (double) height / photo.getHeight());
        int scaledWidth = (int) Math.ceil(photo.getWidth() * scale);
        int scaledHeight = (int) Math.ceil(photo.getHeight() * scale);
        photoGraphics.clipRect(8, 9, width, height);
        photoGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        photoGraphics.drawImage(photo, 8 + (width - scaledWidth) / 2, 9 + (height - scaledHeight) / 2,
                scaledWidth, scaledHeight, null);
        photoGraphics.dispose();
    }

    private void showPhotoError() {
        JDialog promptDialog = createModalDialog("Photo could not be opened");
        JPanel content = createRoundedGreenPanel(36);
        content.setPreferredSize(new Dimension(360, 110));
        JLabel message = JournalStyle.label("Choose a readable image, such as a JPEG or PNG.",
                JournalStyle.UI_FONT.deriveFont(13f));
        message.setBounds(0, 14, 360, 30);
        content.add(message);
        JButton dismissButton = JournalStyle.roundedButton("OK", JournalStyle.PAPER, promptDialog::dispose);
        dismissButton.setBounds(134, 57, 92, 33);
        content.add(dismissButton);
        showDialog(promptDialog, content, dismissButton);
    }

    private JDialog createModalDialog(String dialogTitle) {
        JDialog modalDialog = new JDialog(SwingUtilities.getWindowAncestor(this), dialogTitle,
                Dialog.ModalityType.APPLICATION_MODAL);
        modalDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        return modalDialog;
    }

    private void showDialog(JDialog modalDialog, JPanel content, JButton defaultButton) {
        modalDialog.setContentPane(content);
        modalDialog.getRootPane().setDefaultButton(defaultButton);
        modalDialog.pack();
        modalDialog.setLocationRelativeTo(this);
        modalDialog.setVisible(true);
    }

    private static JPanel createRoundedGreenPanel(int arc) {
        JPanel panel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics graphics) {
                JournalStyle.paintRounded(graphics, JournalStyle.GREEN, getWidth(), getHeight(), arc);
            }
        };
        panel.setOpaque(false);
        return panel;
    }
}
