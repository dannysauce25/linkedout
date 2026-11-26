package entity;

/** A saved reflection or photo associated with a user and a side quest. */
public final class JournalEntry {
    private final String userId;
    private final String questId;
    private final String reflection;
    private final byte[] photo;

    public JournalEntry(String userId, String questId, String reflection) {
        this(userId, questId, reflection, null);
    }

    public JournalEntry(String userId, String questId, String reflection, byte[] photo) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be blank");
        }
        if (questId == null || questId.isBlank()) {
            throw new IllegalArgumentException("Quest ID cannot be blank");
        }
        boolean hasPhoto = photo != null && photo.length > 0;
        if ((reflection == null || reflection.isBlank()) && !hasPhoto) {
            throw new IllegalArgumentException("Enter a description or add a photo.");
        }

        this.userId = userId;
        this.questId = questId;
        this.reflection = reflection == null ? "" : reflection;
        this.photo = hasPhoto ? photo.clone() : null;
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
