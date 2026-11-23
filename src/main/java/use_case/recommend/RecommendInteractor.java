package use_case.recommend;

import entity.SideQuest;
import entity.UserPreferences;
import java.util.List;

public class RecommendInteractor implements RecommendInputBoundary {

    private final QuestDataAccess dataAccess;
    private final RecommendOutputBoundary presenter;
    private final RecommendationEngine engine = new RecommendationEngine();

    public RecommendInteractor(QuestDataAccess dataAccess, RecommendOutputBoundary presenter) {
        this.dataAccess = dataAccess;
        this.presenter = presenter;
    }

    @Override
    public void execute(RecommendInputData input) {
        // 1. Validate the raw input
        if (input.getActivityType() == null
                || input.getAvailableTime() == null
                || input.getBudget() == null) {
            presenter.prepareFailView("Please answer all questions.");
            return;
        }

        // 2. Build the entity
        UserPreferences prefs = new UserPreferences();
        try {
            prefs.setInterests(input.getInterests());
        } catch (IllegalArgumentException e) {
            presenter.prepareFailView(
                    "Please choose at most " + UserPreferences.MAX_INTERESTS + " interests.");
            return;
        }
        prefs.setActivityType(input.getActivityType());
        prefs.setAvailableTime(input.getAvailableTime());
        prefs.setBudget(input.getBudget());

        // 3. Rank the quests
        List<SideQuest> ranked = engine.recommend(dataAccess.getAllQuests(), prefs);

        // 4. Report the result
        if (ranked.isEmpty()) {
            presenter.prepareFailView("No quests match. Try a bigger budget or more time.");
        } else {
            presenter.prepareSuccessView(new RecommendOutputData(ranked));
        }
    }
}