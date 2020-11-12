package nadeem.animei.Library;

public class Item {

    private String cover;
    private String title;
    private String link;

    public Item(String cover, String title, String link) {
        this.cover = cover;
        this.title = title;
        this.link = link;
    }

    public String getCover() {
        return cover;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
