package artech.com.semi.business.item;

import android.graphics.Bitmap;

/**
 * Created by moon on 2018-05-09.
 */

public class QuestionManagementItem {
    private String mId;
    private String mTitle;
    private String mRegDate;
    private String mContent;
    private String mName;
    private int mState;
    private int mPoint;
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

    public String getContent() {
        return mContent;
    }

    public int getState() {
        return mState;
    }

    public int getPoint() {
        return mPoint;
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

    public void setContent(String value) {
        this.mContent = value;
    }

    public void setState(int value) {
        this.mState = value;
    }

    public void setPoint(int value) {
        this.mPoint = value;
    }

    public Bitmap getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.mThumbnail = thumbnail;
    }
}
