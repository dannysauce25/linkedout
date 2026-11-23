package use_case.recommend;

import entity.SideQuest;
import java.util.List;

public interface QuestDataAccess {
    List<SideQuest> getAllQuests();
    List<String> getAllTags();   // the view uses this to build the interest checkboxes
}