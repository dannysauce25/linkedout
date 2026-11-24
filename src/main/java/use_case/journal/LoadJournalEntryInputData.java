package use_case.journal;

/** Identifies the entry to open. */
public final class LoadJournalEntryInputData {
    private final String userId;
    private final String questId;

    public LoadJournalEntryInputData(String userId, String questId) {
        this.userId = userId;
        this.questId = questId;
    }

    public String getUserId() {
        return userId;
    }

    public String getQuestId() {
        return questId;
    }
}
