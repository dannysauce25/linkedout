package interface_adapter.journal;

import use_case.journal.LoadJournalEntryOutputBoundary;
import use_case.journal.LoadJournalEntryOutputData;
import use_case.journal.SaveJournalEntryOutputBoundary;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.function.Consumer;
import javax.imageio.ImageIO;

final class JournalEntryPresenter {
    LoadJournalEntryOutputBoundary forLoad(Consumer<JournalEntryForm> showEntry, Consumer<String> showError) {
        return new LoadJournalEntryOutputBoundary() {
            @Override
            public void prepareSuccessView(LoadJournalEntryOutputData outputData) {
                byte[] photo = outputData.getPhoto();
                BufferedImage image = null;
                if (photo != null) {
                    try {
                        image = ImageIO.read(new ByteArrayInputStream(photo));
                        if (image == null) {
                            throw new IOException("Unsupported image format");
                        }
                    } catch (IOException exception) {
                        prepareFailView("Could not open your journal photo.");
                        return;
                    }
                }
                showEntry.accept(new JournalEntryForm(outputData.getReflection(), image));
            }

            @Override
            public void prepareFailView(String message) {
                showError.accept(message);
            }
        };
    }

    SaveJournalEntryOutputBoundary forSave(Runnable showSaved, Consumer<String> showError) {
        return new SaveJournalEntryOutputBoundary() {
            @Override
            public void prepareSuccessView() {
                showSaved.run();
            }

            @Override
            public void prepareFailView(String message) {
                showError.accept(message);
            }
        };
    }
}
