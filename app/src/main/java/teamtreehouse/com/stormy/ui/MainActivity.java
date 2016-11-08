package teamtreehouse.com.stormy.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import teamtreehouse.com.stormy.R;
import teamtreehouse.com.stormy.api.ForecastService;
import teamtreehouse.com.stormy.modelRetrofit2.ForecastRetro;
import teamtreehouse.com.stormy.util.ForecastUtil;
import teamtreehouse.com.stormy.util.GPSTracker;


public class MainActivity extends ActionBarActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    public static final String DAILY_FORECAST = "DAILY_FORECAST";
    public static final String HOURLY_FORECAST = "HOURLY_FORECAST";
    public static final String TIMEZONE = "TIMEZONE";

    private ForecastRetro mForecastRetro;

    @InjectView(R.id.timeLabel)
    TextView mTimeLabel;
    @InjectView(R.id.temperatureLabel)
    TextView mTemperatureLabel;
    @InjectView(R.id.humidityValue)
    TextView mHumidityValue;
    @InjectView(R.id.precipValue)
    TextView mPrecipValue;
    @InjectView(R.id.summaryLabel)
    TextView mSummaryLabel;
    @InjectView(R.id.iconImageView)
    ImageView mIconImageView;
    @InjectView(R.id.refreshImageView)
    ImageView mRefreshImageView;
    @InjectView(R.id.progressBar)
    ProgressBar mProgressBar;
    @InjectView(R.id.locationLabel)
    TextView mLocationLabel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        String[] latitudeLongitude = getLatitudeLongitude();
        final String latitude = latitudeLongitude[0];
        final String longitude = latitudeLongitude[1];

        mRefreshImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] latitudeLongitude = getLatitudeLongitude();
                final String latitude = latitudeLongitude[0];
                final String longitude = latitudeLongitude[1];
                getForecast(latitude, longitude);
            }
        });

        getForecast(latitude, longitude);

        Log.d(TAG, "Main UI code is running!");
    }


    public void getForecast(String latitude, String longitude) {

        // verify the network status
        if (isNetworkAvailable()) {
            toggleRefresh();

            ////////////////new piece of code with retrofit2////////////////////////
            //build the retrofit
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ForecastService.FORECAST_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            //preparing to make a call
            ForecastService service = retrofit.create(ForecastService.class);
            Call<ForecastRetro> requestCurrentForecast = service.forecast(latitude, longitude);

            //enqueue the method to run outside de ui thread
            requestCurrentForecast.enqueue(new retrofit2.Callback<ForecastRetro>() {
                @Override
                public void onResponse(Call<ForecastRetro> call, retrofit2.Response<ForecastRetro> response) {
                    if (!response.isSuccessful()) {
                        //UnSuccessful
                        alertUserAboutError();
                    } else {
                        //successful
                        //receiving the object
                        mForecastRetro = response.body();
                        updateDisplay();
                        toggleRefresh();
                    }
                }

                @Override
                public void onFailure(Call<ForecastRetro> call, Throwable t) {
                    // on failure means you didn't get a response
                    alertUserAboutError();
                }
            });

            ////////////////////////////////////////////////////////////////////////

        } else {
            Toast.makeText(this, getString(R.string.network_unavailable_message),Toast.LENGTH_LONG).show();
        }
    }


    //getting latitude and longitude
    private String[] getLatitudeLongitude(){
        String[] latitudeLongitude = new String[2];

        GPSTracker gps = new GPSTracker(this);
        mProgressBar.setVisibility(View.INVISIBLE);

        DecimalFormat numberFormat = new DecimalFormat("#.0000");
        latitudeLongitude[0] = numberFormat.format(gps.getLatitude());
        latitudeLongitude[1] = numberFormat.format(gps.getLongitude());

        return latitudeLongitude;
    }

    //toggle the loading imageview
    private void toggleRefresh() {
        if (mProgressBar.getVisibility() == View.INVISIBLE) {
            mProgressBar.setVisibility(View.VISIBLE);
            mRefreshImageView.setVisibility(View.INVISIBLE);
        } else {
            mProgressBar.setVisibility(View.INVISIBLE);
            mRefreshImageView.setVisibility(View.VISIBLE);
        }
    }

    //verifying the network
    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        boolean isAvailable = false;
        if (networkInfo != null && networkInfo.isConnected()) {
            isAvailable = true;
        }

        return isAvailable;
    }


    private void updateDisplay() {
        mLocationLabel.setText(mForecastRetro.timezone);
        mTemperatureLabel.setText(mForecastRetro.currently.getTemperature() + "");
        mTimeLabel.setText("At " + mForecastRetro.currently.getTime(mForecastRetro.timezone) + " it will be");
        mHumidityValue.setText(mForecastRetro.currently.getHumidity() + "%");
        mPrecipValue.setText(mForecastRetro.currently.getPrecipProbability() + "%");
        mSummaryLabel.setText(mForecastRetro.currently.summary);

        Drawable drawable = getResources().getDrawable(ForecastUtil.getIconId(mForecastRetro.currently.icon));
        mIconImageView.setImageDrawable(drawable);
    }

    private void alertUserAboutError() {
        AlertDialogFragment dialog = new AlertDialogFragment();
        dialog.show(getFragmentManager(), "error_dialog");
    }

    @OnClick(R.id.dailyButton)
    public void startDailyActivity(View view) {
        if(isNetworkAvailable()){
            Intent intent = new Intent(this, DailyForecastActivity.class);
            intent.putExtra(DAILY_FORECAST, mForecastRetro.daily.data);
            intent.putExtra(TIMEZONE, mForecastRetro.timezone);
            startActivity(intent);
        }else {
            Toast.makeText(this, getString(R.string.network_unavailable_message),Toast.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.hourlyButton)
    public void startHourlyActivity(View view) {
        if(isNetworkAvailable()){
            Intent intent = new Intent(this, HourlyForecastActivity.class);
            intent.putExtra(HOURLY_FORECAST, mForecastRetro.hourly.data);
            intent.putExtra(TIMEZONE, mForecastRetro.timezone);
            startActivity(intent);
        }else {
            Toast.makeText(this, getString(R.string.network_unavailable_message),Toast.LENGTH_LONG).show();
        }
    }

}














