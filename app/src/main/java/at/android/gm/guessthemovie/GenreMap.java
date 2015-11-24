package at.android.gm.guessthemovie;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by georg on 24-Nov-15.
 */
public class GenreMap {
    private static GenreMap ourInstance = new GenreMap();

    private Map<String, Integer> CONSTANT_MAP;

    public static GenreMap getInstance() {
        return ourInstance;
    }

    private GenreMap() {
        Map<String, Integer> tmp = new LinkedHashMap<String, Integer>();
        tmp.put("Action", 28);
        tmp.put("Adventure", 12);
        tmp.put("Animation", 16);
        tmp.put("Comedy", 35);
        tmp.put("Crime", 80);
        tmp.put("Documentary", 99);
        tmp.put("Drama", 18);
        tmp.put("Family", 10751);
        tmp.put("Fantasy", 14);
        tmp.put("Foreign", 10769);
        tmp.put("History", 36);
        tmp.put("Horror", 27);
        tmp.put("Music", 10402);
        tmp.put("Mystery", 9648);
        tmp.put("Romance", 10749);
        tmp.put("Science Fiction", 878);
        tmp.put("TV Movie", 10770);
        tmp.put("Thriller", 53);
        tmp.put("War", 10752);
        tmp.put("Western", 37);
        CONSTANT_MAP = Collections.unmodifiableMap(tmp);
    }

    public Map<String, Integer> getCONSTANT_MAP() {
        return CONSTANT_MAP;
    }
}
