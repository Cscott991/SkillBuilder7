import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StreamingPlatformTest {
    StreamingPlatform platform = new StreamingPlatform("StreamFlix");
    private Movie inception;
    private Movie knives;
    private Song blinding;
    private Song lofi;
    private Podcast serialPod;
    private Podcast hubermanPod;

    StreamingPlatformTest(){
        inception = new Movie(
                "Inception",
                "Christopher Nolan",
                8880,           // 2h 28m
                4.8,
                "PG-13",
                "Sci-Fi",
                new String[]{"en", "es", "fr", "de", "ja"}
        );

        knives = new Movie(
                "Knives Out",
                "Rian Johnson",
                7560,           // 2h 6m
                4.5,
                "PG-13",
                "Thriller",
                new String[]{"en", "es", "pt"}
        );

        blinding = new Song(
                "Blinding Lights",
                "The Weeknd",
                200,
                4.9,
                "Pop",
                "After Hours",
                false
        );

        lofi = new Song(
                "Rainy Day Study Session",
                "ChilledCow",
                3600,
                4.3,
                "Lo-Fi",
                "Lo-Fi Beats Vol. 3",
                false
        );

        serialPod = new Podcast(
                "The Alibi",
                "Sarah Koenig",
                2940,           // 49m
                4.7,
                "Serial",
                1,
                "True Crime"
        );

        hubermanPod = new Podcast(
                "Using Light for Health",
                "Andrew Huberman",
                5400,           // 90m
                4.6,
                "Huberman Lab",
                68,
                "Science"
        );
    }

    @BeforeEach
    void setUp() {
        platform = new StreamingPlatform("StreamFlix"); // Reset platform for each test
        platform.addContent(inception);
        platform.addContent(knives);
        platform.addContent(blinding);
        platform.addContent(lofi);
        platform.addContent(serialPod);
        platform.addContent(hubermanPod);
    }

    @Test
    void testAddContent() {
        assertEquals(6, platform.getLibrary().size());
        assertTrue(platform.getLibrary().contains(inception));
        assertTrue(platform.getLibrary().contains(knives));
        assertTrue(platform.getLibrary().contains(blinding));
        assertTrue(platform.getLibrary().contains(lofi));
        assertTrue(platform.getLibrary().contains(serialPod));
        assertTrue(platform.getLibrary().contains(hubermanPod));
    }

    @Test
    void findByType() {
        // Test with "Movie"
        List<MediaContent> movies = platform.findByType("Movie");
        assertNotNull(movies);
        assertEquals(2, movies.size());
        assertTrue(movies.contains(inception));
        assertTrue(movies.contains(knives));

        // Test with "Song"
        List<MediaContent> songs = platform.findByType("Song");
        assertNotNull(songs);
        assertEquals(2, songs.size());
        assertTrue(songs.contains(blinding));
        assertTrue(songs.contains(lofi));

        // Test with "Podcast"
        List<MediaContent> podcasts = platform.findByType("Podcast");
        assertNotNull(podcasts);
        assertEquals(2, podcasts.size());
        assertTrue(podcasts.contains(serialPod));
        assertTrue(podcasts.contains(hubermanPod));

        // Test with case-insensitive type
        List<MediaContent> lowerCaseMovies = platform.findByType("movie");
        assertNotNull(lowerCaseMovies);
        assertEquals(2, lowerCaseMovies.size());
        assertTrue(lowerCaseMovies.contains(inception));
        assertTrue(lowerCaseMovies.contains(knives));

        // Test with a type that does not exist
        List<MediaContent> nonExistent = platform.findByType("NonExistentType");
        assertNotNull(nonExistent);
        assertTrue(nonExistent.isEmpty());
    }

    @Test
    void getTopRated() {
        // Test with a high minRating, expecting fewer results
        List<MediaContent> topRated4_8 = platform.getTopRated(4.8);
        assertNotNull(topRated4_8);
        assertEquals(2, topRated4_8.size());
        assertTrue(topRated4_8.contains(inception));
        assertTrue(topRated4_8.contains(blinding));

        // Test with a medium minRating
        List<MediaContent> topRated4_5 = platform.getTopRated(4.5);
        assertNotNull(topRated4_5);
        assertEquals(5, topRated4_5.size());
        assertTrue(topRated4_5.contains(hubermanPod));
        assertTrue(topRated4_5.contains(inception));
        assertTrue(topRated4_5.contains(knives));
        assertTrue(topRated4_5.contains(blinding));
        assertTrue(topRated4_5.contains(serialPod));

        // Test with a low minRating, expecting all
        List<MediaContent> allContent = platform.getTopRated(0.0);
        assertNotNull(allContent);
        assertEquals(6, allContent.size());

        // Test with a minRating that excludes all content
        List<MediaContent> emptyList = platform.getTopRated(5.0);
        assertNotNull(emptyList);
        assertTrue(emptyList.isEmpty());
    }

    @Test
    void recommend() {
        // Test recommending based on Inception (Sci-Fi, duration ~8880s)
        // Expected: No other Sci-Fi with similar duration
        List<MediaContent> inceptionRecs = platform.recommend(inception);
        assertNotNull(inceptionRecs);
        assertTrue(inceptionRecs.isEmpty());

        // Test recommending based on Blinding Lights (Pop, duration ~200s)
        // Expected: No other Pop with similar duration (Lo-Fi is different genre)
        List<MediaContent> blindingRecs = platform.recommend(blinding);
        assertNotNull(blindingRecs);
        assertTrue(blindingRecs.isEmpty());

        // Create a new song with similar tag and duration to Blinding Lights
        Song blindingLights2 = new Song(
                "Blinding Lights 2",
                "The Weeknd",
                190, // within 20% of 200s (160-240)
                4.5,
                "Pop",
                "After Hours 2",
                false
        );
        platform.addContent(blindingLights2); // Add to platform for recommendation

        List<MediaContent> blindingRecsAfterAdd = platform.recommend(blinding);
        assertNotNull(blindingRecsAfterAdd);
        assertEquals(1, blindingRecsAfterAdd.size());
        assertTrue(blindingRecsAfterAdd.contains(blindingLights2));
        platform.getLibrary().remove(blindingLights2); // Clean up added content

        // Test with Podcast (Serial - True Crime, ~2940s)
        // Expecting no other True Crime podcasts with similar duration
        List<MediaContent> serialRecs = platform.recommend(serialPod);
        assertNotNull(serialRecs);
        assertTrue(serialRecs.isEmpty());

        // Test recommending from a custom item not on the platform
        Movie customMovie = new Movie(
                "Another Sci-Fi",
                "Some Director",
                9000, // Similar duration to Inception
                4.0,
                "PG",
                "Sci-Fi",
                new String[]{"en"}
        );
        // If Inception is Sci-Fi and customMovie is Sci-Fi with similar duration, it should be recommended
        // However, the `recommend` method expects the item to be from the library to compare its tag.
        // The implementation finds other items that SHARE THE SAME RECOMMENDATION TAG AND have a duration within 20% of the given item's duration.
        // It's not clear if the *given item* itself must be in the library. Assuming it can be any MediaContent for comparison.

        // Add inception back if it was removed in a previous test (it wasn't in this case, due to @BeforeEach)
        // Re-adding content for a precise test scenario if needed
        // Since @BeforeEach resets the platform, inception is always there.

        List<MediaContent> inceptionComparedToCustom = platform.recommend(customMovie);
        assertNotNull(inceptionComparedToCustom);
        assertEquals(1, inceptionComparedToCustom.size());
        assertTrue(inceptionComparedToCustom.contains(inception));

        // Test with item that has no matching tag or duration
        Song randomSong = new Song(
                "Random",
                "Artist",
                100,
                3.0,
                "Rock",
                "Album",
                false
        );
        List<MediaContent> noRecs = platform.recommend(randomSong);
        assertNotNull(noRecs);
        assertTrue(noRecs.isEmpty());
    }
}