package com.prj.ShoppingList.data;

import android.os.Parcel;
import android.os.Parcelable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ProductItem extends RealmObject implements Parcelable {

    private String itemName;
    private String itemCategory;
    private double itemPrice;
    private String itemDescription;


    private boolean purchased;

    @PrimaryKey
    private String itemID;


    public ProductItem() {
    }

    public ProductItem(String itemName, boolean done) {
        this.itemName = itemName;
        this.purchased = done;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public boolean isPurchased() {
        return purchased;
    }

    public void setPurchased(boolean purchased) {
        this.purchased = purchased;
    }

    public String getItemID() {
        return itemID;
    }

    public String getItemCategory() {
        return itemCategory;
    }

    public void setItemCategory(String itemCategory) {
        this.itemCategory = itemCategory;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel out, int i) {
        out.writeString(itemID);
        out.writeString(itemName);
        out.writeString(itemCategory);
        out.writeString(itemDescription);
        out.writeDouble(itemPrice);
    }
    public static final Parcelable.Creator<ProductItem> CREATOR = new Parcelable.Creator<ProductItem>() {
        public ProductItem createFromParcel(Parcel in) {
            return new ProductItem(in);
        }

        public ProductItem[] newArray(int size) {
            return new ProductItem[size];
        }
    };
    private ProductItem(Parcel in) {
        itemPrice = in.readDouble();
        itemID = in.readString();
        itemName = in.readString();
        itemCategory = in.readString();
        itemDescription = in.readString();

    }
}
