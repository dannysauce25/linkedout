package use_case.journal;

import entity.JournalEntry;

import java.io.IOException;

/** Loads and saves journal entries for the signed-in user. */
public final class JournalEntryInteractor implements LoadJournalEntryInputBoundary, SaveJournalEntryInputBoundary {
    private final JournalEntryRepository repository;

    public JournalEntryInteractor(JournalEntryRepository repository) {
        this.repository = repository;
    }

    @Override
    public void load(LoadJournalEntryInputData inputData, LoadJournalEntryOutputBoundary outputBoundary) {
        LoadJournalEntryOutputData outputData;
        try {
            outputData = repository.find(inputData.getUserId(), inputData.getQuestId())
                    .map(entry -> new LoadJournalEntryOutputData(entry.getReflection(), entry.getPhoto()))
                    .orElseGet(() -> new LoadJournalEntryOutputData("", null));
        } catch (IOException exception) {
            outputBoundary.prepareFailView("Could not open your journal entry. Please try again.");
            return;
        }
        outputBoundary.prepareSuccessView(outputData);
    }

    @Override
    public void save(SaveJournalEntryInputData inputData, SaveJournalEntryOutputBoundary outputBoundary) {
        try {
            repository.save(new JournalEntry(inputData.getUserId(), inputData.getQuestId(),
                    inputData.getReflection(), inputData.getPhoto()));
        } catch (IllegalArgumentException exception) {
            outputBoundary.prepareFailView(exception.getMessage());
            return;
        } catch (IOException exception) {
            outputBoundary.prepareFailView("Could not save your journal entry. Please try again.");
            return;
        }
        outputBoundary.prepareSuccessView();
    }
}
