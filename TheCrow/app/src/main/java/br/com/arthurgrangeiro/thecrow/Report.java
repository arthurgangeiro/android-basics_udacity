package br.com.arthurgrangeiro.thecrow;

import android.util.Log;

/**
 * Created by Arthur on 20/01/2017.
 */

public class Report {
    private String mSectionName;
    private String mWebTitle;
    private String mDate;
    private String mWebURL;

    private int FIRST_DATE_INDEX = 0;
    private int LAST_DATE_INDEX = 10;

    public Report() {
    }

    public Report(String mSectionName, String mWebTitle, String mDate, String mWebURL) {
        this.mSectionName = mSectionName;
        this.mWebTitle = mWebTitle;
        this.mDate = mDate;
        this.mWebURL = mWebURL;
    }

    public String getSectionName() {
        return mSectionName;
    }

    public String getWebTitle() {
        return mWebTitle;
    }

    public String getWebURL() {
        return mWebURL;
    }

    public String getDate() {
        //This is like date is: 2016-12-05T12:47:06Z
        String data = mDate.substring(FIRST_DATE_INDEX, LAST_DATE_INDEX);
        Log.i("Texte", data);
        return mDate.substring(FIRST_DATE_INDEX, LAST_DATE_INDEX);
    }

    @Override
    public String toString() {
        return "Report{" +
                "mSectionName='" + mSectionName + '\'' +
                ", mWebTitle='" + mWebTitle + '\'' +
                ", mDate='" + mDate.substring(FIRST_DATE_INDEX, LAST_DATE_INDEX) + '\'' +
                ", mWebURL='" + mWebURL + '\'' +
                '}';
    }
}
