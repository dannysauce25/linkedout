package use_case.journal;

/** The draft to save for a user and quest. */
public final class SaveJournalEntryInputData {
    private final String userId;
    private final String questId;
    private final String reflection;
    private final byte[] photo;

    public SaveJournalEntryInputData(String userId, String questId, String reflection, byte[] photo) {
        this.userId = userId;
        this.questId = questId;
        this.reflection = reflection;
        this.photo = photo == null ? null : photo.clone();
    }

    public String getUserId() {
        return userId;
    }

    public String getQuestId() {
        return questId;
    }

    public String getReflection() {
        return reflection;
    }

    public byte[] getPhoto() {
        return photo == null ? null : photo.clone();
    }
}
