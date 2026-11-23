package use_case.recommend;

import entity.SideQuest;
import entity.UserPreferences;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RecommendationEngine {

    private static final double INTEREST_POINTS = 5.0;
    private static final double ACTIVITY_POINTS = 2.0;

    public List<SideQuest> recommend(List<SideQuest> allQuests, UserPreferences prefs) {
        List<ScoredQuest> scored = new ArrayList<>();
        for (SideQuest quest : allQuests) {
            if (isPossible(quest, prefs)) {
                scored.add(new ScoredQuest(quest, score(quest, prefs)));
            }
        }
        Collections.shuffle(scored);   // random order among ties (sort is stable)
        scored.sort((a, b) -> Double.compare(b.score, a.score));

        List<SideQuest> result = new ArrayList<>();
        for (ScoredQuest s : scored) {
            result.add(s.quest);
        }
        return result;
    }

    private boolean isPossible(SideQuest quest, UserPreferences prefs) {
        return quest.getExpectedTime() <= maxMinutes(prefs.getAvailableTime())
                && quest.getBudget() <= maxBudget(prefs.getBudget());
    }

    private double maxMinutes(String category) {
        switch (category) {
            case UserPreferences.TIME_UNDER_10:   return 10;
            case UserPreferences.TIME_UNDER_30:   return 30;
            case UserPreferences.TIME_UNDER_HOUR: return 60;
            default:                              return Double.MAX_VALUE;
        }
    }

    private double maxBudget(String category) {
        switch (category) {
            case UserPreferences.BUDGET_FREE:     return 0;
            case UserPreferences.BUDGET_UNDER_10: return 10;
            case UserPreferences.BUDGET_UNDER_50: return 50;
            default:                              return Double.MAX_VALUE;
        }
    }

    private double score(SideQuest quest, UserPreferences prefs) {
        double points = 0;
        List<String> questInterests = quest.getInterests();
        List<String> userInterests = prefs.getInterests();

        if (!userInterests.isEmpty()) {
            int matches = 0;
            for (String interest : userInterests) {
                if (questInterests.contains(interest)) {
                    matches++;
                }
            }
            points += INTEREST_POINTS * matches / userInterests.size();
        }
        if (activityMatches(prefs.getActivityType(), quest.isIndoor())) {
            points += ACTIVITY_POINTS;
        }
        return points;
    }

    private boolean activityMatches(String preferred, String actual) {
        return preferred.equals(UserPreferences.EITHER)
                || actual.equals(UserPreferences.EITHER)
                || preferred.equals(actual);
    }

    private static class ScoredQuest {
        final SideQuest quest;
        final double score;
        ScoredQuest(SideQuest quest, double score) {
            this.quest = quest;
            this.score = score;
        }
    }
}