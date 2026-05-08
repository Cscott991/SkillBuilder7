/**
 * Represents a single podcast episode.
 *
 * Unique fields: showName, episodeNumber, category
 * Implements: Downloadable
 */
public class Podcast extends MediaContent implements Downloadable {

    private String showName;
    private int    episodeNumber;
    private String category;      // e.g. "True Crime", "Technology", "Comedy"

    // Downloadable state
    private boolean downloaded       = false;
    private int     downloadedQuality = 0;

    public Podcast(String episodeTitle,
                   String host,
                   int durationSeconds,
                   double rating,
                   String showName,
                   int episodeNumber,
                   String category) {
        super(episodeTitle, host, durationSeconds, rating);
        this.showName      = showName;
        this.episodeNumber = episodeNumber;
        this.category      = category;
    }

    // ── MediaContent abstract methods ─────────────────────────────────────────

    @Override
    public void play() {
        System.out.printf("🎙  Now playing: \"%s\"%n", title);
        System.out.printf("   %s — Episode %d  |  Host: %s%n",
                showName, episodeNumber, creator);
        System.out.printf("   Category: %s%n", category);
    }

    @Override
    public String getContentType() { return "Podcast"; }

    @Override
    public String getRecommendationTag() {
        return switch (category.toLowerCase()) {
            case "true crime"            -> "True crime";
            case "technology", "science" -> "Mind-expanding";
            case "comedy"                -> "Feel-good";
            case "history"               -> "Deep dive";
            case "business", "finance"   -> "Level-up";
            default                      -> "Worth a listen";
        };
    }

    // ── Downloadable ──────────────────────────────────────────────────────────

    @Override
    public boolean download(int qualityLevel) {
        if (qualityLevel < 1 || qualityLevel > 3) {
            System.out.println("   ✗ Invalid quality level. Choose 1 (low), 2 (medium), or 3 (high).");
            return false;
        }
        System.out.printf("   ↓ Downloading \"%s\" (Ep. %d) at quality %d (%d MB)…%n",
                title, episodeNumber, qualityLevel, getFileSizeMB(qualityLevel));
        downloaded        = true;
        downloadedQuality = qualityLevel;
        System.out.println("   ✓ Download complete.");
        return true;
    }

    @Override
    public long getFileSizeMB(int qualityLevel) {
        // Podcast audio is typically compressed speech, so files are small
        long minutesLong = durationSeconds / 60;
        return switch (qualityLevel) {
            case 1 -> Math.max(1, minutesLong / 4);  // ~1 MB per 4 min
            case 2 -> Math.max(2, minutesLong / 2);  // ~1 MB per 2 min
            case 3 -> Math.max(4, minutesLong);       // ~1 MB per min
            default -> 0;
        };
    }

    @Override
    public boolean isDownloaded() { return downloaded; }

    // ── Extra getters ─────────────────────────────────────────────────────────

    public String getShowName()      { return showName; }
    public int    getEpisodeNumber() { return episodeNumber; }
    public String getCategory()      { return category; }

    @Override
    public String toString() {
        String result = super.toString();
        result += "   Show: %s  |  Episode: %d  |  Category: %s  |  Downloaded: %s%n"
                .formatted(
                        showName, episodeNumber, category,
                        downloaded ? "Yes (Q" + downloadedQuality + ")" : "No"
                );
        return result;
    }
}
