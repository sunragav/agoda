package news.agoda.com.sample.app;

import android.app.Application;

import news.agoda.com.sample.interactor.DataInterator;
import news.agoda.com.sample.network.WebServiceDataInterator;

public class AgodaNewsApp extends Application {
    DataInterator dataInterator;
    @Override
    public void onCreate() {
        super.onCreate();
        dataInterator = new WebServiceDataInterator();
    }

    public DataInterator provideNetworkDataInteractor(){
        return dataInterator;
    }
}
