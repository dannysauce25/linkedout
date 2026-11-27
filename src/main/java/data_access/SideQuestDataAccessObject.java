package data_access;

import com.opencsv.CSVWriter;
import com.opencsv.CSVWriterBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;       // lets you skip the header line: .withSkipLines(1)
import com.opencsv.exceptions.CsvException; // readAll() can throw this as well as IOException
import entity.Location;

import entity.SideQuest;
import use_case.add_side_quest.AddSideQuestDataAccessInterface;

import java.io.*;
import java.nio.file.*;
import java.util.*;


public class SideQuestDataAccessObject implements AddSideQuestDataAccessInterface {
    private static final int NAME = 0;
    private static final int ID = 1;
    private static final int IS_INDOOR = 2;
    private static final int BUDGET = 3;
    private static final int EXPECTED_TIME = 4;
    private static final int EXP = 5;
    private static final int PROGRESS_LEVEL = 6;
    private static final int LOCATION = 7;
    private static final int INTERESTS = 8;
    private static final int IS_STARRED = 9;
    private static final int QUEST_TYPE = 10;

    private static final String[] HEADER = {"name", "id", "isIndoor", "budget", "expectedTime", "exp",
        "progressLevel", "location", "interests", "isStarred", "questType"};

    private static final String SEPARATOR = ", ";

    private final Path csvFile;

    /**
     * @param filePath for now: "src/main/resources/database/add-side-quest.csv"
     *                 (use a plain file path, not getResource(): files inside the classpath/jar
     *                 can be read but not written to).
     */
    public SideQuestDataAccessObject(String filePath) {
        this.csvFile = Path.of(filePath);
    }

