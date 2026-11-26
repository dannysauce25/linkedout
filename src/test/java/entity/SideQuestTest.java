package entity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class SideQuestTest {
    private static final Location UOFT = new Location(43.6629, -79.3957);

    private static SideQuest questWithInterests(List<String> interests) {
        return new SideQuest("Read in Queen's Park", "12", "false", "free",
                "under_60_minutes", 10, 0, UOFT, interests, false, SideQuest.DEFAULT_QUEST);
    }

    private static SideQuest questWithType(String questType) {
        return new SideQuest("Read in Queen's Park", "12", "false", "free",
                "under_60_minutes", 10, 0, UOFT, List.of("reading"), false, questType);
    }

    @Test
    void storesAllConstructorValues() {
        SideQuest quest = new SideQuest("Visit the ROM", "quest-7", "true", "less_than_twenty",
                "over_60_minutes", 25, 1, UOFT, List.of("museum", "history"), true,
                SideQuest.CUSTOM_QUEST);

        assertEquals("Visit the ROM", quest.getName());
        assertEquals("quest-7", quest.getId());
        assertEquals("true", quest.isIndoor());
        assertEquals("less_than_twenty", quest.getBudget());
        assertEquals("over_60_minutes", quest.getExpectedTime());
        assertEquals(25, quest.getExp());
        assertEquals(1, quest.getProgressLevel());
        assertSame(UOFT, quest.getLocation());
        assertEquals(List.of("museum", "history"), quest.getInterests());
        assertTrue(quest.isStarred());
        assertEquals("custom quest", quest.getQuestType());
    }

    @Test
    void storesUnstarredQuest() {
        assertFalse(questWithInterests(List.of("reading")).isStarred());
    }

    @Test
    void nameAndIdAreNotSwapped() {
        SideQuest quest = questWithInterests(List.of());
        assertEquals("Read in Queen's Park", quest.getName());
        assertEquals("12", quest.getId());
    }

    @Test
    void acceptsEmptyInterests() {
        assertTrue(questWithInterests(List.of()).getInterests().isEmpty());
    }

    @Test
    void changingTheOriginalInterestListDoesNotChangeTheQuest() {
        List<String> interests = new ArrayList<>(List.of("reading", "outdoors"));
        SideQuest quest = questWithInterests(interests);

        interests.add("music");
        interests.set(0, "gaming");

        assertEquals(List.of("reading", "outdoors"), quest.getInterests());
    }

    @Test
    void returnedInterestsCannotBeModified() {
        SideQuest quest = questWithInterests(List.of("reading"));

        assertThrows(UnsupportedOperationException.class,
                () -> quest.getInterests().add("music"));
        assertEquals(List.of("reading"), quest.getInterests());
    }

    @Test
    void interestsKeepTheirOrder() {
        SideQuest quest = questWithInterests(List.of("c", "a", "b"));
        assertEquals(List.of("c", "a", "b"), quest.getInterests());
    }

    @Test
    void acceptsFiveInterests() {
        SideQuest quest = questWithInterests(List.of("museum", "history", "education", "day trip", "anthropology"));
        assertEquals(5, quest.getInterests().size());
    }

    @Test
    void rejectsMoreThanFiveInterests() {
        assertThrows(IllegalArgumentException.class,
                () -> questWithInterests(List.of("museum", "history", "education", "day trip",
                "anthropology", "toronto")));
    }

    @Test
    void acceptsBothQuestTypes() {
        assertEquals("custom quest", questWithType("custom quest").getQuestType());
        assertEquals("default quest", questWithType("default quest").getQuestType());
    }

    @Test
    void rejectsUnknownQuestType() {
        assertThrows(IllegalArgumentException.class, () -> questWithType("side quest"));
        assertThrows(IllegalArgumentException.class, () -> questWithType("Custom Quest"));
        assertThrows(IllegalArgumentException.class, () -> questWithType(""));
    }

    @Test
    void rejectsNullQuestType() {
        assertThrows(IllegalArgumentException.class, () -> questWithType(null));
    }

    @Test
    void rejectsNullInterestList() {
        assertThrows(NullPointerException.class, () -> questWithInterests(null));
    }

    @Test
    void rejectsNullInterestEntry() {
        assertThrows(NullPointerException.class,
                () -> questWithInterests(Arrays.asList("reading", null)));
    }
}
