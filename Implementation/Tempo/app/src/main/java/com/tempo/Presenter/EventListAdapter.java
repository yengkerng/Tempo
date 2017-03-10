package com.tempo.Presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tempo.Model.CalendarEvent;

import java.util.ArrayList;

/**
 * Created by Costin on 3/9/17.
 */

public class EventListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<CalendarEvent> mDataSource;

    public EventListAdapter(Context context, ArrayList<CalendarEvent> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.list_item_day_event, parent, false);

        TextView NameOfEvent =
                (TextView) rowView.findViewById(R.id.NameOfEvent);

        TextView StartTimeOfEvent =
                (TextView) rowView.findViewById(R.id.StartTimeOfEvent);

        TextView Dash =
                (TextView) rowView.findViewById(R.id.Dash);

        TextView EndTimeOfEvent =
                (TextView) rowView.findViewById(R.id.EndTimeOfEvent);

        TextView LocationOfEvent =
                (TextView) rowView.findViewById(R.id.LocationOfEvent);

        CalendarEvent ce = (CalendarEvent) getItem(position);

        NameOfEvent.setText(ce.getEventName());
        StartTimeOfEvent.setText(ce.getStartTime().toString());
        Dash.setText(" - ");
        EndTimeOfEvent.setText(ce.getEndTime().toString());
        LocationOfEvent.setText(ce.getLocation());


        return rowView;
    }


    @Override
    public int getCount() {
        return mDataSource.size();
    }


    public void setmDataSource(ArrayList<CalendarEvent> mDataSource) {
        this.mDataSource = mDataSource;
    }

    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

}
