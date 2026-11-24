package use_case.journal;

public interface LoadJournalEntryOutputBoundary {
    void prepareSuccessView(LoadJournalEntryOutputData outputData);

    void prepareFailView(String message);
}
