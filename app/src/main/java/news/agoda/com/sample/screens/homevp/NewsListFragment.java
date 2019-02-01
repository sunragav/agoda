package news.agoda.com.sample.screens.homevp;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.backends.pipeline.Fresco;

import java.util.ArrayList;
import java.util.List;

import news.agoda.com.sample.R;
import news.agoda.com.sample.app.AgodaNewsApp;
import news.agoda.com.sample.model.NewsEntity;
import news.agoda.com.sample.router.NewsListItemActionListener;
import news.agoda.com.sample.router.NewsUpdatedListener;
import news.agoda.com.sample.screens.MainActivity;

public class NewsListFragment extends Fragment implements NewsListVP.NewsListView {
    private static final String TAG = "NewsListFragment";
    View rootView;
    private Handler handler = new Handler(Looper.getMainLooper());
    ListView newsListView;
    NewsListAdapter newsListAdapter;
    Context context;
    List<NewsEntity> newsList = new ArrayList<>();
    private NewsListItemActionListener newsListItemActionListener;
    ProgressBar progressBar;
    TextView tvError;
    NewsListVP.NewsListPresenter newsListPresenter;
    NewsUpdatedListener newsUpdatedListener;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        newsListPresenter = new NewListPresenterImpl(this,((AgodaNewsApp)getActivity().getApplication()).provideNetworkDataInteractor());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_news_list, container, false);
        initUI();
        newsListItemActionListener = ((MainActivity) getActivity()).getRootCoordinator();
        newsUpdatedListener =  ((MainActivity) getActivity()).getRootCoordinator();
        Fresco.initialize(getContext());
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        newsListPresenter.loadNews();
    }



    private void initUI() {

        context = getContext();

        progressBar = (ProgressBar) rootView.findViewById(R.id.news_list_progress_bar);
        tvError = (TextView) rootView.findViewById(R.id.news_error);
        newsListView = (ListView) rootView.findViewById(R.id.news_list_view);
        newsListAdapter = new NewsListAdapter(getContext(), R.layout.list_item_news, newsList);
        newsListView.setAdapter(newsListAdapter);
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewsEntity newsEntity = newsList.get(position);
                String title = newsEntity.getTitle();
                newsListItemActionListener.onItemClicked(position);
            }
        });

    }


    public void setItemActionListener(NewsListItemActionListener newsListItemActionListener) {
        this.newsListItemActionListener = newsListItemActionListener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        newsListPresenter.unregister();
    }

    @Override
    public void showNews(List<NewsEntity> newsEntities) {
        newsList = newsEntities;
        newsListAdapter.addAll(newsEntities);
        newsListAdapter.notifyDataSetChanged();
        newsListView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        tvError.setVisibility(View.GONE);
        newsUpdatedListener.propogateNews();
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);
        newsListView.setVisibility(View.GONE);

    }

    @Override
    public void loadNews() {
        newsListPresenter.loadNews();
    }

    @Override
    public void showError() {
        tvError.setVisibility(View.VISIBLE);
        newsListView.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        newsUpdatedListener.propogateError();
    }
}
