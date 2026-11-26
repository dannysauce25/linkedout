package use_case.add_side_quest;
import entity.SideQuest;

import java.util.List;

public interface AddSideQuestDataAccessInterface {

    /**
     * Saves a side quest.
     *
     * @param sideQuest the side quest to save
     * @throws IllegalArgumentException if a side quest with the same id already exists
     */
    void save(SideQuest sideQuest);

    /**
     * Returns every saved side quest.
     *
     * @return all side quests, or an empty list if there are none
     */
    List<SideQuest> getAllQuests();

    /**
     * Returns the side quest with the given id.
     *
     * @param id the id of the side quest
     * @return the side quest, or null if no side quest has that id
     */
    SideQuest getQuestById(String id);

    /**
     * Returns every interest used by at least one side quest, without duplicates.
     *
     * @return the interests in alphabetical order
     */
    List<String> getAllTags();

    /**
     * Returns the id to give the next new side quest.
     *
     * @return one more than the largest saved id, or "1" if there are no side quests
     */
    String getNextId();
}
