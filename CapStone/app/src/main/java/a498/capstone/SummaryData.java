package a498.capstone;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * This object is used to contain the information about receipt from the summary table. The class must
 * implement the Parceable interface in order to be passed as a bundle to a fragment.
 *
 * Created by patrickgibson on 2018-01-27.
 */

public class SummaryData implements Parcelable {
    private int id;
    private String name;
    private String date;

    public SummaryData(int id, String name, String date){
        this.id = id;
        this.name = name;
        this.date = date;
    }

    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel out, int flags){
        out.writeInt(id);
        out.writeString(name);
        out.writeString(date);
    }

    public static final Parcelable.Creator<SummaryData> CREATOR
            = new Parcelable.Creator<SummaryData>() {
        public SummaryData createFromParcel(Parcel in) {
            return new SummaryData(in);
        }

        public SummaryData[] newArray(int size) {
            return new SummaryData[size];
        }
    };

    private SummaryData(Parcel in){
        id = in.readInt();
        name = in.readString();
        date = in.readString();
    }

    @Override
    public String toString() {
        return name + "      " + date;
    }

    public int getID(){
        return id;
    }

    public String getName(){
        return name;
    }

    public String getDate(){
        return date;
    }

}
