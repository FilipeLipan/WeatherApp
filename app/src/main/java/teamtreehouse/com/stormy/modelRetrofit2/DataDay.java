package teamtreehouse.com.stormy.modelRetrofit2;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by lispa on 23/07/2016.
 */
public class DataDay implements Parcelable {
    public long time;
    public String summary;
    public double apparentTemperatureMax;
    public String icon;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeString(summary);
        dest.writeDouble(apparentTemperatureMax);
        dest.writeString(icon);
    }

    private DataDay(Parcel in) {
        time = in.readLong();
        summary = in.readString();
        apparentTemperatureMax = in.readDouble();
        icon = in.readString();
    }

    public static final Creator<DataDay> CREATOR = new Creator<DataDay>() {
        @Override
        public DataDay createFromParcel(Parcel in) {
            return new DataDay(in);
        }

        @Override
        public DataDay[] newArray(int size) {
            return new DataDay[size];
        }
    };

    public DataDay(){

    }

    public String getDayOfTheWeek(String timezone) {
        SimpleDateFormat formatter = new SimpleDateFormat("EEEE");
        formatter.setTimeZone(TimeZone.getTimeZone(timezone));
        Date dateTime = new Date(time * 1000);
        return formatter.format(dateTime);
    }

    public int getTemperatureMax() {
        return (int)Math.round(apparentTemperatureMax);
    }


}
