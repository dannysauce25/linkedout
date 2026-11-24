package use_case.journal;

public interface SaveJournalEntryOutputBoundary {
    void prepareSuccessView();

    void prepareFailView(String message);
}
