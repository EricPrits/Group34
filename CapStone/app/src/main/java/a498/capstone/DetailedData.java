package a498.capstone;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by patrickgibson on 2018-01-28.
 */

public class DetailedData implements Parcelable {
    private String foodType;
    private int quantity;

    public DetailedData(String foodType, int quantity){
        this.foodType = foodType;
        this.quantity = quantity;
    }

    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel out, int flags){
        out.writeString(foodType);
        out.writeInt(quantity);
    }

    public static final Parcelable.Creator<DetailedData> CREATOR
            = new Parcelable.Creator<DetailedData>() {
        public DetailedData createFromParcel(Parcel in) {
            return new DetailedData(in);
        }

        public DetailedData[] newArray(int size) {
            return new DetailedData[size];
        }
    };

    private DetailedData(Parcel in) {
        foodType = in.readString();
        quantity = in.readInt();
    }

    public String getFoodType(){
        return foodType;
    }

    public int getQuantity(){
        return quantity;
    }
}
