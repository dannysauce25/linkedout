package use_case.recommend;

import java.util.List;

public class RecommendInputData {
    private final List<String> interests;
    private final String activityType;
    private final String availableTime;
    private final String budget;

    public RecommendInputData(List<String> interests, String activityType,
                              String availableTime, String budget) {
        this.interests = interests;
        this.activityType = activityType;
        this.availableTime = availableTime;
        this.budget = budget;
    }

    public List<String> getInterests() { return interests; }
    public String getActivityType() { return activityType; }
    public String getAvailableTime() { return availableTime; }
    public String getBudget() { return budget; }
}