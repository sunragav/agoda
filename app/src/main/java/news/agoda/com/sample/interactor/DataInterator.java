package news.agoda.com.sample.interactor;

import java.util.List;

import io.reactivex.Single;
import news.agoda.com.sample.model.NewsEntity;

public interface DataInterator {
    Single<List<NewsEntity>> getNewsObservable();

    List<NewsEntity> getCachedNews();

    boolean loadInProgress();
}
