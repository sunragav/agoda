package news.agoda.com.sample.network;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface APIClient {

    @GET("bins/nl6jh")
    Single<String> getNetworkObservable();
}