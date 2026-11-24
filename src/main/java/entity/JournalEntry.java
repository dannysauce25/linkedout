package entity;

/** A saved reflection associated with a user and a side quest. */
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
        if (reflection == null || reflection.isBlank()) {
            throw new IllegalArgumentException("Please enter a reflection");
        }

        this.userId = userId;
        this.questId = questId;
        this.reflection = reflection;
        if (photo != null && photo.length == 0) {
            throw new IllegalArgumentException("Photo cannot be empty");
        }
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
