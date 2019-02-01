package news.agoda.com.sample.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

/**
 * This class represents a media item
 */
public class MediaEntity {
    public static final String URL = "url";
    private String url;


    public MediaEntity(JSONObject jsonObject) throws JSONException {
        url = jsonObject.optString(URL);

    }
    public MediaEntity(String url) {
        this.url = url;
    }
    public String getUrl() {
        return url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MediaEntity that = (MediaEntity) o;
        return Objects.equals(url, that.url);
    }

    @Override
    public int hashCode() {

        return Objects.hash(url);
    }
}
