package news.agoda.com.sample.network;

import android.support.test.espresso.IdlingResource;

import news.agoda.com.sample.interactor.DataInterator;

/**
 * Created by Sundararaghavan on 11/27/2018.
 */

public class NetworkIdlingResource implements IdlingResource {

    private DataInterator dataInterator;
    private ResourceCallback callback;

    public NetworkIdlingResource(DataInterator dataInterator) {

        this.dataInterator = dataInterator;
    }

    @Override
    public String getName() {
        return NetworkIdlingResource.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        boolean isIdle = dataInterator.loadInProgress();
        if (isIdle)
            callback.onTransitionToIdle();
        return isIdle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }
}
