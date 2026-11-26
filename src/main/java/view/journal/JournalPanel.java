package view.journal;

import interface_adapter.journal.JournalEntryController;
import interface_adapter.journal.JournalEntryForm;

import java.awt.CardLayout;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/** Journal screens for one signed-in user and their quests. */
public final class JournalPanel extends JPanel {
    public static final class Quest {
        private final String id;
        private final String name;

        public Quest(String id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    private final CardLayout screens = new CardLayout();
    private final JournalEntryController controller;
    private final Function<JournalView.Tab, List<Quest>> questsByStatus;
    private final JournalView journalView;
    private JScrollPane entryScrollPane;

    public JournalPanel(JournalEntryController controller,
                        Function<JournalView.Tab, List<Quest>> questsByStatus, Runnable returnHome) {
        this.controller = controller;
        this.questsByStatus = questsByStatus;
        setLayout(screens);
        journalView = new JournalView(returnHome, this::showQuests);
        add(journalView, "journal");
        showQuests(JournalView.Tab.COMPLETED);
    }

    private void showQuests(JournalView.Tab tab) {
        journalView.showQuests(questsByStatus.apply(tab).stream()
                .map(quest -> new JournalView.QuestRow(quest.name, () -> openEntry(quest)))
                .collect(Collectors.toList()));
    }

    public void openEntry(Quest quest) {
        controller.load(quest.id, contents -> showEntry(quest, contents), this::showError);
    }

    private void showEntry(Quest quest, JournalEntryForm contents) {
        if (entryScrollPane != null) {
            remove(entryScrollPane);
        }
        JournalEntryView entryView = new JournalEntryView(quest.name, contents,
                edited -> controller.save(quest.id, edited,
                        () -> JOptionPane.showMessageDialog(this, "Journal entry saved."), this::showError),
                () -> screens.show(this, "journal"));
        entryScrollPane = new JScrollPane(entryView);
        entryScrollPane.setBorder(null);
        entryScrollPane.getVerticalScrollBar().setUnitIncrement(20);
        add(entryScrollPane, "entry");
        screens.show(this, "entry");
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Journal Entry", JOptionPane.ERROR_MESSAGE);
    }
}
