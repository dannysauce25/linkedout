package use_case.add_side_quest;

import static org.junit.jupiter.api.Assertions.assertEquals;

import entity.Location;
import entity.SideQuest;
import java.util.List;
import org.junit.jupiter.api.Test;

class AddSideQuestInputDataTest {

    @Test
    void questEnteredByUserIsCustom() {
        AddSideQuestInputData inputData = new AddSideQuestInputData("Feed the ducks", "outdoor",
                0, 20, 40, new Location(43.6629, -79.3957), List.of("nature"));

        assertEquals(SideQuest.CUSTOM_QUEST, inputData.getQuestType());
        assertEquals("custom quest", inputData.getQuestType());
    }
}
