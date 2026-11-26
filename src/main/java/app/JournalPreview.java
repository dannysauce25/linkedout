package app;

import data_access.FileJournalEntryRepository;
import view.journal.JournalPanel;
import view.journal.JournalPreviewPanel;
import view.journal.JournalView;

import java.nio.file.Path;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/** Runs the journal with sample quests and local storage. */
public final class JournalPreview {
    private JournalPreview() {
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Path storage = Path.of(System.getProperty("user.home"), ".linkedout", "journal");
            FileJournalEntryRepository repository = new FileJournalEntryRepository(storage);
            AppBuilder builder = new AppBuilder();
            JournalPreviewPanel preview = new JournalPreviewPanel(returnHome ->
                    builder.buildJournal(repository, "preview", JournalPreview::sampleQuests, returnHome));

            JFrame frame = new JFrame("Linked Out · Journal Preview");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setContentPane(preview);
            frame.setResizable(false);
            frame.pack();
            WindowPlacement.fitAndCenter(frame);
            frame.setVisible(true);
        });
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
}
