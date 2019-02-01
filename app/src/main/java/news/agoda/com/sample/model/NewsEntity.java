package news.agoda.com.sample.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This represents a news item
 */
public class NewsEntity {
    private static final String TAG = NewsEntity.class.getSimpleName();
    public static final String TITLE = "title";
    public static final String ABSTRACT = "abstract";
    public static final String URL = "url";
    public static final String MULTIMEDIA = "multimedia";
    private String title;
    private String summary;
    private String articleUrl;
    private List<MediaEntity> mediaEntityList;

    public NewsEntity(JSONObject jsonObject) {
        if(jsonObject!=null) {
            try {
                mediaEntityList = new ArrayList<>();
                title = jsonObject.optString(TITLE);
                summary = jsonObject.optString(ABSTRACT);
                articleUrl = jsonObject.optString(URL);
                Object multimedia = jsonObject.opt(MULTIMEDIA);
                if (multimedia != null) {
                    if (multimedia instanceof JSONArray) {
                        JSONArray mediaArray = (JSONArray) multimedia;
                        for (int i = 0; i < mediaArray.length(); i++) {
                            JSONObject mediaObject = mediaArray.getJSONObject(i);
                            MediaEntity mediaEntity = new MediaEntity(mediaObject);
                            mediaEntityList.add(mediaEntity);
                        }
                    } else if (multimedia instanceof String && !(((String) multimedia).isEmpty()))
                        mediaEntityList.add(new MediaEntity((String) multimedia));
                }

            } catch (JSONException exception) {
               // Log.e(TAG, exception.getMessage());
            }
        }
    }

    public String getTitle() {
        return title;
    }

    public String getSummary() {
        return summary;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public List<MediaEntity> getMediaEntity() {
        return mediaEntityList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NewsEntity that = (NewsEntity) o;
        return Objects.equals(title, that.title) &&
                Objects.equals(summary, that.summary) &&
                Objects.equals(articleUrl, that.articleUrl) &&
                Objects.equals(mediaEntityList, that.mediaEntityList);
    }

    @Override
    public int hashCode() {

        return Objects.hash(title, summary, articleUrl, mediaEntityList);
    }
}
