package a498.capstone;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by patrickgibson on 2018-01-28.
 */

public class DetailedData implements Parcelable {
    private String foodType;
    private int quantity;
    private int id;
    private String expiryDate;
    private String purchaseDate;

    public DetailedData(String foodType, int quantity, int id, String expiryDate, String purchaseDate){
        this.foodType = foodType;
        this.quantity = quantity;
        this.id = id;
        this.expiryDate = expiryDate;
        this.purchaseDate = purchaseDate;
    }

    public DetailedData(String food){
        foodType = food;
    }


    public int describeContents(){
        return 0;
    }

    public void writeToParcel(Parcel out, int flags){
        out.writeString(foodType);
        out.writeInt(quantity);
        out.writeInt(id);
        out.writeString(expiryDate);
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
        id = in.readInt();
        expiryDate = in.readString();
    }

    public String getFoodType(){
        return foodType;
    }

    public int getQuantity(){
        return quantity;
    }

    public int getId(){ return id;}

    public String getExpiryDate(){ return expiryDate; }

    public String getPurchaseDate() {
        return purchaseDate;
    }

}


