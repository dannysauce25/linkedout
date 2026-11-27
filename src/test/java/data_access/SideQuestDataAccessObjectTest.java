package data_access;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import entity.Location;
import entity.SideQuest;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

class SideQuestDataAccessObjectTest {
    private static final String HEADER =
            "name,id,isIndoor,budget,expectedTime,exp,progressLevel,location,interests,isStarred,questType\n";
    private static final String GERSTEIN =
            "Borrow a new book from Gerstein Library,1,indoor,free,under_10_minutes,20,not started,"
            + "\"43.6622, -79.3934\",\"academic, library\",false,default quest\n";
    private static final String ROBARTS =
            "\"Go to Robarts Library, and get coffee\",2,indoor,less_than_twenty,under_60_minutes,60,"
            + "not started,,,false,default quest\n";

    @TempDir
    Path tempDir;

    private Path csvFile;
    private SideQuestDataAccessObject dao;

    @BeforeEach
    void setUp() throws IOException {
        csvFile = tempDir.resolve("side-quests.csv");
        Files.writeString(csvFile, HEADER + GERSTEIN + ROBARTS);
        dao = new SideQuestDataAccessObject(csvFile.toString());
    }

    private static SideQuest customQuest(String id, String name) {
        return new SideQuest(name, id, "outdoor", "free", "under_30_minutes", "in progress",
                new Location(43.66, -79.39), List.of("nature", "relaxation"), true, SideQuest.CUSTOM_QUEST);
    }

    @Test
    void loadsEveryQuestExceptTheHeader() {
        assertEquals(2, dao.getAllQuests().size());
    }

    @Test
    void loadsAllColumnsOfAQuest() {
        SideQuest quest = dao.getQuestById("1");

        assertEquals("Borrow a new book from Gerstein Library", quest.getName());
        assertEquals("1", quest.getId());
        assertEquals("indoor", quest.getIsIndoor());
        assertEquals("free", quest.getBudget());
        assertEquals("under_10_minutes", quest.getExpectedTime());
        assertEquals(20, quest.getExp());
        assertEquals("not started", quest.getProgressLevel());
        assertEquals(new Location(43.6622, -79.3934), quest.getLocation());
        assertEquals(List.of("academic", "library"), quest.getInterests());
        assertFalse(quest.isStarred());
        assertEquals(SideQuest.DEFAULT_QUEST, quest.getQuestType());
    }

    @Test
    void emptyLocationAndInterestsLoadAsNullAndEmpty() {
        SideQuest quest = dao.getQuestById("2");

        assertNull(quest.getLocation());
        assertTrue(quest.getInterests().isEmpty());
    }

    @Test
    void nameWithACommaLoadsInOnePiece() {
        assertEquals("Go to Robarts Library, and get coffee", dao.getQuestById("2").getName());
    }

    @Test
    void skipsBlankLines() throws IOException {
        Files.writeString(csvFile, HEADER + GERSTEIN + "\n" + ROBARTS + "\n");

        assertEquals(2, dao.getAllQuests().size());
    }

    @Test
    void returnsNullForAnUnknownId() {
        assertNull(dao.getQuestById("999"));
    }

    @Test
    void savedQuestReadsBackTheSame() {
        SideQuest saved = customQuest("3", "Feed the ducks, then nap");

        dao.save(saved);
        SideQuest loaded = dao.getQuestById("3");

        assertEquals(saved.getName(), loaded.getName());
        assertEquals(saved.getIsIndoor(), loaded.getIsIndoor());
        assertEquals(saved.getBudget(), loaded.getBudget());
        assertEquals(saved.getExpectedTime(), loaded.getExpectedTime());
        assertEquals(saved.getExp(), loaded.getExp());
        assertEquals(saved.getProgressLevel(), loaded.getProgressLevel());
        assertEquals(saved.getLocation(), loaded.getLocation());
        assertEquals(saved.getInterests(), loaded.getInterests());
        assertEquals(saved.isStarred(), loaded.isStarred());
        assertEquals(saved.getQuestType(), loaded.getQuestType());
        assertEquals(3, dao.getAllQuests().size());
    }

    @Test
    void savesAQuestWithoutALocation() {
        dao.save(new SideQuest("Read a book", "3", "indoor", "free", "over_60_minutes", "not started",
                null, List.of(), false, SideQuest.CUSTOM_QUEST));

        assertNull(dao.getQuestById("3").getLocation());
    }

    @Test
    void rejectsSavingADuplicateId() {
        assertThrows(IllegalArgumentException.class, () -> dao.save(customQuest("1", "Another quest")));
        assertEquals(2, dao.getAllQuests().size());
    }

    @Test
    void tagsHaveNoDuplicatesAndAreAlphabetical() {
        dao.save(customQuest("3", "Feed the ducks"));
        dao.save(customQuest("4", "Watch the sunset"));

        assertEquals(List.of("academic", "library", "nature", "relaxation"), dao.getAllTags());
    }

    @Test
    void nextIdIsOneMoreThanTheLargestId() {
        assertEquals("3", dao.getNextId());
    }

    @Test
    void nextIdComparesIdsAsNumbers() {
        dao.save(customQuest("9", "Quest nine"));
        dao.save(customQuest("10", "Quest ten"));

        assertEquals("11", dao.getNextId());
    }

    @Test
    void nextIdIsOneWhenThereAreNoQuests() throws IOException {
        Files.writeString(csvFile, HEADER);

        assertEquals("1", dao.getNextId());
    }

    @Test
    void updateReplacesOnlyTheMatchingQuest() {
        SideQuest updated = new SideQuest("Borrow a new book from Gerstein Library", "1", "indoor", "free",
                "under_10_minutes", "completed", new Location(43.6622, -79.3934),
                List.of("academic", "library"), true, SideQuest.DEFAULT_QUEST);

        dao.updateSideQuest(updated);

        SideQuest loaded = dao.getQuestById("1");
        assertEquals("completed", loaded.getProgressLevel());
        assertTrue(loaded.isStarred());
        assertEquals("Go to Robarts Library, and get coffee", dao.getQuestById("2").getName());
        assertEquals(2, dao.getAllQuests().size());
    }

    @Test
    void updateKeepsTheHeaderRow() throws IOException {
        dao.updateSideQuest(customQuest("2", "Changed name"));

        assertEquals(HEADER.trim(), Files.readAllLines(csvFile).get(0));
    }

    @Test
    void updateRejectsAnUnknownId() {
        assertThrows(IllegalArgumentException.class, () -> dao.updateSideQuest(customQuest("999", "Missing")));
    }

    @Test
    void missingFileThrowsUncheckedIoException() {
        SideQuestDataAccessObject missing =
                new SideQuestDataAccessObject(tempDir.resolve("does-not-exist.csv").toString());

        assertThrows(UncheckedIOException.class, missing::getAllQuests);
    }
}
