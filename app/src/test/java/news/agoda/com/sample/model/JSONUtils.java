package news.agoda.com.sample.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JSONUtils {

    public static final String RESULTS = "results";

    public static JSONArray fromStream(InputStream inputStream) throws JSONException {
        String fileContent = readStream(inputStream);
        JSONObject jsonObject =new JSONObject(fileContent);
        return  jsonObject.getJSONArray(RESULTS);
    }
    public static String readStream(InputStream in) {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {

            String nextLine = "";
            while ((nextLine = reader.readLine()) != null) {
                sb.append(nextLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static List<NewsEntity> transformJsonToEntity(JSONArray jsonArray) throws JSONException {
        List<NewsEntity> newsEntities = new ArrayList<>();
        for (int i = 0;  i < jsonArray.length(); i++) {
            JSONObject newsObject = jsonArray.getJSONObject(i);
            newsEntities.add(new NewsEntity(newsObject));
        }
        return newsEntities;
    }

}
