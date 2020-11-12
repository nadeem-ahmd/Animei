package nadeem.animei.Model;

public class Anime {

    private String link;
    private String name;
    private String cover;
    private String description;

    public Anime(String link, String name, String cover, String description) {
        this.link = link;
        this.name = name;
        this.cover = cover;
        this.description = description;
    }

    public Anime(String link, String name, String cover) {
        this.link = link;
        this.name = name;
        this.cover = cover;
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover() {
        return cover;
    }

    public String getDescription() {
        return description;
    }

}
