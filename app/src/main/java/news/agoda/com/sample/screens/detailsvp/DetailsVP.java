package news.agoda.com.sample.screens.detailsvp;

import news.agoda.com.sample.model.NewsEntity;

public interface DetailsVP {
    interface DetailsView {
        void showError();
        void showNews(NewsEntity entity);
        void showLoading();
    }

    interface DetailsViewPresenter {
        public void loadNews(int pos);
        public void unregister();
    }
}
