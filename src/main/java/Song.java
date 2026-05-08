/**
 * Represents a music track.
 *
 * Unique fields: genre, album, explicit
 * Implements: Downloadable
 */
public class Song extends MediaContent implements Downloadable {

    private String  genre;
    private String  album;
    private boolean explicit;

    private boolean downloaded       = false;
    private int     downloadedQuality = 0;

    public Song(String title,
                String artist,
                int durationSeconds,
                double rating,
                String genre,
                String album,
                boolean explicit) {
        super(title, artist, durationSeconds, rating);
        this.genre    = genre;
        this.album    = album;
        this.explicit = explicit;
    }


    @Override
    public void play() {
        System.out.printf("♫  Now playing: \"%s\" by %s%s%n",
                title, creator, explicit ? " [E]" : "");
        System.out.printf("   Album: %s  |  Genre: %s%n", album, genre);
    }

    @Override
    public String getContentType() { return "Song"; }

    @Override
    public String getRecommendationTag() {
        return switch (genre.toLowerCase()) {
            case "lo-fi", "ambient", "classical" -> "Focus music";
            case "pop", "dance", "edm"           -> "Party vibes";
            case "hip-hop", "rap"                -> "Hype track";
            case "jazz", "blues"                 -> "Laid-back";
            case "rock", "metal"                 -> "High energy";
            default                              -> "Great listen";
        };
    }


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
        // Audio files are much smaller than video
        return switch (qualityLevel) {
            case 1 -> 3;   // ~128 kbps MP3
            case 2 -> 8;   // ~320 kbps MP3
            case 3 -> 25;  // lossless FLAC
            default -> 0;
        };
    }

    @Override
    public boolean isDownloaded() { return downloaded; }

    public String  getGenre()    { return genre; }
    public String  getAlbum()    { return album; }
    public boolean isExplicit()  { return explicit; }

    @Override
    public String toString() {
        String result = super.toString();
        result += "   Album: %s  |  Genre: %s  |  Explicit: %s  |  Downloaded: %s%n"
                .formatted(
                    album, genre, explicit ? "Yes" : "No",
                    downloaded ? "Yes (Q" + downloadedQuality + ")" : "No"
                );
        return result;
    }
}