    /**
     * Appends one quest as a new row at the end of the CSV.
     */
    @Override
    public void save(SideQuest sideQuest) {
        String[] row = serializeQuest(sideQuest);
        if (getQuestById(sideQuest.getId()) != null) {
            throw new IllegalArgumentException("A side quest of the same id already exists.");
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile.toFile(), true))) {
            writer.writeNext(row, false);
        } catch (IOException e) {
            throw new UncheckedIOException("Could not save this Side Quest.", e);
        }

    }

    /**
     * Replaces the saved row that has the same id as the given quest, and rewrites the CSV.
     *
     * @param sideQuest the new version of an already saved quest
     * @throws IllegalArgumentException if no saved quest has that id
     */
    public void updateSideQuest(SideQuest sideQuest) {
        List<String[]> rows;
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(csvFile.toFile())).withSkipLines(1).build()) {
            rows = reader.readAll();
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot retrieve all Side Quests.", e);
        } catch (CsvException e) {
            throw new RuntimeException("Cannot parse the CSV file.", e);
        }

        boolean found = false;
        for (int i = 0; i < rows.size(); i++) {
            String[] row = rows.get(i);
            if (row.length > ID && row[ID].equals(sideQuest.getId())) {
                rows.set(i, serializeQuest(sideQuest));
                found = true;
            }
        }
        if (!found) {
            throw new IllegalArgumentException("No side quest has the id " + sideQuest.getId() + ".");
        }

        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFile.toFile(), false))) {
            writer.writeNext(HEADER, false);
            for (String[] row : rows) {
                if (!(row.length == 1 && row[0].isBlank())) {
                    writer.writeNext(row, false);
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Could not update this Side Quest.", e);
        }
    }

    /**
     * Reads every row (except the header) and turns it into a SideQuest.
     */
    @Override
    public List<SideQuest> getAllQuests() {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(csvFile.toFile())).withSkipLines(1).build()) {
            List<String[]> rows = reader.readAll();

            List<SideQuest> sideQuests = new ArrayList<>();
            for (String[] row : rows) {
                // Skips blank lines in the database
                if (!(row.length == 1 && row[0].isBlank())){
                    sideQuests.add(deserializeQuest(row));
                }
            }
            return sideQuests;

        } catch (IOException e) {
            throw new UncheckedIOException("Cannot retrieve all Side Quests.", e);
        } catch (CsvException e) {
            throw new RuntimeException("Cannot parse the CSV file.", e);
        }
    }

    /**
     * Finds one quest by its id (the "accessed by the id" part of the old class description).
     *
     * @return the quest, or null if there is no quest with that id
     */
    @Override
    public SideQuest getQuestById(String id) {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(csvFile.toFile())).withSkipLines(1).build()) {
            List<String[]> rows = reader.readAll();

            // Reads every line sequentially to find the id of the quest
            for (String[] row : rows) {
                // row.length check skips blank lines, since they do not have id values
                if (row.length > ID && row[ID].equals(id)){
                    return deserializeQuest(row);
                }
            }
            return null;
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot retrieve all Side Quests.", e);
        } catch (CsvException e) {
            throw new RuntimeException("Cannot parse the CSV file.", e);
        }
    }

    /**
     * Every interest/tag used by at least one quest, without duplicates.
     * Useful for the "pick up to 5 tags" screen in Add Side Quest.
     */
    @Override
    public List<String> getAllTags() {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(csvFile.toFile())).withSkipLines(1).build()) {
            List<String[]> rows = reader.readAll();

            // TreeSet removes duplicates and keeps the tags in alphabetical order
            Set<String> tags = new TreeSet<>();
            // Reads every line sequentially to add the interests of the quest into the set
            for (String[] row : rows) {
                if (!(row.length == 1 && row[0].isBlank())) {
                    tags.addAll(deserializeQuest(row).getInterests());
                }
            }
            return new ArrayList<>(tags);

        } catch (IOException e) {
            throw new UncheckedIOException("Cannot retrieve all Side Quests.", e);
        } catch (CsvException e) {
            throw new RuntimeException("Cannot parse the CSV file.", e);
        }
    }

    /**
     * The id to give the next new quest: one more than the largest id in the file, or "1" if empty.
     * This is what the commented-out getLastId() code in AddSideQuestInteractor is reaching for;
     * right now every user quest gets the id "0".
     * If the interactor should call it, add it to AddSideQuestDataAccessInterface too.
     */
    @Override
    public String getNextId() {
        try (CSVReader reader = new CSVReaderBuilder(new FileReader(csvFile.toFile())).withSkipLines(1).build()) {
            // Reads every line sequentially, and keeps the largest id (0 if the file has no quests)
            List<String[]> rows = reader.readAll();
            int largestId = 0;
            for (String[] row : rows) {
                if (row.length > ID && !row[ID].isBlank()) {
                    // Compare ids as numbers, since as text "9" > "10"
                    largestId = Math.max(largestId, Integer.parseInt(row[ID].trim()));
                }
            }
            // Increment one from the largest id
            return String.valueOf(largestId + 1);
        } catch (IOException e) {
            throw new UncheckedIOException("Cannot retrieve all Side Quests.", e);
        } catch (CsvException e) {
            throw new RuntimeException("Cannot parse the CSV file.", e);
        }
    }

    /**
     * SideQuest -> String[] in exactly the HEADER order
     */
    private String[] serializeQuest(SideQuest sideQuest) {
        String[] row = new String[HEADER.length];
        row[NAME] = sideQuest.getName();
        row[ID] = sideQuest.getId();
        row[IS_INDOOR] = sideQuest.getIsIndoor();
        row[BUDGET] = sideQuest.getBudget();
        row[EXPECTED_TIME] = sideQuest.getExpectedTime();
        row[EXP] = String.valueOf(sideQuest.getExp());
        row[PROGRESS_LEVEL] = sideQuest.getProgressLevel();

        // A quest without coordinates is saved with an empty location cell
        Location location = sideQuest.getLocation();
        row[LOCATION] = location == null ? ""
                : location.getLatitude() + SEPARATOR + location.getLongitude();

        row[INTERESTS] = String.join(SEPARATOR, sideQuest.getInterests());
        row[IS_STARRED] = String.valueOf(sideQuest.isStarred());
        row[QUEST_TYPE] = sideQuest.getQuestType();
        return row;
    }

    /**
     * String[] (one CSV row) -> SideQuest. The reverse of serializeQuest.
     */
    private SideQuest deserializeQuest(String[] row) {
        String name = row[NAME];
        String id = row[ID];
        String isIndoor = row[IS_INDOOR];
        String budget = row[BUDGET];
        String expectedTime = row[EXPECTED_TIME];
        String progressLevel = row[PROGRESS_LEVEL];

        Location location = null;
        if (!row[LOCATION].isBlank()) {
            String[] coordinates = row[LOCATION].split(",");
            double latitude = Double.parseDouble(coordinates[0].trim());
            double longitude = Double.parseDouble(coordinates[1].trim());
            location = new Location(latitude, longitude);
        }

        // e.g. "food, campus" becomes ["food", "campus"], or an empty list when the cell is empty
        List<String> interests = new ArrayList<>();
        if (!row[INTERESTS].isBlank()) {
            for (String interest : row[INTERESTS].split(",")) {
                interests.add(interest.trim());
            }
        }

        boolean isStarred = Boolean.parseBoolean(row[IS_STARRED].trim());
        String questType = row[QUEST_TYPE];

        return new SideQuest(name, id, isIndoor, budget, expectedTime, progressLevel, location, interests,
                isStarred, questType);
    }
}
