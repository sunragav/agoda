package news.agoda.com.sample.network;

import android.util.Log;

import org.json.JSONArray;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import news.agoda.com.sample.interactor.DataInterator;
import news.agoda.com.sample.model.NewsEntity;
import news.agoda.com.sample.utils.JSONUtils;

/**
 * Created by Sundararaghavan on 11/26/2018.
 */

public class MockWebServiceDataInterator implements DataInterator {
    private static final String TAG = "MockWebServiceDataInter";
    public List<NewsEntity> newsEntities;
    private boolean isLoading = false;
    JSONArray jsonArray;

    public MockWebServiceDataInterator() {
    }

    public void setResult(JSONArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    @Override
    public Single<List<NewsEntity>> getNewsObservable() {
        return Single.create(new SingleOnSubscribe<List<NewsEntity>>() {
            @Override
            public void subscribe(SingleEmitter<List<NewsEntity>> emitter) throws Exception {
                try {
                    isLoading = true;
                    newsEntities = JSONUtils.transformJsonToEntity(jsonArray);

                    emitter.onSuccess(newsEntities);
                } catch (Exception e) {
                    e.printStackTrace();
                    emitter.onError(e);
                    Log.d(TAG, "Excetion while loading fake json");
                }
                finally {
                    isLoading = false;
                }
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread());
    }

    @Override
    public List<NewsEntity> getCachedNews() {
        return newsEntities;
    }

    @Override
    public boolean loadInProgress() {
        return isLoading;
    }
}
