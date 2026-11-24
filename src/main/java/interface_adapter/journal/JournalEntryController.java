package interface_adapter.journal;

import use_case.journal.LoadJournalEntryInputBoundary;
import use_case.journal.LoadJournalEntryInputData;
import use_case.journal.SaveJournalEntryInputBoundary;
import use_case.journal.SaveJournalEntryInputData;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.function.Consumer;
import javax.imageio.ImageIO;

public final class JournalEntryController {
    private final String userId;
    private final LoadJournalEntryInputBoundary loadEntry;
    private final SaveJournalEntryInputBoundary saveEntry;
    private final JournalEntryPresenter presenter = new JournalEntryPresenter();

    public JournalEntryController(String userId, LoadJournalEntryInputBoundary loadEntry,
                                  SaveJournalEntryInputBoundary saveEntry) {
        if (userId == null || userId.isBlank()) {
            throw new IllegalArgumentException("User ID cannot be blank");
        }
        this.userId = userId;
        this.loadEntry = loadEntry;
        this.saveEntry = saveEntry;
    }

    public void load(String questId, Consumer<JournalEntryForm> showEntry, Consumer<String> showError) {
        loadEntry.load(new LoadJournalEntryInputData(userId, questId), presenter.forLoad(showEntry, showError));
    }

    public void save(String questId, JournalEntryForm form, Runnable showSaved, Consumer<String> showError) {
        byte[] photo;
        try {
            photo = encodePhoto(form.getPhoto());
        } catch (IOException exception) {
            showError.accept("Could not save your journal photo. Please try again.");
            return;
        }
        saveEntry.save(new SaveJournalEntryInputData(userId, questId, form.getReflection(), photo),
                presenter.forSave(showSaved, showError));
    }

    private static byte[] encodePhoto(BufferedImage photo) throws IOException {
        if (photo == null) {
            return null;
        }

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        if (!ImageIO.write(photo, "png", output)) {
            throw new IOException("Could not encode journal photo");
        }
        return output.toByteArray();
    }
}
