package view.homepage;


import javax.swing.*;
import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/** Standalone preview with sample quests. */
public final class HomePagePreview {
    private final CardLayout screenLayout = new CardLayout();
    private final JPanel screenContainer = new JPanel(screenLayout);
    private final HomePageView homePageView = new HomePageView(() -> screenLayout.show(screenContainer, "home"),
            this::showQuests);

    private HomePagePreview() {
        screenContainer.add(createHomePanel(), "home");
        screenContainer.add(homePageView, "homePage");
        showQuests(HomePageView.Tab.HOME);
        screenLayout.show(screenContainer, "homePage");
    }

    private JPanel createHomePanel() {
        final JPanel homePanel = new JPanel(new GridBagLayout());
        homePanel.setBackground(HomePageStyle.PAPER);
        final GridBagConstraints buttonLayout = new GridBagConstraints();
        buttonLayout.ipadx = 50;
        buttonLayout.ipady = 12;
        homePanel.add(HomePageStyle.roundedButton("Journal", HomePageStyle.PHOTO_BUTTON,
                () -> screenLayout.show(screenContainer, "homePage")), buttonLayout);
        return homePanel;
    }

    private void showQuests(HomePageView.Tab tab) {
        List<String> questNames;
        switch (tab) {
            case HOME:
                questNames = Arrays.asList("Visit a new café", "Browse a local bookshop", "Take a sunset walk",
                        "Try a new recipe", "Explore a neighbourhood park", "Visit a farmers market",
                        "Try a new workout", "Sketch a street scene");
                break;
//            case :
//                questNames = List.of("Find a new weekend trail", "Finish a library book");
//                break;
//            case CANCELLED:
//                questNames = List.of("Visit the night market");
//                break;
            default:
                throw new AssertionError(tab);
        }
        homePageView.showQuests(questNames.stream()
                .map(questName -> new HomePageView.QuestRow(questName, () -> { }))
                .collect(Collectors.toList()));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            final HomePagePreview preview = new HomePagePreview();
            final JFrame frame = new JFrame("Linked Out · Journal Preview");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            preview.screenContainer.setPreferredSize(new Dimension(HomePageStyle.WIDTH, HomePageStyle.HEIGHT));
            frame.setContentPane(preview.screenContainer);
            frame.setResizable(false);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
