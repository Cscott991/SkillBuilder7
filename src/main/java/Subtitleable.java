/**
 * Interface for video content that supports subtitle tracks.
 */
public interface Subtitleable {

    /**
     * Enables subtitles for the given BCP-47 language code (e.g. "en", "es", "fr").
     *
     * @param languageCode the language to activate
     */
    void enableSubtitles(String languageCode);

    /** Turns off subtitle display. */
    void disableSubtitles();

    /**
     * Returns the language code of the currently active subtitle track,
     * or null if subtitles are disabled.
     */
    String getCurrentSubtitleLanguage();

    /**
     * Returns all subtitle language codes available for this content.
     *
     * @return array of BCP-47 language codes
     */
    String[] getAvailableLanguages();
}
