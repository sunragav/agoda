package news.agoda.com.sample.screens.homevp;

import android.accounts.NetworkErrorException;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.schedulers.Schedulers;
import news.agoda.com.sample.model.JSONUtils;
import news.agoda.com.sample.model.NewsEntity;
import news.agoda.com.sample.interactor.DataInterator;

import static org.junit.Assert.assertEquals;

public class NewListPresenterImplTest {
    DataInterator dataInterator;
    NewsListVP.NewsListView newsListView;
    NewsListVP.NewsListPresenter newsListPresenter;
    private JSONArray jsonArray;

    @Before
    public void setup() throws JSONException {
        newsListView = Mockito.mock(NewsListVP.NewsListView.class);
        dataInterator = Mockito.mock(DataInterator.class);
        newsListPresenter = new NewListPresenterImpl(newsListView, dataInterator);
        jsonArray = JSONUtils.fromStream(NewsEntity.class.getResourceAsStream("/sampleData/nullTest.json"));

    }

    @Test
    public void loadNewsFromServerSuccess() throws JSONException {
        final List<NewsEntity> newsEntities =JSONUtils.transformJsonToEntity(jsonArray);
        Mockito.when(dataInterator.getCachedNews()).thenReturn(null);
        Mockito.when(dataInterator.loadInProgress()).thenReturn(false);
        Mockito.when(dataInterator.getNewsObservable()).thenReturn(Single.create(new SingleOnSubscribe<List<NewsEntity>>() {
            @Override
            public void subscribe(SingleEmitter<List<NewsEntity>> emitter) throws Exception {
                emitter.onSuccess(newsEntities);
            }
        }).subscribeOn(Schedulers.trampoline()).observeOn(Schedulers.trampoline()));
        newsListPresenter.loadNews();
        Mockito.verify(newsListView, Mockito.times(1)).showProgressBar();
        ArgumentCaptor<List<NewsEntity>> captor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(newsListView, Mockito.times(1)).showNews(captor.capture());
        assertEquals(captor.getValue(),newsEntities);
    }

    @Test
    public void loadNewsFromServerFailure()  {

        Mockito.when(dataInterator.getCachedNews()).thenReturn(null);
        Mockito.when(dataInterator.loadInProgress()).thenReturn(false);
        Mockito.when(dataInterator.getNewsObservable()).thenReturn(Single.create(new SingleOnSubscribe<List<NewsEntity>>() {
            @Override
            public void subscribe(SingleEmitter<List<NewsEntity>> emitter) throws Exception {
                emitter.onError(new NetworkErrorException());
            }
        }).subscribeOn(Schedulers.trampoline()).observeOn(Schedulers.trampoline()));
        newsListPresenter.loadNews();
        Mockito.verify(newsListView, Mockito.times(1)).showProgressBar();
        Mockito.verify(newsListView, Mockito.times(1)).showError();
}

    @Test
    public void loadNewsFromCache() throws JSONException {
        final List<NewsEntity> newsEntities =JSONUtils.transformJsonToEntity(jsonArray);
        Mockito.when(dataInterator.getCachedNews()).thenReturn(newsEntities);
        Mockito.when(dataInterator.loadInProgress()).thenReturn(false);

        newsListPresenter.loadNews();
        ArgumentCaptor<List<NewsEntity>> captor = ArgumentCaptor.forClass(List.class);
        Mockito.verify(newsListView, Mockito.times(1)).showNews(captor.capture());
        assertEquals(captor.getValue(),newsEntities);
    }
}