package use_case.journal;

/** The reflection and optional photo to display when opening the editor. */
public final class LoadJournalEntryOutputData {
    private final String reflection;
    private final byte[] photo;

    public LoadJournalEntryOutputData(String reflection, byte[] photo) {
        this.reflection = reflection;
        this.photo = photo == null ? null : photo.clone();
    }

    public String getReflection() {
        return reflection;
    }

    public byte[] getPhoto() {
        return photo == null ? null : photo.clone();
    }
}
