package view.homepage;


import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

/** Standalone preview with sample data. */
public final class HomePagePreview {
    private final CardLayout screenLayout = new CardLayout();
    private final JPanel screenContainer = new JPanel(screenLayout);
    private final HomePageView homePageView = new HomePageView(() -> screenLayout.show(screenContainer, "home"),
            tab -> { });

    private HomePagePreview() {
        screenContainer.add(createHomePanel(), "home");
        screenContainer.add(homePageView, "homePage");
        homePageView.showUserProfile("Username", 1, 40, 120);
        homePageView.showRecommendedQuests(Arrays.asList(
                new HomePageView.RecommendedQuest("Try out a new cafe", () -> { }, null),
                new HomePageView.RecommendedQuest("Browse a bookshop", () -> { }, null),
                new HomePageView.RecommendedQuest("Take a sunset walk", () -> { }, null),
                new HomePageView.RecommendedQuest("Find a new trail", () -> { }, null),
                new HomePageView.RecommendedQuest("Finish a library book", () -> { }, null),
                new HomePageView.RecommendedQuest("Visit the night market", () -> { }, null)));
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            final HomePagePreview preview = new HomePagePreview();
            final JFrame frame = new JFrame("Linked Out · Home Preview");
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