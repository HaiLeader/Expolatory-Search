package vn.brine.haileader.expolatorysearch.models;

/**
 * Created by haileader on 26/10/16.
 */
public class SearchResult {
    private String label;
    private String uri;

    public SearchResult(String label, String uri){
        this.label = label;
        this.uri = uri;
    }

    public String getLabel(){
        return label;
    }

    public String getUri(){
        return uri;
    }
}
