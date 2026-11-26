package view.journal;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.function.Function;
import javax.swing.JPanel;

/** Shows the journal and a sample home screen in the standalone preview. */
public final class JournalPreviewPanel extends JPanel {
    private final CardLayout screens = new CardLayout();

    public JournalPreviewPanel(Function<Runnable, JournalPanel> createJournal) {
        setLayout(screens);
        setPreferredSize(new Dimension(JournalStyle.WIDTH, JournalStyle.HEIGHT));
        add(createHomePanel(), "home");
        add(createJournal.apply(() -> screens.show(this, "home")), "journal");
        screens.show(this, "journal");
    }

    private JPanel createHomePanel() {
        JPanel homePanel = new JPanel(new GridBagLayout());
        homePanel.setBackground(JournalStyle.PAPER);
        GridBagConstraints buttonLayout = new GridBagConstraints();
        buttonLayout.ipadx = 50;
        buttonLayout.ipady = 12;
        homePanel.add(JournalStyle.roundedButton("Journal", JournalStyle.PHOTO_BUTTON,
                () -> screens.show(this, "journal")), buttonLayout);
        return homePanel;
    }
}
