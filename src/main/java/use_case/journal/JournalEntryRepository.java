package use_case.journal;

import entity.JournalEntry;

import java.io.IOException;
import java.util.Optional;

/** Stores one journal entry per user and quest. */
public interface JournalEntryRepository {
    Optional<JournalEntry> find(String userId, String questId) throws IOException;

    void save(JournalEntry entry) throws IOException;
}
