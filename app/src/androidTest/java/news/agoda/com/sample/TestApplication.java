package news.agoda.com.sample;

import news.agoda.com.sample.app.AgodaNewsApp;
import news.agoda.com.sample.interactor.DataInterator;
import news.agoda.com.sample.network.MockWebServiceDataInterator;

/**
 * Created by Sundararaghavan on 11/26/2018.
 */

public class TestApplication extends AgodaNewsApp {
    private final DataInterator dataInterator=new MockWebServiceDataInterator();

    public DataInterator provideNetworkDataInteractor(){
        return dataInterator;
    }
}
