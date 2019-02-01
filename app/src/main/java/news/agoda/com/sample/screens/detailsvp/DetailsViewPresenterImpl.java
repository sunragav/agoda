package news.agoda.com.sample.screens.detailsvp;

import java.util.List;

import io.reactivex.disposables.Disposable;
import news.agoda.com.sample.model.NewsEntity;
import news.agoda.com.sample.interactor.DataInterator;

public class DetailsViewPresenterImpl implements DetailsVP.DetailsViewPresenter {
    private DetailsVP.DetailsView view;
    private DataInterator dataInterator;
    private int position = 0;
    private Disposable disposable;

    public DetailsViewPresenterImpl(final DetailsVP.DetailsView view, DataInterator dataInterator) {
        this.view = view;
        this.dataInterator = dataInterator;

    }

    @Override
    public void loadNews(final int position) {
        this.position = position;
        List<NewsEntity> cachedNews = dataInterator.getCachedNews();

        if(cachedNews!=null && !cachedNews.isEmpty())
            view.showNews(cachedNews.get(position));
        else
            view.showLoading();

    }

    @Override
    public void unregister() {
        view = null;
    }
}
