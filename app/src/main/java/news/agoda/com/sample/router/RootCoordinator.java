package news.agoda.com.sample.router;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;

import java.util.List;

import news.agoda.com.sample.R;
import news.agoda.com.sample.app.AgodaNewsApp;
import news.agoda.com.sample.model.NewsEntity;
import news.agoda.com.sample.screens.detailsvp.DetailsViewFragment;
import news.agoda.com.sample.screens.homevp.NewsListFragment;

import static news.agoda.com.sample.screens.homevp.NewsListVP.NewsListView.SELECTED_NEWS;

/**
 * Created by Sundararaghavan on 11/27/2018.
 */

public class RootCoordinator implements ConnectivityListener, NewsListItemActionListener, NewsUpdatedListener, StoryLinkClickedListener {

    private DetailsViewFragment detailsViewFragment;
    private NewsListFragment newsListFragment;

    public Bundle getExtras() {
        if (extras == null)
            extras = new Bundle();
        return extras;
    }

    public Bundle getDefaultBundle() {
        List<NewsEntity> cachedNews = ((AgodaNewsApp) context).provideNetworkDataInteractor().getCachedNews();
        if (cachedNews == null || cachedNews.isEmpty())
            return null;
        getExtras().putInt(SELECTED_NEWS, 0);
        return extras;
    }

    private Bundle extras;

    public boolean isPortraitMode() {
        return PORTRAIT_MODE;
    }

    public void setPortraitMode(boolean PORTRAIT_MODE) {
        this.PORTRAIT_MODE = PORTRAIT_MODE;
    }

    private boolean PORTRAIT_MODE = false;

    private Context context;
    private FragmentManager supportFragmentManager;
    private static final int DETAILS = 1;
    public static final String SCREEN_SATE = "screenSate";

    public RootCoordinator(Context context, FragmentManager supportFragmentManager) {
        this.context = context;

        this.supportFragmentManager = supportFragmentManager;
    }

    public void unregister() {
        context = null;
        supportFragmentManager = null;
    }


    public void addNewsListFragment() {
        newsListFragment = new NewsListFragment();
        newsListFragment.setItemActionListener(this);
        supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, newsListFragment)
                .addToBackStack("newsListFragment")
                .commit();
    }

    @Override
    public void propogateError() {
        if (detailsViewFragment != null) detailsViewFragment.showError();
    }

    @Override
    public void onNetworkStateChanged(boolean isConnected) {
        newsListFragment.loadNews();
    }

    @Override
    public void propogateNews() {
        if (!PORTRAIT_MODE) {
            if (extras == null) {
                extras = new Bundle();
                extras.putInt(SELECTED_NEWS, 0);
            }
            addDetailsFragment(extras);
        }
    }

    public void addDetailsFragment(Bundle b) {
        extras = b;
        int fragmentContainer;

        if (PORTRAIT_MODE) {
            fragmentContainer = R.id.fragmentContainer;
        } else {
            fragmentContainer = R.id.fragmentContainer2;
        }

        detailsViewFragment = new DetailsViewFragment();
        detailsViewFragment.setArguments(b);
        detailsViewFragment.setOnFullStoryLinkClickedListener(this);
        supportFragmentManager.beginTransaction()
                .replace(fragmentContainer, detailsViewFragment)
                .addToBackStack("detailsViewFragment")
                .commit();
    }

    @Override
    public void onFullStoryClicked(String storyURL) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(storyURL));
        context.startActivity(intent);
    }


    @Override
    public void onItemClicked(int pos) {
        if (extras == null)
            extras = new Bundle();
        extras.putInt(SELECTED_NEWS, pos);
        extras.putInt(SCREEN_SATE, DETAILS);
        addDetailsFragment(extras);
    }

}
