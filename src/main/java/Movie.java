import java.util.Arrays;

/**
 * Represents a feature film.
 *
 * Unique fields: pgRating, genre
 * Implements: Downloadable, Subtitleable
 */
public class Movie extends MediaContent implements Downloadable, Subtitleable {

    private String pgRating;   // e.g. "G", "PG", "PG-13", "R"
    private String genre;      // e.g. "Thriller", "Comedy", "Sci-Fi"

    // Downloadable state
    private boolean downloaded = false;
    private int     downloadedQuality = 0;

    // Subtitleable state
    private String   currentSubtitleLanguage = null;
    private String[] availableLanguages;

    public Movie(String title,
                 String director,
                 int durationSeconds,
                 double rating,
                 String pgRating,
                 String genre,
                 String[] availableLanguages) {
        super(title, director, durationSeconds, rating);
        this.pgRating           = pgRating;
        this.genre              = genre;
        this.availableLanguages = availableLanguages;
    }

    // ── MediaContent abstract methods ─────────────────────────────────────────

    @Override
    public void play() {
        System.out.printf("▶  Now playing movie: \"%s\" [%s] (%s)%n",
                title, pgRating, genre);
        if (currentSubtitleLanguage != null) {
            System.out.printf("   Subtitles: %s%n", currentSubtitleLanguage);
        }
    }

    @Override
    public String getContentType() { return "Movie"; }

    @Override
    public String getRecommendationTag() {
        return switch (genre.toLowerCase()) {
            case "thriller", "horror"  -> "Edge-of-your-seat";
            case "comedy"              -> "Feel-good";
            case "sci-fi", "sci fi"   -> "Mind-bending";
            case "documentary"         -> "Eye-opening";
            default                    -> "Cinematic";
        };
    }

    // ── Downloadable ──────────────────────────────────────────────────────────

    @Override
    public boolean download(int qualityLevel) {
        if (qualityLevel < 1 || qualityLevel > 3) {
            System.out.println("   ✗ Invalid quality level. Choose 1 (low), 2 (medium), or 3 (high).");
            return false;
        }
        System.out.printf("   ↓ Downloading \"%s\" at quality %d (%d MB)…%n",
                title, qualityLevel, getFileSizeMB(qualityLevel));
        downloaded        = true;
        downloadedQuality = qualityLevel;
        System.out.println("   ✓ Download complete.");
        return true;
    }

    @Override
    public long getFileSizeMB(int qualityLevel) {
        // Base size derived from duration; quality multiplies it
        long base = durationSeconds / 60; // ~1 MB per minute at low quality
        return switch (qualityLevel) {
            case 1 -> base;
            case 2 -> base * 4;
            case 3 -> base * 10;
            default -> 0;
        };
    }

    @Override
    public boolean isDownloaded() { return downloaded; }

    // ── Subtitleable ──────────────────────────────────────────────────────────

    @Override
    public void enableSubtitles(String languageCode) {
        boolean found = Arrays.asList(availableLanguages).contains(languageCode);
        if (found) {
            currentSubtitleLanguage = languageCode;
            System.out.printf("   Subtitles enabled: %s%n", languageCode);
        } else {
            System.out.printf("   ✗ Subtitles not available for language: %s%n", languageCode);
        }
    }

    @Override
    public void disableSubtitles() {
        currentSubtitleLanguage = null;
        System.out.println("   Subtitles disabled.");
    }

    @Override
    public String getCurrentSubtitleLanguage() { return currentSubtitleLanguage; }

    @Override
    public String[] getAvailableLanguages() { return availableLanguages; }

    // ── Extra getters ─────────────────────────────────────────────────────────

    public String getPgRating() { return pgRating; }
    public String getGenre()    { return genre; }

    @Override
    public String toString() {
        String result = super.toString();
        result += "   Genre: %s  |  Rating: %s  |  Downloaded: %s%n".formatted(
                genre,
                pgRating,
                downloaded ? "Yes (Q" + downloadedQuality + ")" : "No"
        );
        result += "   Available subtitles: %s%n".formatted(
                Arrays.toString(availableLanguages)
        );
        return result;
    }
}
