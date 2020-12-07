package artech.com.semi.normal.item;


/**
 * Created by moon on 2018-05-09.
 */

public class QuestionItem {
    private String mTitle;
    private String mDate;
    private String mContents;


    public String getTitle() {
        return mTitle;
    }

    public String getDate() {
        return mDate;
    }

    public String getContents() {
        return mContents;
    }



    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setDate(String date) {
        this.mDate = date;
    }

    public void setContents(String contents) {
        this.mContents = contents;
    }
}
