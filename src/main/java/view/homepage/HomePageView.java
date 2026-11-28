
package view.homepage;


import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.font.TextAttribute;
import java.awt.geom.Ellipse2D;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/** Home page: date header, tab strip, user profile card and recommended quests. */
public final class HomePageView extends JPanel {

    public enum Tab {
        // History("progress"), Current Quests, Home, Journal, Add Side Quest, Map
        HISTORY("history"), CURRENT("Current Quest"), JOUNRAL("Journal"), HOME("\uD83C\uDFE0"),
        ADD_SIDE_QUEST("Add Side Quest"), MAP("Map");

        private final String label;

        Tab(String label) {
            this.label = label;
        }
    }

    public static final class RecommendedQuest {
        private final String questName;
        private final Runnable onViewQuest;
        private final Consumer<Boolean> onFavoriteChange;

        public RecommendedQuest(String questName, Runnable onViewQuest, Consumer<Boolean> onFavoriteChange) {
            this.questName = questName;
            this.onViewQuest = onViewQuest;
            this.onFavoriteChange = onFavoriteChange;
        }
    }

    private static final int PROFILE_Y = 120;
    private static final int RECOMMENDED_Y = 620;
    private static final int RECOMMENDED_CARD_WIDTH = 114;
    private static final int RECOMMENDED_CARD_HEIGHT = 105;
    private static final int RECOMMENDED_CARD_GAP = 8;
    private static final int RECOMMENDED_WHEEL_PIXELS = 40;

    private final JLabel usernameLabel = HomePageStyle.label("Username", HomePageStyle.UI_FONT.deriveFont(20f));
    private final JLabel levelLabel = HomePageStyle.label("LEVEL 1", HomePageStyle.UI_FONT.deriveFont(11f)
            .deriveFont(Collections.singletonMap(TextAttribute.TRACKING, 0.12f)));
    private final JLabel xpLabel = HomePageStyle.label("0/100 XP", HomePageStyle.UI_FONT.deriveFont(14f));
    private final JComponent xpBar = createXpBar();
    private final JComponent avatarView = createAvatarView();
    private final JPanel recommendedCardsPanel = new JPanel();
    private final JScrollPane recommendedScrollPane = new JScrollPane(recommendedCardsPanel);
    private Image avatarImage;
    private double xpProgress;
    private double pendingRecommendedScrollPixels;
    private int dragStartScreenX;
    private int dragStartViewX;
    private Runnable onEditUsername = () -> { };
    private Runnable onChangePhoto = () -> { };

