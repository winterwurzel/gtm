package at.android.gm.guessthemovie;

/**
 * Created by georg on 15-Nov-15.
 */
public class Movie {
    private boolean adult;
    private String backdrop_path;
    private String genre_ids;
    private int id;
    private String original_language;
    private String original_title;
    private String overview;
    private String release_Date;
    private String poster_path;
    private double popularity;
    private String title;
    private double vote_average;

    public Movie(boolean adult, String backdrop_path, String genre_ids, int id, String original_language, String original_title, String overview, String release_Date, String poster_path, double popularity, String title, double vote_average) {
        this.adult = adult;
        this.backdrop_path = backdrop_path;
        this.genre_ids = genre_ids;
        this.id = id;
        this.original_language = original_language;
        this.original_title = original_title;
        this.overview = overview;
        this.release_Date = release_Date;
        this.poster_path = poster_path;
        this.popularity = popularity;
        this.title = title;
        this.vote_average = vote_average;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public String getGenre_ids() {
        return genre_ids;
    }

    public int getId() {
        return id;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public String getRelease_Date() {
        return release_Date;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getTitle() {
        return title;
    }

    public double getVote_average() {
        return vote_average;
    }
}
