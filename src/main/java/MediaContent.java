/**
 * Abstract base class representing any piece of media content
 * on the streaming platform.
 */
public abstract class MediaContent {

    protected String title;
    protected String creator;
    protected int durationSeconds;
    protected double rating;

    public MediaContent(String title, String creator, int durationSeconds, double rating) {
        this.title = title;
        this.creator = creator;
        this.durationSeconds = durationSeconds;
        this.rating = Math.min(5.0, Math.max(0.0, rating));
    }


    public String getTitle()    { return title; }
    public String getCreator()  { return creator; }
    public int    getDuration() { return durationSeconds; }
    public double getRating()   { return rating; }

    /**
     * Converts raw seconds into a human-readable HH:MM:SS string.
     */
    public String formatDuration() {
        int hours   = durationSeconds / 3600;
        int minutes = (durationSeconds % 3600) / 60;
        int seconds = durationSeconds % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }


    /** Simulates playing the content, printing a description of the experience. */
    public abstract void play();

    /** Returns a short type label, e.g. "Movie", "Song", "Podcast". */
    public abstract String getContentType();

    /**
     * Returns a short recommendation tag used to group similar content,
     * e.g. "Binge-worthy", "Focus music", "True crime".
     */
    public abstract String getRecommendationTag();

    /**
     * Returns a formatted string summary of this content to standard output.
     */
    public String toString() {
        String result = "╔══════════════════════════════════════════\n";
        result += "║ [%s] %s\n".formatted(getContentType(), title);
        result += "║ Creator  : %s\n".formatted(creator);
        result += "║ Duration : %s\n".formatted(formatDuration());
        result += "║ Rating   : %.1f / 5.0\n".formatted(rating);
        result += "║ Tag      : %s\n".formatted(getRecommendationTag());
        result += "╚══════════════════════════════════════════\n";
        return result;
    }
}
