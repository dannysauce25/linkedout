package use_case.recommend;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import entity.SideQuest;
import entity.UserPreferences;

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
        private static final int MAX_INTERESTS = 10;

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
            ArrayList<String> chosen = new ArrayList<>();
            int limit = Math.min(MAX_INTERESTS, availableTags.size());

            System.out.println("Available interests: " + String.join(", ", availableTags));
            System.out.println("Enter up to " + limit + " interests, one at a time. Type 'done' to finish.");

            while (chosen.size() < limit) {
                System.out.print("Interest " + (chosen.size() + 1) + "/" + limit + ": ");
                String typed = scanner.nextLine().trim().toLowerCase();

                if (typed.isEmpty() || typed.equals("done")) {
                    break;
                }

                String match = null;
                for (String tag : availableTags) {
                    if (typed.equals(tag.toLowerCase())) {
                        match = tag;   // keep the tag exactly as written in the list
                        break;
                    }
                }

                if (match == null) {
                    System.out.println("'" + typed + "' isn't a recognized interest, ignoring it.");
                } else if (chosen.contains(match)) {
                    System.out.println("You already added '" + match + "'.");
                } else {
                    chosen.add(match);
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
            String availableTime = askString("How much time do you have available?",
                    List.of("less than 10 min", "less than 30 min",
                            "less than 60 min", "more than 60 min"));
            preferences.setAvailableTime(availableTime);
        }

        private void askBudget() {
            String budget = askString("What's your budget?",
                    List.of("free", "less than 10 CAD",
                    "less than 50 CAD", "more than 50 CAD"));
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

        private UserPreferences getPreferences() {
            return preferences;
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
            //return quest.getExpectedTime().equals(prefs.getAvailableTime())
                    //&& quest.getBudget() == prefs.getBudget();
            return true;
        }

        private double score(SideQuest quest, UserPreferences prefs) {
            double points = 0;

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