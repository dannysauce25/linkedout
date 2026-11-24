package use_case.journal;

public interface SaveJournalEntryInputBoundary {
    void save(SaveJournalEntryInputData inputData, SaveJournalEntryOutputBoundary outputBoundary);
}
