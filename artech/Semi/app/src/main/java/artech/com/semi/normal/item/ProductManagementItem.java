package artech.com.semi.normal.item;

import android.graphics.Bitmap;

/**
 * Created by moon on 2018-05-09.
 */

public class ProductManagementItem {
    private String mId;
    private String mTitle;
    private String mContents;
    private String mShop;
    private String mAddress;
    private int mDefaultPrice;
    private int mPrice;
    private int mSalePrice;
    private int mRecommend;
    private int mState;
    private int mFreshness;
//    private int mThumbnail;
    private Bitmap mThumbnail;

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContents() {
        return mContents;
    }

    public String getShop() {
        return mShop;
    }

    public String getAddress() {
        return mAddress;
    }

    public int getPrice() {
        return mPrice;
    }

    public int getDefaultPrice() {
        return mDefaultPrice;
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

    public int getFreshness() {
        return mFreshness;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setContents(String contents) {
        this.mContents = contents;
    }

    public void setShop(String shop) {
        this.mShop = shop;
    }

    public void setAddress(String address) {
        this.mAddress = address;
    }

    public void setDefaultPrice(int value) {
        this.mDefaultPrice = value;
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

    public void setFreshness(int value) {
        this.mFreshness = value;
    }

    public Bitmap getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.mThumbnail = thumbnail;
    }
}
