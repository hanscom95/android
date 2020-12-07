package artech.com.semi.business.item;

import android.graphics.Bitmap;

/**
 * Created by moon on 2018-05-09.
 */

public class ProductManagementItem {
    private String mId;
    private String mName;
    private String mDate;
    private int mPrice;
    private int mSalePrice;
    private int mRecommend;
    private int mState;
//    private int mThumbnail;
    private Bitmap mThumbnail;

    public String getId() {
        return mId;
    }

    public String getName() {
        return mName;
    }

    public String getDate() {
        return mDate;
    }

    public int getPrice() {
        return mPrice;
    }

    public int getSalePrice() {
        return mSalePrice;
    }

    public int getRecommend() {
        return mRecommend;
    }

    public int getState() {
        return mState;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setDate(String value) {
        this.mDate = value;
    }

    public void setPrice(int value) {
        this.mPrice = value;
    }

    public void setSalePrice(int value) {
        this.mSalePrice = value;
    }

    public void setRecommand(int value) {
        this.mRecommend = value;
    }

    public void setState(int value) {
        this.mState = value;
    }

    public Bitmap getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.mThumbnail = thumbnail;
    }
}
