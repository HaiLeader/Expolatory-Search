package vn.brine.haileader.expolatorysearch.models;

/**
 * Created by HaiLeader on 7/15/2016.
 */
public class Movie {

    private String title;
    private String thumbnail;
    private String uri;

    public Movie(String title, String thumbnail, String uri){
        this.title = title;
        setThumbnail(thumbnail);
        this.uri = uri;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public String getTitle(){
        return title;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String imageUrl){
        if(imageUrl == null) return;
        String httpsLink = imageUrl.replace("http://", "https://");
        this.thumbnail = httpsLink;
    }

    public void setUri(String uri){
        this.uri = uri;
    }

    public String getUri(){
        return uri;
    }
}
