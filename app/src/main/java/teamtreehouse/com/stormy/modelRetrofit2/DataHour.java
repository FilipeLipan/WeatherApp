package teamtreehouse.com.stormy.modelRetrofit2;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lispa on 23/07/2016.
 */
public class DataHour implements Parcelable {
    public long time;
    public String summary;
    public String icon;
    public double temperature;

    private DataHour(Parcel in) {
        time = in.readLong();
        summary = in.readString();
        icon = in.readString();
        temperature = in.readDouble();
    }

    public String getHour() {
        SimpleDateFormat formatter = new SimpleDateFormat("h a");
        Date date = new Date(time * 1000);
        return formatter.format(date);
    }

    public static final Creator<DataHour> CREATOR = new Creator<DataHour>() {
        @Override
        public DataHour createFromParcel(Parcel in) {
            return new DataHour(in);
        }

        @Override
        public DataHour[] newArray(int size) {
            return new DataHour[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time);
        dest.writeString(summary);
        dest.writeString(icon);
        dest.writeDouble(temperature);
    }


    public int getTemperature() {
        return (int) Math.round(temperature) ;
    }
}
