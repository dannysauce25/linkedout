package view.journal;

import data_access.FileJournalEntryRepository;
import interface_adapter.journal.JournalEntryController;
import use_case.journal.JournalEntryInteractor;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.nio.file.Path;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/** Runs the journal with sample quests and local storage. */
public final class JournalPreview {
    private final CardLayout screenLayout = new CardLayout();
    private final JPanel screenContainer = new JPanel(screenLayout);

    private JournalPreview() {
        Path storage = Path.of(System.getProperty("user.home"), ".linkedout", "journal");
        JournalEntryInteractor interactor = new JournalEntryInteractor(new FileJournalEntryRepository(storage));
        JournalEntryController controller = new JournalEntryController("preview", interactor, interactor);
        JournalPanel journal = new JournalPanel(controller, JournalPreview::sampleQuests,
                () -> screenLayout.show(screenContainer, "home"));
        screenContainer.add(createHomePanel(), "home");
        screenContainer.add(journal, "journal");
        screenLayout.show(screenContainer, "journal");
    }

    private JPanel createHomePanel() {
        JPanel homePanel = new JPanel(new GridBagLayout());
        homePanel.setBackground(JournalStyle.PAPER);
        GridBagConstraints buttonLayout = new GridBagConstraints();
        buttonLayout.ipadx = 50;
        buttonLayout.ipady = 12;
        homePanel.add(JournalStyle.roundedButton("Journal", JournalStyle.PHOTO_BUTTON,
                () -> screenLayout.show(screenContainer, "journal")), buttonLayout);
        return homePanel;
    }

    private static List<JournalPanel.Quest> sampleQuests(JournalView.Tab tab) {
        switch (tab) {
            case COMPLETED:
                return List.of(new JournalPanel.Quest("cafe", "Visit a new café"),
                        new JournalPanel.Quest("bookshop", "Browse a local bookshop"),
                        new JournalPanel.Quest("sunset", "Take a sunset walk"),
                        new JournalPanel.Quest("recipe", "Try a new recipe"),
                        new JournalPanel.Quest("park", "Explore a neighbourhood park"),
                        new JournalPanel.Quest("farmers-market", "Visit a farmers market"),
                        new JournalPanel.Quest("workout", "Try a new workout"),
                        new JournalPanel.Quest("sketch", "Sketch a street scene"));
            case IN_PROGRESS:
                return List.of(new JournalPanel.Quest("trail", "Find a new weekend trail"),
                        new JournalPanel.Quest("library", "Finish a library book"));
            case CANCELLED:
                return List.of(new JournalPanel.Quest("night-market", "Visit the night market"));
            default:
                throw new AssertionError(tab);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JournalPreview preview = new JournalPreview();
            JFrame frame = new JFrame("Linked Out · Journal Preview");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            preview.screenContainer.setPreferredSize(new Dimension(JournalStyle.WIDTH, JournalStyle.HEIGHT));
            frame.setContentPane(preview.screenContainer);
            frame.setResizable(false);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
