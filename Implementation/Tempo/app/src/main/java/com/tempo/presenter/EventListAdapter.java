package com.tempo.presenter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.tempo.model.CalendarEvent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


/**
 * Created by Costin on 3/9/17.
 */

public class EventListAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private List<CalendarEvent> mDataSource;
    DateFormat formatter;


    public EventListAdapter(Context context, List<CalendarEvent> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        formatter = new SimpleDateFormat("hh:mm a");

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.list_item_day_event, parent, false);

        TextView nameOfEvent = (TextView) rowView.findViewById(R.id.NameOfEvent);
        TextView startTimeOfEvent = (TextView) rowView.findViewById(R.id.StartTimeOfEvent);
        TextView endTimeOfEvent = (TextView) rowView.findViewById(R.id.EndTimeOfEvent);
        TextView locationOfEvent = (TextView) rowView.findViewById(R.id.LocationOfEvent);

        CalendarEvent ce = (CalendarEvent) getItem(position);

        nameOfEvent.setText(ce.getEventName());
        startTimeOfEvent.setText(formatter.format(new Date(ce.getStartTime())));
        endTimeOfEvent.setText(formatter.format(new Date(ce.getEndTime())));
        locationOfEvent.setText(ce.getLocation());


        return rowView;
    }


    @Override
    public int getCount() {
        return mDataSource.size();
    }


    public void setmDataSource(List<CalendarEvent> mDataSource) {
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
