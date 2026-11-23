package entity;

import java.util.ArrayList;
import java.util.List;

public class UserPreferences {

    public static final int MAX_INTERESTS = 10;

    // Activity type
    public static final String INDOOR = "indoor";
    public static final String OUTDOOR = "outdoor";
    public static final String EITHER = "either";

    // Available time
    public static final String TIME_UNDER_10 = "less than 10 min";
    public static final String TIME_UNDER_30 = "less than 30 min";
    public static final String TIME_UNDER_HOUR = "less than an hour";
    public static final String TIME_OVER_HOUR = "more than an hour";

    // Budget
    public static final String BUDGET_FREE = "free";
    public static final String BUDGET_UNDER_10 = "less than 10 CAD";
    public static final String BUDGET_UNDER_50 = "less than 50 CAD";
    public static final String BUDGET_OVER_50 = "more than 50 CAD";

    // Lists of valid options, in display order (handy for combo boxes and validation)
    public static final List<String> ACTIVITY_OPTIONS = List.of(INDOOR, OUTDOOR, EITHER);
    public static final List<String> TIME_OPTIONS =
            List.of(TIME_UNDER_10, TIME_UNDER_30, TIME_UNDER_HOUR, TIME_OVER_HOUR);
    public static final List<String> BUDGET_OPTIONS =
            List.of(BUDGET_FREE, BUDGET_UNDER_10, BUDGET_UNDER_50, BUDGET_OVER_50);

    private List<String> interests = new ArrayList<>();
    private String activityType;
    private String availableTime;
    private String budget;

    public String getActivityType() {
        return activityType;
    }

    public String getAvailableTime() {
        return availableTime;
    }

    public String getBudget() {
        return budget;
    }

    public List<String> getInterests() {
        return List.copyOf(interests);
    }

    public void setActivityType(String activityType) {
        this.activityType = activityType;
    }

    public void setAvailableTime(String availableTime) {
        this.availableTime = availableTime;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    public void setInterests(List<String> interests) {
        if (interests.size() > MAX_INTERESTS) {
            throw new IllegalArgumentException(
                    "At most " + MAX_INTERESTS + " interests allowed, got " + interests.size());
        }
        this.interests = new ArrayList<>(interests);
    }
}