package news.agoda.com.sample.screens.detailsvp;

import org.json.JSONArray;
import org.json.JSONException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.List;

import news.agoda.com.sample.model.JSONUtils;
import news.agoda.com.sample.model.NewsEntity;
import news.agoda.com.sample.interactor.DataInterator;

import static org.junit.Assert.assertEquals;

public class DetailsViewPresenterImplTest {
    DataInterator dataInterator;
    DetailsVP.DetailsView detailsView;
    DetailsVP.DetailsViewPresenter detailsViewPresenter;
    private JSONArray jsonArray;

    @Before
    public void setup() throws JSONException {
        detailsView = Mockito.mock(DetailsVP.DetailsView.class);
        dataInterator = Mockito.mock(DataInterator.class);
        detailsViewPresenter = new DetailsViewPresenterImpl(detailsView, dataInterator);
        jsonArray = JSONUtils.fromStream(NewsEntity.class.getResourceAsStream("/sampleData/nullTest.json"));
    }

    @Test
    public void showNews() throws JSONException {
        final List<NewsEntity> newsEntities =JSONUtils.transformJsonToEntity(jsonArray);
        Mockito.when(dataInterator.getCachedNews()).thenReturn(newsEntities);

        detailsViewPresenter.loadNews(1);
        ArgumentCaptor<NewsEntity> captor = ArgumentCaptor.forClass(NewsEntity.class);
        Mockito.verify(detailsView, Mockito.times(1)).showNews(captor.capture());
        assertEquals(captor.getValue(),newsEntities.get(1));
    }

    @Test
    public void loading() throws JSONException {
        Mockito.when(dataInterator.getCachedNews()).thenReturn(null);
        detailsViewPresenter.loadNews(1);
        Mockito.verify(detailsView, Mockito.times(1)).showLoading();
    }
}