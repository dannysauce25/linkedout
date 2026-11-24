package use_case.journal;

public interface LoadJournalEntryInputBoundary {
    void load(LoadJournalEntryInputData inputData, LoadJournalEntryOutputBoundary outputBoundary);
}
