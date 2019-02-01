package news.agoda.com.sample.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NewsEntityTest {

    public static final String MULTIMEDIA = "multimedia";
    public static final String URL = "url";
    public static final String ABSTRACT = "abstract";
    public static final String TITLE = "title";
    private JSONArray jsonArray;

    enum INDEX {
        DEFAULT,
        TITLE,
        ABSTRACT,
        URL,
        MEDIA,
        ALL
    }

    @Before
    public void setUp() throws JSONException {
        jsonArray = JSONUtils.fromStream(NewsEntity.class.getResourceAsStream("/sampleData/nullTest.json"));
    }

    @Test
    public void nullJson() {
        new NewsEntity(null);
    }

    @Test
    public void nullTestNewsEntity() throws JSONException {
        //TITLE
        NewsEntity newsEntity = new NewsEntity(jsonArray.getJSONObject(INDEX.TITLE.ordinal()));
        assertNotNull(newsEntity.getArticleUrl());
        assertNotNull(newsEntity.getSummary());
        assertNotNull(newsEntity.getMediaEntity());
        assertNotNull(newsEntity.getTitle());

        newsEntity = new NewsEntity(jsonArray.getJSONObject(INDEX.ABSTRACT.ordinal()));
        assertNotNull(newsEntity.getArticleUrl());
        assertNotNull(newsEntity.getSummary());
        assertNotNull(newsEntity.getMediaEntity());
        assertNotNull(newsEntity.getTitle());

        newsEntity = new NewsEntity(jsonArray.getJSONObject(INDEX.URL.ordinal()));
        assertNotNull(newsEntity.getArticleUrl());
        assertNotNull(newsEntity.getSummary());
        assertNotNull(newsEntity.getMediaEntity());
        assertNotNull(newsEntity.getTitle());

        newsEntity = new NewsEntity(jsonArray.getJSONObject(INDEX.MEDIA.ordinal()));
        assertNotNull(newsEntity.getArticleUrl());
        assertNotNull(newsEntity.getSummary());
        assertNotNull(newsEntity.getMediaEntity());
        assertNotNull(newsEntity.getTitle());

        newsEntity = new NewsEntity(jsonArray.getJSONObject(INDEX.ALL.ordinal()));
        assertNotNull(newsEntity.getArticleUrl());
        assertNotNull(newsEntity.getSummary());
        assertNotNull(newsEntity.getMediaEntity());
        assertNotNull(newsEntity.getTitle());

        newsEntity = new NewsEntity(jsonArray.getJSONObject(INDEX.DEFAULT.ordinal()));
        assertNotNull(newsEntity.getArticleUrl());
        assertNotNull(newsEntity.getSummary());
        assertNotNull(newsEntity.getMediaEntity());
        assertNotNull(newsEntity.getTitle());
    }

    @Test
    public void titleAbstractArticleURLCheck() throws JSONException {
        //TITLE
        JSONObject jsonObject = jsonArray.getJSONObject(INDEX.TITLE.ordinal());
        NewsEntity newsEntity = new NewsEntity(jsonObject);
        assertEquals(newsEntity.getTitle(), "");
        assertEquals(newsEntity.getSummary(), jsonObject.getString(ABSTRACT));
        assertEquals(newsEntity.getArticleUrl(), jsonObject.getString(URL));

        jsonObject = jsonArray.getJSONObject(INDEX.ABSTRACT.ordinal());
        newsEntity = new NewsEntity(jsonObject);
        assertEquals(newsEntity.getTitle(), jsonObject.getString(TITLE));
        assertEquals(newsEntity.getSummary(), "");
        assertEquals(newsEntity.getArticleUrl(), jsonObject.getString(URL));

        jsonObject = jsonArray.getJSONObject(INDEX.URL.ordinal());
        newsEntity = new NewsEntity(jsonObject);
        assertEquals(newsEntity.getTitle(), jsonObject.getString(TITLE));
        assertEquals(newsEntity.getSummary(), jsonObject.getString(ABSTRACT));
        assertEquals(newsEntity.getArticleUrl(), "");

        jsonObject = jsonArray.getJSONObject(INDEX.MEDIA.ordinal());
        newsEntity = new NewsEntity(jsonObject);
        assertEquals(newsEntity.getTitle(), jsonObject.getString(TITLE));
        assertEquals(newsEntity.getSummary(), jsonObject.getString(ABSTRACT));
        assertEquals(newsEntity.getArticleUrl(), jsonObject.getString(URL));

        jsonObject = jsonArray.getJSONObject(INDEX.ALL.ordinal());
        newsEntity = new NewsEntity(jsonObject);
        assertEquals(newsEntity.getTitle(), "");
        assertEquals(newsEntity.getSummary(), "");
        assertEquals(newsEntity.getArticleUrl(), "");

        jsonObject = jsonArray.getJSONObject(INDEX.DEFAULT.ordinal());
        JSONObject multimedia = jsonObject.getJSONArray(MULTIMEDIA).getJSONObject(0);
        newsEntity = new NewsEntity(jsonObject);
        MediaEntity mediaEntity = newsEntity.getMediaEntity().get(0);
        assertEquals(newsEntity.getTitle(), jsonObject.getString(TITLE));
        assertEquals(newsEntity.getSummary(), jsonObject.getString(ABSTRACT));
        assertEquals(newsEntity.getArticleUrl(), jsonObject.getString(URL));
        assertEquals(mediaEntity.getUrl(), multimedia.getString(URL));

    }

    @Test
    public void testMediaEntity() throws JSONException {
        //TITLE
        JSONObject jsonObject = jsonArray.getJSONObject(INDEX.TITLE.ordinal());
        JSONObject multimedia = jsonObject.getJSONArray(MULTIMEDIA).getJSONObject(0);

        NewsEntity newsEntity = new NewsEntity(jsonObject);
        MediaEntity mediaEntity = newsEntity.getMediaEntity().get(0);

        assertEquals(mediaEntity.getUrl(), multimedia.getString(URL));

        jsonObject = jsonArray.getJSONObject(INDEX.MEDIA.ordinal());
        newsEntity = new NewsEntity(jsonObject);

        assertEquals(newsEntity.getMediaEntity().size(), 0);

        jsonObject = jsonArray.getJSONObject(INDEX.ALL.ordinal());
        newsEntity = new NewsEntity(jsonObject);

        assertEquals(newsEntity.getMediaEntity().size(), 0);
    }

}