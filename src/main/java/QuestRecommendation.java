import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import entity.SideQuest;

public class QuestRecommendation {

    public static void main(String[] args) {

        List<String> availableTags = List.of("1");

        Questionnaire questionnaire = new Questionnaire(availableTags);
        questionnaire.start();
        UserPreferences preferences = questionnaire.getPreferences();

        System.out.println("\nYour answers: " + preferences);
    }

    public static class Questionnaire {

        private final Scanner scanner = new Scanner(System.in);
        private final UserPreferences preferences = new UserPreferences();
        private final List<String> availableTags;

        Questionnaire(List<String> availableTags) {
            this.availableTags = availableTags;
        }

        private void start() {
            System.out.println("What kind of sidequests would you like?");
            askInterests();
            askActivityType();
            askTimeAvailability();
            askBudget();
        }

        private void askInterests() {
            System.out.println("Type your interests, separated by commas.");
            System.out.println("Available: " + String.join(", ", availableTags));
            System.out.print("> ");

            String line = scanner.nextLine();
            ArrayList<String> chosen = new ArrayList<>();

            for (String part : line.split(",")) {
                String typed = part.trim().toLowerCase();
                for (String tag : availableTags) {
                    if (typed.equals(tag.toLowerCase()) && !chosen.contains(tag)) {
                        chosen.add(tag);
                    }
                }
            }
            preferences.setInterests(chosen);
        }

        private void askActivityType() {
            String activityType = askString(
                    "Do you prefer indoor or outdoor quests?",
                    List.of("indoor", "outdoor", "either"));
            preferences.setActivityType(activityType);
        }

        private void askTimeAvailability() {
            int minutes = askInt("How many minutes do you have available?", 1, 24 * 60);
            preferences.setAvailableTime(minutes);
        }

        private void askBudget() {
            double budget = askDouble("What's your budget? (0 for free quests only)", 0);
            preferences.setBudget(budget);
        }

        private String askString(String question, List<String> options) {
            while (true) {
                System.out.print(question + " (" + String.join("/", options) + "): ");
                String answer = scanner.nextLine().trim().toLowerCase();
                if (!answer.isEmpty()) {
                    for (String option : options) {
                        if (answer.equals(option.toLowerCase())) {
                            return option;
                        }
                    }
                }
                System.out.println("Please choose one of: " + String.join(", ", options) + ".");
            }
        }

        private int askInt(String question, int min, int max) {
            while (true) {
                System.out.print(question + " (" + min + "-" + max + "): ");
                try {
                    int value = Integer.parseInt(scanner.nextLine().trim());
                    if (value >= min && value <= max) {
                        return value;
                    }
                } catch (NumberFormatException e) {
                    // fall through to the error message
                }
                System.out.println("Please enter a whole number between " + min + " and " + max + ".");
            }
        }

        private double askDouble(String question, double min) {
            while (true) {
                System.out.print(question + ": ");
                try {
                    double value = Double.parseDouble(scanner.nextLine().trim());
                    if (value >= min) {
                        return value;
                    }
                } catch (NumberFormatException e) {
                    // fall through to the error message
                }
                System.out.println("Please enter a number that is at least " + min + ".");
            }
        }

        private UserPreferences getPreferences() {
            UserPreferences preference = new UserPreferences();
            return preference;
        }

    }

    public static class UserPreferences {

        private ArrayList<String> interests;
        private String activityType;
        private String socialPreference;
        private int availableTime;
        private double budget;

        private String getActivityType() {
            return activityType;
        }

        private String getSocialPreference() {
            return socialPreference;
        }

        private int getAvailableTime() {
            return availableTime;
        }

        private double getBudget() {
            return budget;
        }

        private ArrayList<String> getInterests() {
            return interests;
        }

        private void setActivityType(String activityType) {
            this.activityType = activityType;
        }

        private void setSocialPreference(String socialPreference) {
            this.socialPreference = socialPreference;
        }

        private void setAvailableTime(int availableTime) {
            this.availableTime = availableTime;
        }

        private void setBudget(double budget) {
            this.budget = budget;
        }

        private void setInterests(ArrayList<String> interests) {
            this.interests = interests;
        }

    }

    public static class RecommendationEngine {

        private static final double INTEREST_POINTS = 5.0;
        private static final double ACTIVITY_POINTS = 2.0;

        public ArrayList<SideQuest> recommend(List<SideQuest> allQuests, UserPreferences prefs) {
            ArrayList<ScoredQuest> scored = new ArrayList<>();

            for (SideQuest quest : allQuests) {
                if (!isPossible(quest, prefs)) {
                    continue;
                }
                scored.add(new ScoredQuest(quest, score(quest, prefs)));
            }

            Collections.shuffle(scored);
            scored.sort((a, b) -> Double.compare(b.score, a.score));

            ArrayList<SideQuest> result = new ArrayList<>();
            for (ScoredQuest s : scored) {
                result.add(s.quest);
            }
            return result;
        }

        private boolean isPossible(SideQuest quest, UserPreferences prefs) {
            return quest.getExpectedTime() <= prefs.getAvailableTime()
                    && quest.getBudget() <= prefs.getBudget();
        }

        private double score(SideQuest quest, UserPreferences prefs) {
            double points = 0;

            // Interests: fraction of the user's interests the quest covers
            List<String> questInterests = quest.getInterests();   // copy once, not per loop
            List<String> userInterests = prefs.getInterests();
            if (!userInterests.isEmpty()) {
                int matches = 0;
                for (String interest : userInterests) {
                    if (questInterests.contains(interest)) {
                        matches++;
                    }
                }
                points += INTEREST_POINTS * matches / userInterests.size();
            }

            if (matches(prefs.getActivityType(), quest.isIndoor())) {
                points += ACTIVITY_POINTS;
            }

            return points;
        }

        private boolean matches(String preferred, String actual) {
            return preferred.equals("either")
                    || actual.equals("either")
                    || preferred.equals(actual);
        }

        private static class ScoredQuest {
            final SideQuest quest;
            final double score;

            ScoredQuest(SideQuest quest, double score) {
                this.quest = quest;
                this.score = score;
            }
        }
    }
}