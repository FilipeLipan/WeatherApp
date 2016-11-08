package teamtreehouse.com.stormy.api;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import teamtreehouse.com.stormy.modelRetrofit2.ForecastRetro;

/**
 * Created by lispa on 16/07/2016.
 */
public interface ForecastService {
    public static final String FORECAST_BASE_URL = "https://api.forecast.io/";

    @GET("/forecast/" + Api.FORECAST_API_KEY + "/{latitude},{longitude}")
    Call<ForecastRetro> forecast (@Path("latitude") String latitude,
                                    @Path("longitude") String longitude);

}
