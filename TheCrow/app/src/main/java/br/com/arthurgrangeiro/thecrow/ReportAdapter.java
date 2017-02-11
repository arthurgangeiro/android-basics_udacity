package br.com.arthurgrangeiro.thecrow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Arthur on 20/01/2017.
 */

public class ReportAdapter extends ArrayAdapter<Report> {
    public ReportAdapter(Context context, ArrayList<Report> reports) {
        super(context, 0, reports);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(
                    R.layout.reports_item, parent, false
            );
        }

        Report currentReport = getItem(position);

        TextView sectionName = (TextView) view.findViewById(R.id.section_name);
        sectionName.setText(currentReport.getSectionName());

        TextView reportTitle = (TextView) view.findViewById(R.id.report_title);
        reportTitle.setText(currentReport.getWebTitle());

        TextView reportDate = (TextView) view.findViewById(R.id.report_date);
        reportDate.setText(currentReport.getDate());

        return view;
    }
}
