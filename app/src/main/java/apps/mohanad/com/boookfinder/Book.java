package apps.mohanad.com.boookfinder;

/**
 * Created by mohanad on 01/07/17.
 * model class for a book object
 */

public class Book {
    /*the book id */
    private String mId;

    /*the book title*/
    private String mTitle;

    /*the book author*/
    private String mAuthor;

    /*the book image url*/
    private String mImageUrl;

    /*the book rating*/
    private float mRate;

    /*the book json url*/
    private String mJsonUrl;

    public Book(String mId, String mTitle, String mAuthor, String mImageUrl, float mRate , String mJsonUrl) {
        this.mId = mId;
        this.mTitle = mTitle;
        this.mAuthor = mAuthor;
        this.mImageUrl = mImageUrl;
        this.mRate = mRate;
        this.mJsonUrl=mJsonUrl;
    }

    public String getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public float getRate() {
        return mRate;
    }

    public String getJsonUrl() {
        return mJsonUrl;
    }

    @Override
    public String toString() {
        return "Book{" +
                "mId='" + mId + '\'' +
                ", mTitle='" + mTitle + '\'' +
                ", mAuthor='" + mAuthor + '\'' +
                ", mImageUrl='" + mImageUrl + '\'' +
                ", mRate=" + mRate +
                ", mJsonUrl='" + mJsonUrl + '\'' +
                '}';
    }
}
