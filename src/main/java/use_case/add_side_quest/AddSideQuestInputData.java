package use_case.add_side_quest;
import entity.Location;
import entity.SideQuest;
import java.util.List;
import java.util.ArrayList;

public class AddSideQuestInputData {
    private final String questDescription;
    private final String isIndoor;
    private final double budget;
    private final double expectedTime;
    private final Location location;
    private final List<String> interests;
    private final String questType;

    public AddSideQuestInputData(String questDescription,
                                 String isIndoor,
                                 double budget,         // This is in CAD
                                 double expectedTime,   // This is in minutes
                                 Location location,
                                 List<String> interests) {
        this.questDescription = questDescription;
        this.isIndoor = isIndoor;
        this.budget = budget;
        this.expectedTime = expectedTime;
        this.location = location;
        this.interests = interests == null ? new ArrayList<>() : new ArrayList<>(interests);
        // A quest entered by the user is always a custom quest.
        this.questType = SideQuest.CUSTOM_QUEST;
    }

    public String getQuestDescription() {
        return questDescription;
    }

    public String getIsIndoor() {
        return isIndoor;
    }

    public double getBudget() {
        return budget;
    }

    public double getExpectedTime() {
        return expectedTime;
    }

    public Location getLocation() {
        return location;
    }

    public List<String> getInterests() {
        return new ArrayList<>(interests);
    }

    public String getQuestType() {
        return questType;
    }
}