package com.tempo.presenter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by bkell on 3/15/2017.
 */

class MeetingTimeAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<MeetingTimeAlgorithm.MeetingTime> meetingTimes;

    MeetingTimeAdapter(Activity activity, List<MeetingTimeAlgorithm.MeetingTime> meetingTimes) {
        this.inflater = activity.getLayoutInflater();
        this.meetingTimes = meetingTimes;
    }

    @Override
    public int getCount() {
        return meetingTimes.size();
    }

    @Override
    public Object getItem(int i) {
        return meetingTimes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup parent) {

        View listItem = inflater.inflate(R.layout.meeting_times_item, parent, false);
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        String date = formatter.format(new Date(meetingTimes.get(i).getTime()));

        ((TextView) listItem.findViewById(R.id.meeting_times_item))
                .setText(date + " : " + meetingTimes.get(i).getAttendance()
                + " members can attend.");

        return listItem;
    }

}
