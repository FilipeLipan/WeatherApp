package teamtreehouse.com.stormy.modelRetrofit2;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by lispa on 23/07/2016.
 */
public class CurrentRetro extends ForecastRetro {

    public String icon;
    public long time;
    public double temperature;
    public double humidity;
    public double precipProbability;
    public String summary;

    public int getPrecipProbability() {
        double precipPercentage = precipProbability * 100;
        return (int) Math.round(precipPercentage);
    }

    public int getTemperature() {
        return (int) temperature;
    }

    public int getHumidity() {
        return (int) Math.round(humidity * 100);
    }

    public String getTime(String timezone) {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a");
        formatter.setTimeZone(TimeZone.getTimeZone(timezone));
        Date dateTime = new Date(time * 1000);
        String timeString = formatter.format(dateTime);
        return timeString;
    }
}
