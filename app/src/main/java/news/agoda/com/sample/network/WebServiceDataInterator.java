package news.agoda.com.sample.network;

import android.support.annotation.VisibleForTesting;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import news.agoda.com.sample.interactor.DataInterator;
import news.agoda.com.sample.model.NewsEntity;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class WebServiceDataInterator implements DataInterator {
    public static String getBaseURL() {
        return BASE_URL;
    }

    private static final String BASE_URL = "https://api.myjson.com/";
    private static final String TAG = "WebServiceDataInterator";
    private List<NewsEntity> newsEntities;
    boolean inProgress = false;



    public static APIClient getAPIClient() {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(APIClient.class);
    }

    public Single<List<NewsEntity>> getNewsObservable() {
        inProgress = true;
        return getAPIClient().getNetworkObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .map(new Function<String, List<NewsEntity>>() {
                    @Override
                    public List<NewsEntity> apply(String s) throws Exception {
                        return onResult(s);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public List<NewsEntity> getCachedNews() {
        return newsEntities;
    }

    @Override
    public boolean loadInProgress() {
        return inProgress;
    }

    @VisibleForTesting
    private List<NewsEntity> onResult(final String data) throws JSONException {

        JSONObject jsonObject;
        newsEntities = new ArrayList<>();
        jsonObject = new JSONObject(data);
        JSONArray resultArray = jsonObject.getJSONArray("results");
        for (int i = 0;  i < resultArray.length(); i++) {
            JSONObject newsObject = resultArray.getJSONObject(i);
            newsEntities.add(new NewsEntity(newsObject));
        }
        inProgress = false;
        return newsEntities;
    }

}
