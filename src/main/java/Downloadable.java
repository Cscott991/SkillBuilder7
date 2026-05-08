/**
 * Interface for media content that can be saved locally for offline playback.
 *
 * Quality levels:
 *   1 = Low  (smallest file, lowest fidelity)
 *   2 = Medium
 *   3 = High (largest file, best fidelity)
 */
public interface Downloadable {

    /**
     * Attempts to download the content at the specified quality level.
     *
     * @param qualityLevel 1 (low), 2 (medium), or 3 (high)
     * @return true if the download succeeded, false otherwise
     */
    boolean download(int qualityLevel);

    /**
     * Returns the estimated file size in megabytes for the given quality level.
     *
     * @param qualityLevel 1, 2, or 3
     * @return size in MB
     */
    long getFileSizeMB(int qualityLevel);

    /**
     * Returns whether this item has already been downloaded.
     */
    boolean isDownloaded();
}
