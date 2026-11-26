package entity;
import java.util.List;

//From Pauline: Please add the following attributes and methods:
//IsStarred, not a final attribute is set by the user by clicking a button (aka I believe this should be a public attribute) 
    //1. Any quest regardless of its progress level can be starred and can be unstarred at any time. 
    //2. The default for any side quest will be unstarred.

public class SideQuest {
    public static final String CUSTOM_QUEST = "custom quest";   // entered by a user
    public static final String DEFAULT_QUEST = "default quest"; // ships with the app

    private final String name;
    private final String id; // A unique ID for quests
    private final String isIndoor;  // isIndoor is a String as there is a possibility of the activity being both indoors and outdoors
    private final String budget;    // Split into four categories (free, less_than_twenty, less_than_fifty, over_fifty)
    private final String expectedTime; // under_10_minutes, under_30_minutes, under_60_minutes, over_60_minutes
    private final int exp;
    //ProgressLevel, follows the following business rules follow
    // 1.  There are only 4 statuses: Not Started, In progress, Completed, Cancelled
    // 2.  Once a side quest is completed or cancelled is cannot be undone by the user
    // 3.  A "In progress" side quest will remain in progress until it is cancelled or completed
    private int progressLevel;
    private final Location location; // latitude and longitude, validated by Location
    private final List<String> interests;
    private boolean isStarred;
    private final String questType; // either CUSTOM_QUEST or DEFAULT_QUEST

    public SideQuest(String name,
                     String id,
                     String isIndoor,
                     String budget,
                     String expectedTime,
                     int exp,
                     int progressLevel,
                     Location location,
                     List<String> interests,
                     boolean isStarred,
                     String questType) {

        // validates the number of interests
        if (interests.size() > 5) {
            throw new IllegalArgumentException("A side quest cannot have more than 5 interests.");
        }

        // validates the quest type
        if (!CUSTOM_QUEST.equals(questType) && !DEFAULT_QUEST.equals(questType)) {
            throw new IllegalArgumentException(
                    "Quest type must be \"" + CUSTOM_QUEST + "\" or \"" + DEFAULT_QUEST + "\".");
        }

        this.name = name;
        this.id = id;
        this.isIndoor = isIndoor;
        this.budget = budget;
        this.expectedTime = expectedTime;
        this.exp = exp;
        this.progressLevel = progressLevel;
        this.location = location;
        this.interests = List.copyOf(interests);
        this.isStarred = isStarred;
        this.questType = questType;

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBudget() {
        return budget;
    }

    public String isIndoor() {
        return isIndoor;
    }

    public String getExpectedTime() {
        return expectedTime;
    }

    public int getExp() {
        return exp;
    }

    public Location getLocation() {
        return location;
    }

    public List<String> getInterests() {
        return List.copyOf(interests);
    }

    public int getProgressLevel() {
        return progressLevel;
    }

    public boolean isStarred() {
        return isStarred;
    }

    public String getQuestType() {
        return questType;
    }
}
