package artech.com.semi.business.item;

import android.graphics.Bitmap;

/**
 * Created by moon on 2018-05-09.
 */

public class SaleManagementItem {
    private String mId;
    private String mTitle;
    private String mRegDate;
    private String mReceiptDate;
    private String mName;
    private int mAmmount;
    private int mState;
    private int mChecked;
//    private int mThumbnail;
    private Bitmap mThumbnail;

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getName() {
        return mName;
    }

    public String getRegDate() {
        return mRegDate;
    }

    public String getReceiptDate() {
        return mReceiptDate;
    }

    public int getAmmount() {
        return mAmmount;
    }

    public int getState() {
        return mState;
    }

    public int getChecked() {
        return mChecked;
    }

    public void setId(String id) {
        this.mId = id;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setRegDate(String value) {
        this.mRegDate = value;
    }

    public void setReceiptDate(String value) {
        this.mReceiptDate = value;
    }

    public void setAmmount(int value) {
        this.mAmmount = value;
    }

    public void setState(int value) {
        this.mState = value;
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
