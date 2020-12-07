package artech.com.semi.normal.item;

import android.graphics.Bitmap;

/**
 * Created by moon on 2018-05-09.
 */

public class FavoritesItem {
    private String mId;
    private String mFId;
    private String mTitle;
    private String mContents;
    private String mShop;
    private String mAddress;
    private int mFlag;
    private int mChecked;
//    private int mThumbnail;
    private Bitmap mThumbnail;

    public String getId() {
        return mId;
    }

    public String getFId() {
        return mFId;
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

    public int getFlag() {
        return mFlag;
    }

    public int getChecked() {
        return mChecked;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public void setFId(String id) {
        this.mFId = id;
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

    public void setFlag(int value) {
        this.mFlag = value;
    }

    public void setChecked(int checked) {
        this.mChecked = checked;
    }

    public Bitmap getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.mThumbnail = thumbnail;
    }
}
