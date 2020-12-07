package artech.com.semi.normal.item;

import android.graphics.Bitmap;

/**
 * Created by moon on 2018-05-09.
 */

public class PurchaseHistoryItem {
    private String mId;
    private String mTitle;
    private String mOrderNumber;
    private String mOrderTime;
    private String mShop;
    private String mAddress;
    private int mState;
//    private int mThumbnail;
    private Bitmap mThumbnail;

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getOrderNumber() {
        return mOrderNumber;
    }

    public String getOrderTime() {
        return mOrderTime;
    }

    public String getShop() {
        return mShop;
    }

    public String getAddress() {
        return mAddress;
    }

    public int getState() {
        return mState;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setOrderNumber(String number) {
        this.mOrderNumber = number;
    }

    public void setOrderTime(String time) {
        this.mOrderTime = time;
    }

    public void setState(int value) {
        this.mState = value;
    }

    public void setShop(String shop) {
        this.mShop = shop;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public Bitmap getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.mThumbnail = thumbnail;
    }
}
