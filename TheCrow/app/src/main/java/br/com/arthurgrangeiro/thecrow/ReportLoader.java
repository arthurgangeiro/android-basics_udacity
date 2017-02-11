package br.com.arthurgrangeiro.thecrow;

import android.content.Context;
import android.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by Arthur on 20/01/2017.
 */

public class ReportLoader extends AsyncTaskLoader<List<Report>> {

    /**
     * Query URL
     */
    private String mUrl;

    /**
     * Constructs a new ReportLoader.
     *
     * @param context
     */
    public ReportLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public List<Report> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        List<Report> reports = QueryUtils.fetchReportData(mUrl);
        return reports;
    }
}
