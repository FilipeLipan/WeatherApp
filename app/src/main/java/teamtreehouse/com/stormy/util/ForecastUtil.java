package teamtreehouse.com.stormy.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import butterknife.InjectView;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import teamtreehouse.com.stormy.R;
import teamtreehouse.com.stormy.api.ForecastService;
import teamtreehouse.com.stormy.modelRetrofit2.ForecastRetro;
import teamtreehouse.com.stormy.ui.AlertDialogFragment;

/**
 * Created by lispa on 27/07/2016.
 */
public class ForecastUtil {

    // method used to get the resource values to change the icon
    public static int getIconId(String iconString) {
        int iconId = R.drawable.clear_day;

        if (iconString.equals("clear-day")) {
            iconId = R.drawable.clear_day;
        } else if (iconString.equals("clear-night")) {
            iconId = R.drawable.clear_night;
        } else if (iconString.equals("rain")) {
            iconId = R.drawable.rain;
        } else if (iconString.equals("snow")) {
            iconId = R.drawable.snow;
        } else if (iconString.equals("sleet")) {
            iconId = R.drawable.sleet;
        } else if (iconString.equals("wind")) {
            iconId = R.drawable.wind;
        } else if (iconString.equals("fog")) {
            iconId = R.drawable.fog;
        } else if (iconString.equals("cloudy")) {
            iconId = R.drawable.cloudy;
        } else if (iconString.equals("partly-cloudy-day")) {
            iconId = R.drawable.partly_cloudy;
        } else if (iconString.equals("partly-cloudy-night")) {
            iconId = R.drawable.cloudy_night;
        }

        return iconId;
    }

}

