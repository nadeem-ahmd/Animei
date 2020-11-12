package nadeem.animei.Model;

public class Episode {

    private String link;
    private int episode;

    public Episode(String link, int episode) {
        this.link = link;
        this.episode = episode;
    }

    public String getLink() {
        return link;
    }

    public int getEpisode() {
        return episode;
    }

    public String getEpisodeString() {
        return "Episode " + getEpisode();
    }

}
