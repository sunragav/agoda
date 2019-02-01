package news.agoda.com.sample.screens.homevp;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import news.agoda.com.sample.model.NewsEntity;
import news.agoda.com.sample.interactor.DataInterator;

public class NewListPresenterImpl implements NewsListVP.NewsListPresenter {

    private NewsListVP.NewsListView view;
    private DataInterator dataInterator;
    private Disposable disposable;

    public NewListPresenterImpl(final NewsListVP.NewsListView view, DataInterator dataInterator) {
        this.view = view;
        this.dataInterator = dataInterator;


    }


    @Override
    public void loadNews() {
        if (dataInterator.getCachedNews() == null || dataInterator.getCachedNews().isEmpty()) {
            if(!dataInterator.loadInProgress()) {
                dataInterator.getNewsObservable()
                        .subscribe(new SingleObserver<List<NewsEntity>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                if (view != null)
                                    view.showProgressBar();
                                disposable = d;
                            }

                            @Override
                            public void onSuccess(List<NewsEntity> newsEntities) {
                                if (view != null)
                                    view.showNews(newsEntities);
                            }


                            @Override
                            public void onError(Throwable e) {
                                if (view != null)
                                    view.showError();
                            }

                        });
            }
        } else {
            view.showNews(dataInterator.getCachedNews());
        }
    }


    @Override
    public void unregister() {
        view = null;
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
            disposable=null;
        }
    }

}