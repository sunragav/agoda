package news.agoda.com.sample.screens.homevp;

import java.util.List;

import news.agoda.com.sample.model.NewsEntity;

public interface NewsListVP {
    interface NewsListPresenter {
        void loadNews();

        void unregister();

    }

    interface NewsListView {
        String SELECTED_NEWS = "selectedNews";

        void showNews(List<NewsEntity> newsEntities);

        void showProgressBar();

        void loadNews();

        void showError();
    }
}
