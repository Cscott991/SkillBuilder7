import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The main platform that manages a library of media content.
 */
public class StreamingPlatform {

    private String name;
    private ArrayList<MediaContent> library;

    public StreamingPlatform(String name) {
        this.name    = name;
        this.library = new ArrayList<>();
    }

    /** Adds a piece of content to the platform library. */
    public void addContent(MediaContent m) {
        library.add(m);
    }


    /**
     * Returns all content whose getContentType() matches the given type string
     * (case-insensitive).
     */
    public List<MediaContent> findByType(String type) {
        List<MediaContent> results = new ArrayList<>();
        for (MediaContent m : library) {
            if (m.getContentType().equalsIgnoreCase(type)) {
                results.add(m);
            }
        }
        return results;
    }

    /**
     * Returns all content with a rating >= minRating.
     */
    public List<MediaContent> getTopRated(double minRating) {
        List<MediaContent> results = new ArrayList<>();
        for (MediaContent m : library) {
            if (m.getRating() >= minRating) {
                results.add(m);
            }
        }
        return results;
    }

    /**
     * Attempts to download everything in the library that implements Downloadable.
     * Uses instanceof to check at runtime — demonstrates polymorphism's limits.
     *
     * @param quality 1 (low), 2 (medium), or 3 (high)
     */
    public void downloadAll(int quality) {
        System.out.printf("%n── Downloading all content at quality %d ──%n", quality);
        int downloaded = 0;
        int skipped    = 0;
        for (MediaContent m : library) {
            if (m instanceof Downloadable d) {   // pattern matching (Java 16+)
                d.download(quality);
                downloaded++;
            } else {
                System.out.printf("   ⊘ \"%s\" is not downloadable — skipping.%n", m.getTitle());
                skipped++;
            }
        }
        System.out.printf("── Done: %d downloaded, %d skipped ──%n%n", downloaded, skipped);
    }

    /**
     * BONUS — Recommendation engine.
     * Finds other items that share the same recommendation tag AND have a
     * duration within 20% of the given item's duration.
     */
    public List<MediaContent> recommend(MediaContent item) {
        List<MediaContent> recommendations = new ArrayList<>();
        double low  = item.getDuration() * 0.8;
        double high = item.getDuration() * 1.2;

        for (MediaContent m : library) {
            if (m != item &&
                    item.getRecommendationTag().equals(m.getRecommendationTag()) &&
                    m.getDuration() >= low &&
                    m.getDuration() <= high) {

                recommendations.add(m);
            }
        }
        
        return recommendations;
    }

    public String getName() { return name; }

    public String toString() {
        String result = "\n══ %s Library (%d items) ══\n\n".formatted(name, library.size());
        for (MediaContent m : library) {
            result += m + "\n\n"; // one \n to go to next line and 2nd for blank line
        }
        return result;
    }

    public List<MediaContent> getLibrary() {
        return library;
    }
}
