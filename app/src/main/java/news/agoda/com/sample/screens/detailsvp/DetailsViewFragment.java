package news.agoda.com.sample.screens.detailsvp;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.common.util.UriUtil;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.DraweeView;
import com.facebook.imagepipeline.request.ImageRequest;

import java.util.List;

import news.agoda.com.sample.R;
import news.agoda.com.sample.app.AgodaNewsApp;
import news.agoda.com.sample.model.NewsEntity;
import news.agoda.com.sample.router.StoryLinkClickedListener;
import news.agoda.com.sample.screens.MainActivity;

import static news.agoda.com.sample.screens.homevp.NewsListVP.NewsListView.SELECTED_NEWS;

public class DetailsViewFragment extends Fragment implements DetailsVP.DetailsView {
    private String storyURL = "";
    View rootView;
    Bundle extras;
    private StoryLinkClickedListener storyLinkClickedListener;
    private DetailsVP.DetailsViewPresenter detailsViewPresenter;
    private int position;
    View tvError;
    ProgressBar progressBar;
    private TextView titleView;
    private DraweeView imageView;
    private TextView summaryView;
    private Button button;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_detail, container, false);
        storyLinkClickedListener = ((MainActivity)getActivity()).getRootCoordinator();
        if (savedInstanceState != null) {
            extras = savedInstanceState;
        } else {
            extras = getArguments();
        }
        initUI();
        return rootView;
    }

    @Override
    public void onStop() {
        super.onStop();
        detailsViewPresenter.unregister();
    }

    private void initUI() {
        progressBar = (ProgressBar) rootView.findViewById(R.id.detailed_news_progress_bar);
        tvError = rootView.findViewById(R.id.detailed_news_error);
        titleView = (TextView) rootView.findViewById(R.id.detailed_news_title);
        imageView = (DraweeView) rootView.findViewById(R.id.detailed_news_image);
        summaryView = (TextView) rootView.findViewById(R.id.detailed_summary_content);
        button = (Button) rootView.findViewById(R.id.full_story_link);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storyLinkClickedListener.onFullStoryClicked(storyURL);
            }
        });
        List<NewsEntity> cachedNews = ((AgodaNewsApp) getActivity().getApplication()).provideNetworkDataInteractor().getCachedNews();
        if (extras == null || cachedNews == null || cachedNews.isEmpty()) {
            String title = getContext().getResources().getString(R.string.no_news_yet);
            TextView titleView = (TextView) rootView.findViewById(R.id.detailed_news_title);
            titleView.setText(title);
            return;
        }
        position = extras.getInt(SELECTED_NEWS, 0);


        storyURL = cachedNews.get(position).getArticleUrl();
        String title = cachedNews.get(position).getTitle();
        String summary = cachedNews.get(position).getSummary();
        String imageURL = null;
        if (!cachedNews.get(position).getMediaEntity().isEmpty())
            imageURL = cachedNews.get(position).getMediaEntity().get(0).getUrl();

        titleView.setText(title);
        summaryView.setText(summary);
        if (imageURL != null) {
            DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                    .setImageRequest(ImageRequest.fromUri(Uri.parse(imageURL)))
                    .setOldController(imageView.getController()).build();
            imageView.setController(draweeController);
        }
        else{

            Uri uri = new Uri.Builder()
                    .scheme(UriUtil.LOCAL_RESOURCE_SCHEME) // "res"
                    .path(String.valueOf(R.drawable.place_holder))
                    .build();
            DraweeController draweeControlleri = Fresco.newDraweeControllerBuilder().setUri(uri).build();
            imageView.setController(draweeControlleri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        detailsViewPresenter = new DetailsViewPresenterImpl(this, ((AgodaNewsApp) getActivity().getApplication()).provideNetworkDataInteractor());
        detailsViewPresenter.loadNews(position);
    }



    public void setOnFullStoryLinkClickedListener(StoryLinkClickedListener storyLinkClickedListener) {
        this.storyLinkClickedListener = storyLinkClickedListener;
    }


    private void hideUI() {
        titleView.setVisibility(View.GONE);
        imageView.setVisibility(View.GONE);
        button.setVisibility(View.GONE);
        summaryView.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        tvError.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        hideUI();
    }

    @Override
    public void showNews(NewsEntity entity) {
        if (entity.getTitle() != null && !entity.getTitle().isEmpty())
            titleView.setVisibility(View.VISIBLE);
        if (entity.getMediaEntity() != null && !entity.getMediaEntity().isEmpty())
            imageView.setVisibility(View.VISIBLE);
        if (entity.getSummary() != null && !entity.getSummary().isEmpty())
            summaryView.setVisibility(View.VISIBLE);
        if (entity.getArticleUrl() != null && !entity.getArticleUrl().isEmpty())
            button.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);
        hideUI();
    }
}
