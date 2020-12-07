package artech.com.semi.normal.item;

import android.graphics.Bitmap;

/**
 * Created by moon on 2018-05-09.
 */

public class ProductReviewItem {
    private String mId;
    private String mName;
    private String mTitle;
    private String mContents;
    private String mDate;
    private String mReplyId;
    private String mReplyName;
    private String mReplyTitle;
    private String mReplyContents;
    private String mReplyDate;
    private int mFlag;
    private int mPoint;
//    private int mThumbnail;
    private Bitmap mThumbnail;
    private Bitmap mFirstImg;
    private Bitmap mSecondImg;
    private Bitmap mThirdImg;

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getContents() {
        return mContents;
    }

    public String getName() {
        return mName;
    }

    public String getDate() {
        return mDate;
    }

    public int getFlag() {
        return mFlag;
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

    public void setContents(String contents) {
        this.mContents = contents;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public void setFlag(int value) {
        this.mFlag = value;
    }

    public void setPoint(int value) {
        this.mPoint = value;
    }

    public Bitmap getThumbnail() {
        return mThumbnail;
    }

    public Bitmap getFirstImg() {
        return mFirstImg;
    }

    public Bitmap getSecondImg() {
        return mSecondImg;
    }

    public Bitmap getThirdImg() {
        return mThirdImg;
    }

    public void setThumbnail(Bitmap thumbnail) {
        this.mThumbnail = thumbnail;
    }

    public void setFirstImg(Bitmap img) {
        this.mFirstImg = img;
    }

    public void setSecondImg(Bitmap img) {
        this.mSecondImg = img;
    }

    public void setThirdImg(Bitmap img) {
        this.mThirdImg = img;
    }



}
