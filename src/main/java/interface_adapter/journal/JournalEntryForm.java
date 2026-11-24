package interface_adapter.journal;

import java.awt.image.BufferedImage;

/** Contents displayed by the desktop journal editor. */
public final class JournalEntryForm {
    private final String reflection;
    private final BufferedImage photo;

    public JournalEntryForm(String reflection, BufferedImage photo) {
        this.reflection = reflection;
        this.photo = photo;
    }

    public String getReflection() {
        return reflection;
    }

    public BufferedImage getPhoto() {
        return photo;
    }
}
