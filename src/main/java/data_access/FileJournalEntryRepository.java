package data_access;

import entity.JournalEntry;
import use_case.journal.JournalEntryRepository;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Base64;
import java.util.Optional;
import java.util.Properties;

/** Stores the reflection and photo together so a failed save leaves the previous entry intact. */
public final class FileJournalEntryRepository implements JournalEntryRepository {
    private final Path directory;

    public FileJournalEntryRepository(Path directory) {
        this.directory = directory;
    }

    @Override
    public Optional<JournalEntry> find(String userId, String questId) throws IOException {
        Properties properties = new Properties();
        try (InputStream input = Files.newInputStream(entryPath(userId, questId))) {
            properties.load(input);
            String photo = properties.getProperty("photo");
            return Optional.of(new JournalEntry(userId, questId, properties.getProperty("reflection"),
                    photo == null ? null : Base64.getDecoder().decode(photo)));
        } catch (NoSuchFileException exception) {
            return Optional.empty();
        } catch (IllegalArgumentException exception) {
            throw new IOException("Invalid journal entry", exception);
        }
    }

    @Override
    public void save(JournalEntry entry) throws IOException {
        Path destination = entryPath(entry.getUserId(), entry.getQuestId());
        Files.createDirectories(destination.getParent());
        Properties properties = new Properties();
        properties.setProperty("reflection", entry.getReflection());
        byte[] photo = entry.getPhoto();
        if (photo != null) {
            properties.setProperty("photo", Base64.getEncoder().encodeToString(photo));
        }

        Path temporaryFile = Files.createTempFile(destination.getParent(), "entry-", ".tmp");
        try {
            try (OutputStream output = Files.newOutputStream(temporaryFile)) {
                properties.store(output, null);
            }
            Files.move(temporaryFile, destination, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } finally {
            Files.deleteIfExists(temporaryFile);
        }
    }

    private Path entryPath(String userId, String questId) {
        return directory.resolve(encodeKey(userId)).resolve(encodeKey(questId) + ".properties");
    }

    private static String encodeKey(String value) {
        StringBuilder key = new StringBuilder();
        for (byte character : value.getBytes(StandardCharsets.UTF_8)) {
            key.append(Character.forDigit((character & 0xff) >>> 4, 16));
            key.append(Character.forDigit(character & 0xf, 16));
        }
        return key.toString();
    }
}