    // Lets the recommended row be dragged with the mouse, wherever on a card the drag starts.
    private final MouseAdapter recommendedDragScroller = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent event) {
            dragStartScreenX = event.getXOnScreen();
            dragStartViewX = recommendedScrollPane.getViewport().getViewPosition().x;
        }

        @Override
        public void mouseDragged(MouseEvent event) {
            setRecommendedScrollX(dragStartViewX - (event.getXOnScreen() - dragStartScreenX));
        }
    };

    public HomePageView(final Runnable returnToHome, final Consumer<Tab> selectHomePageTab) {
        setLayout(null);
        setBackground(HomePageStyle.PAPER);
        setPreferredSize(new Dimension(HomePageStyle.WIDTH, HomePageStyle.HEIGHT));
        // Keep your own title line here if yours differs from this one.
        HomePageStyle.addPageHeader(this, LocalDate.now().toString(), returnToHome);
        add(createStatusTabStrip(selectHomePageTab));
        add(createUserProfilePanel());
        add(createRecommendedQuestsSection());
    }

    private static JLabel createStatusTabStrip(final Consumer<Tab> selectHomePageTab) {
        final JLabel statusTabStrip = new JLabel(HomePageStyle.loadScaledIcon("tabs", HomePageStyle.WIDTH, 40));
        statusTabStrip.setLayout(new GridLayout(1, Tab.values().length));
        statusTabStrip.setBounds(0, 72, HomePageStyle.WIDTH, 40);
        final ButtonGroup statusButtonGroup = new ButtonGroup();
        for (Tab homePageTab : Tab.values()) {
            final JToggleButton statusTabButton = new JToggleButton(homePageTab.label);
            statusTabButton.setContentAreaFilled(false);
            statusTabButton.setBorderPainted(false);
            statusTabButton.setFocusPainted(false);
            statusTabButton.setSelected(homePageTab == Tab.HOME);
            statusTabButton.setFont(HomePageStyle.UI_FONT
                    .deriveFont(statusTabButton.isSelected() ? Font.BOLD : Font.PLAIN, 12f));
            statusTabButton.addItemListener(selectionChange -> statusTabButton.setFont(statusTabButton.getFont()
                    .deriveFont(statusTabButton.isSelected() ? Font.BOLD : Font.PLAIN)));
            statusTabButton.addActionListener(tabActivation -> selectHomePageTab.accept(homePageTab));
            statusButtonGroup.add(statusTabButton);
            statusTabStrip.add(statusTabButton);
        }
        return statusTabStrip;
    }

    // ----- Public API for later linking ---------------------------------------------------

    public void setProfileActions(Runnable onEditUsername, Runnable onChangePhoto) {
        this.onEditUsername = onEditUsername;
        this.onChangePhoto = onChangePhoto;
    }

    public void showUserProfile(String username, int level, int currentXp, int xpForNextLevel) {
        usernameLabel.setText(username);
        usernameLabel.setToolTipText(username);
        levelLabel.setText("LEVEL " + level);
        xpProgress = xpForNextLevel <= 0 ? 0 : Math.max(0, Math.min(1, (double) currentXp / xpForNextLevel));
        xpLabel.setText(currentXp + "/" + xpForNextLevel + " XP");
        xpBar.repaint();
    }

    /** Pass null to go back to the placeholder picture. */
    public void setAvatar(Image avatar) {
        avatarImage = avatar;
        avatarView.repaint();
    }

    public void showRecommendedQuests(List<RecommendedQuest> quests) {
        pendingRecommendedScrollPixels = 0;
        recommendedCardsPanel.removeAll();
        recommendedCardsPanel.add(Box.createHorizontalStrut(6));
        if (quests.isEmpty()) {
            recommendedCardsPanel.add(HomePageStyle.label("No recommendations yet.", HomePageStyle.UI_FONT.deriveFont(14f)));
        }
        for (int questIndex = 0; questIndex < quests.size(); questIndex++) {
            if (questIndex > 0) {
                recommendedCardsPanel.add(Box.createHorizontalStrut(RECOMMENDED_CARD_GAP));
            }
            recommendedCardsPanel.add(createRecommendedCard(quests.get(questIndex)));
        }
        recommendedCardsPanel.add(Box.createHorizontalStrut(6));
        recommendedScrollPane.getViewport().setViewPosition(new Point());
        recommendedCardsPanel.revalidate();
        recommendedCardsPanel.repaint();
    }

    // ----- Profile card -------------------------------------------------------------------

    private JPanel createUserProfilePanel() {
        JPanel profileCard = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics graphics) {
                HomePageStyle.paintRounded(graphics, HomePageStyle.CARD_GREEN, getWidth(), getHeight(), 60);
            }
        };
        profileCard.setOpaque(false);
        profileCard.setBounds(7, PROFILE_Y, 400, 153);

        // Added before the avatar so the camera badge paints on top of it.
        JButton changePhotoButton = HomePageStyle.paintedButton("Change photo", HomePageStyle::paintCameraBadge,
                () -> onChangePhoto.run());
        changePhotoButton.setBounds(95, 98, 44, 44);
        profileCard.add(changePhotoButton);

        avatarView.setBounds(22, 18, 117, 117);
        profileCard.add(avatarView);

        usernameLabel.setHorizontalAlignment(SwingConstants.LEFT);
        usernameLabel.setBounds(152, 30, 136, 32);
        profileCard.add(usernameLabel);

        JButton editUsernameButton = HomePageStyle.paintedButton("Edit username", HomePageStyle::paintPencil,
                () -> onEditUsername.run());
        editUsernameButton.setBounds(292, 32, 28, 28);
        profileCard.add(editUsernameButton);

        levelLabel.setHorizontalAlignment(SwingConstants.LEFT);
        levelLabel.setForeground(HomePageStyle.PROFILE_MUTED);
        levelLabel.setBounds(152, 62, 200, 16);
        profileCard.add(levelLabel);

        xpBar.setBounds(152, 86, 128, 28);
        profileCard.add(xpBar);

        xpLabel.setHorizontalAlignment(SwingConstants.LEFT);
        xpLabel.setBounds(288, 86, 104, 28);
        profileCard.add(xpLabel);
        return profileCard;
    }

    private JComponent createAvatarView() {
        return new JComponent() {
            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics2D avatarGraphics = (Graphics2D) graphics.create();
                avatarGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                avatarGraphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                        RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                int avatarW = getWidth();
                int avatarH = getHeight();
                avatarGraphics.setClip(new Ellipse2D.Float(0, 0, avatarW, avatarH));
                if (avatarImage == null) {
                    avatarGraphics.setPaint(new GradientPaint(0, 0, new Color(0xbfe0f5), 0, avatarH, new Color(0xeaf5fb)));
                    avatarGraphics.fillRect(0, 0, avatarW, avatarH);
                    avatarGraphics.setColor(new Color(0xa8c957));
                    avatarGraphics.fill(new Ellipse2D.Float(-avatarW * .2f, avatarH * .6f, avatarW * 1.4f, avatarH));
                } else {
                    int imageW = Math.max(1, avatarImage.getWidth(null));
                    int imageH = Math.max(1, avatarImage.getHeight(null));
                    double scale = Math.max((double) avatarW / imageW, (double) avatarH / imageH);
                    int drawW = (int) Math.round(imageW * scale);
                    int drawH = (int) Math.round(imageH * scale);
                    avatarGraphics.drawImage(avatarImage, (avatarW - drawW) / 2, (avatarH - drawH) / 2, drawW, drawH, null);
                }
                avatarGraphics.dispose();
            }
        };
    }

    private JComponent createXpBar() {
        return new JComponent() {
            @Override
            protected void paintComponent(Graphics graphics) {
                HomePageStyle.paintRounded(graphics, HomePageStyle.XP_TRACK, getWidth(), getHeight(), getHeight());
                int fillWidth = (int) Math.round(getWidth() * xpProgress);
                if (fillWidth > 0) {
                    HomePageStyle.paintRounded(graphics, HomePageStyle.XP_FILL,
                            Math.max(fillWidth, getHeight()), getHeight(), getHeight());
                }
            }
        };
    }

    // ----- Recommended quests -------------------------------------------------------------

    private JPanel createRecommendedQuestsSection() {
        JPanel recommendedSection = new JPanel(null);
        recommendedSection.setOpaque(false);
        recommendedSection.setBounds(0, RECOMMENDED_Y, HomePageStyle.WIDTH, 165);

        JLabel recommendedHeading = HomePageStyle.label("Recommended for you", HomePageStyle.UI_FONT.deriveFont(24f));
        recommendedHeading.setHorizontalAlignment(SwingConstants.LEFT);
        recommendedHeading.setBounds(14, 0, HomePageStyle.WIDTH - 28, 36);
        recommendedSection.add(recommendedHeading);

        recommendedCardsPanel.setLayout(new BoxLayout(recommendedCardsPanel, BoxLayout.X_AXIS));
        recommendedCardsPanel.setOpaque(false);
        recommendedCardsPanel.addMouseListener(recommendedDragScroller);
        recommendedCardsPanel.addMouseMotionListener(recommendedDragScroller);
        recommendedScrollPane.setBorder(null);
        recommendedScrollPane.setOpaque(false);
        recommendedScrollPane.getViewport().setOpaque(false);
        recommendedScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        recommendedScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        // The mouse wheel moves the row sideways; Swing's default would scroll vertically.
        recommendedScrollPane.setWheelScrollingEnabled(false);
        recommendedScrollPane.addMouseWheelListener(this::scrollRecommendedWithWheel);
        recommendedScrollPane.setBounds(8, 46, 362, RECOMMENDED_CARD_HEIGHT + 4);
        recommendedSection.add(recommendedScrollPane);

        JButton nextButton = HomePageStyle.paintedButton("Next recommendations", HomePageStyle::paintNextArrow,
                this::scrollRecommendedQuests);
        nextButton.setBounds(376, 46 + (RECOMMENDED_CARD_HEIGHT - 30) / 2, 30, 30);
        recommendedSection.add(nextButton);
        return recommendedSection;
    }

    private int maxRecommendedScrollX() {
        return Math.max(0, recommendedCardsPanel.getPreferredSize().width
                - recommendedScrollPane.getViewport().getWidth());
    }

    private void setRecommendedScrollX(int scrollX) {
        int clampedScrollX = Math.max(0, Math.min(scrollX, maxRecommendedScrollX()));
        recommendedScrollPane.getViewport().setViewPosition(new Point(clampedScrollX, 0));
    }

    private void scrollRecommendedWithWheel(MouseWheelEvent event) {
        double wheelRotation = event.getPreciseWheelRotation();
        if (wheelRotation == 0) {
            return;
        }
        // Accumulate fractions so small trackpad movements are not lost.
        pendingRecommendedScrollPixels += wheelRotation * RECOMMENDED_WHEEL_PIXELS;
        int wholePixelsToScroll = (int) pendingRecommendedScrollPixels;
        pendingRecommendedScrollPixels -= wholePixelsToScroll;
        setRecommendedScrollX(recommendedScrollPane.getViewport().getViewPosition().x + wholePixelsToScroll);
        event.consume();
    }

    /** Arrow button: moves one card to the right, and jumps back to the start from the end. */
    private void scrollRecommendedQuests() {
        int currentX = recommendedScrollPane.getViewport().getViewPosition().x;
        if (currentX >= maxRecommendedScrollX()) {
            setRecommendedScrollX(0);
        } else {
            setRecommendedScrollX(currentX + RECOMMENDED_CARD_WIDTH + RECOMMENDED_CARD_GAP);
        }
    }

    private JPanel createRecommendedCard(RecommendedQuest quest) {
        JPanel recommendedCard = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics graphics) {
                HomePageStyle.paintRounded(graphics, HomePageStyle.RECOMMENDED_CARD, getWidth(), getHeight(), 36);
            }
        };
        recommendedCard.setOpaque(false);
        recommendedCard.setAlignmentY(TOP_ALIGNMENT);
        Dimension cardSize = new Dimension(RECOMMENDED_CARD_WIDTH, RECOMMENDED_CARD_HEIGHT);
        recommendedCard.setMinimumSize(cardSize);
        recommendedCard.setPreferredSize(cardSize);
        recommendedCard.setMaximumSize(cardSize);
        recommendedCard.addMouseListener(recommendedDragScroller);
        recommendedCard.addMouseMotionListener(recommendedDragScroller);

        JTextPane questNamePane = new JTextPane();
        questNamePane.setText(quest.questName.toUpperCase());
        StyledDocument questNameDocument = questNamePane.getStyledDocument();
        SimpleAttributeSet centeredText = new SimpleAttributeSet();
        StyleConstants.setAlignment(centeredText, StyleConstants.ALIGN_CENTER);
        questNameDocument.setParagraphAttributes(0, questNameDocument.getLength(), centeredText, false);
        questNamePane.setFont(HomePageStyle.UI_FONT.deriveFont(12f));
        questNamePane.setForeground(HomePageStyle.RECOMMENDED_TEXT);
        questNamePane.setEditable(false);
        questNamePane.setFocusable(false);
        questNamePane.setOpaque(false);
        questNamePane.setBorder(null);
        questNamePane.setToolTipText(quest.questName);
        questNamePane.setBounds(10, 24, RECOMMENDED_CARD_WIDTH - 20, 42);
        questNamePane.addMouseListener(recommendedDragScroller);
        questNamePane.addMouseMotionListener(recommendedDragScroller);
        recommendedCard.add(questNamePane);

        JToggleButton favoriteButton = new JToggleButton() {
            @Override
            protected void paintComponent(Graphics graphics) {
                Graphics2D heartGraphics = (Graphics2D) graphics.create();
                heartGraphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                HomePageStyle.paintHeart(heartGraphics, getWidth(), getHeight(), isSelected());
                heartGraphics.dispose();
            }
        };
        favoriteButton.getAccessibleContext().setAccessibleName("Favourite");
        favoriteButton.setToolTipText("Favourite");
        favoriteButton.setOpaque(false);
        favoriteButton.setBorderPainted(false);
        favoriteButton.setContentAreaFilled(false);
        favoriteButton.setFocusPainted(false);
        favoriteButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        favoriteButton.addActionListener(favoriteToggle -> {
            if (quest.onFavoriteChange != null) {
                quest.onFavoriteChange.accept(favoriteButton.isSelected());
            }
        });
        favoriteButton.setBounds(RECOMMENDED_CARD_WIDTH - 28, 6, 20, 20);
        recommendedCard.add(favoriteButton);

        JButton viewQuestButton = HomePageStyle.roundedButton("View Quest", Color.WHITE,
                quest.onViewQuest == null ? () -> { } : quest.onViewQuest);
        viewQuestButton.setBounds((RECOMMENDED_CARD_WIDTH - 88) / 2, RECOMMENDED_CARD_HEIGHT - 26 - 9, 88, 26);
        recommendedCard.add(viewQuestButton);
        return recommendedCard;
    }
}