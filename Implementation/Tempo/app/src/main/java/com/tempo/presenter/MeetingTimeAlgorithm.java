package com.tempo.presenter;

import com.tempo.model.CalendarEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by bkell on 3/15/2017.
 */

public class MeetingTimeAlgorithm {
    final static long FIVEMINUTES = 5 * 60 * ((long)(1000));

    private MeetingTimeAlgorithm() {
        // ...
    }

    public static List<MeetingTime> run(List<List<CalendarEvent>> events, long start, long end, long duration) {

        int numUsers = events.size();

        Map<Long, Integer> usersFree = initializeUserFreeMap(start, end, numUsers);

        calculateFreeTimes(events, start, usersFree);

        List<MeetingTime> availableTimes = calculateAvailableTimes(start, end, duration, numUsers, usersFree);

        Collections.sort(availableTimes);

        return availableTimes;

    }

    public static Map<Long, Integer> initializeUserFreeMap(long start, long end, int count) {

        HashMap<Long, Integer> usersFree = new HashMap<>();

        for (long t = start; t <= end; t += FIVEMINUTES) {
            usersFree.put(t, count);
        }

        return usersFree;

    }

    public static void calculateFreeTimes(List<List<CalendarEvent>> userCalendars,
                                           long start, Map<Long, Integer> usersFree) {

        for (List<CalendarEvent> userCalendar : userCalendars) {
            getUniqueTime(userCalendars, start, usersFree, userCalendar);
        }
    }

    public static void getUniqueTime(List<List<CalendarEvent>> userCalendars,
                                     long start, Map<Long, Integer> usersFree, List<CalendarEvent> userCalendar) {
        for (CalendarEvent userEvent : userCalendar) {
            HashSet<Long> uniqueTimeForUser = new HashSet<>();
            long offset = (userEvent.getStartTime() - start) % FIVEMINUTES;
            getUniqueTimeHelper(usersFree, userEvent, offset, uniqueTimeForUser);
        }
    }

    public static void getUniqueTimeHelper(Map<Long, Integer> usersFree, CalendarEvent userEvent, long offset, Set<Long> uniqueTimeForUser) {
        for (long t = userEvent.getStartTime() - offset; t <= userEvent.getEndTime(); t += FIVEMINUTES) {
            if (!uniqueTimeForUser.contains(t) && usersFree.containsKey(t)) {
                uniqueTimeForUser.add(t);
                usersFree.put(t, usersFree.get(t) - 1);
            }
        }
    }

    public static List<MeetingTime> calculateAvailableTimes(long start, long end, long duration, int numUsers, Map<Long, Integer> usersFree) {

        List<MeetingTime> availableTimes = new ArrayList<>();

        for (long t = start; t <= end; t += FIVEMINUTES) {
            int minAttendance = numUsers;
            long spanT = t;
            minAttendance = calculateAvailableTimesHelper(duration, usersFree, spanT, minAttendance, t);
            availableTimes.add(new MeetingTime(minAttendance, spanT));
        }
        return availableTimes;
    }

    public static int calculateAvailableTimesHelper(long duration, Map<Long, Integer> usersFree, long spanT, int minAttendance, long t) {
        for ( ; spanT <= t + duration; spanT += FIVEMINUTES) {
            if (usersFree.containsKey(spanT)) {
                int tempAttendance = usersFree.get(spanT);
                if (tempAttendance < minAttendance) {
                    minAttendance = tempAttendance;
                }
            }
        }
        return minAttendance;
    }

    static class MeetingTime implements Comparable<MeetingTime> {

        private int attendance;
        private long time;

        public MeetingTime(int attendance, long time) {
            this.attendance = attendance;
            this.time = time;
        }

        @Override
        public int compareTo(MeetingTime other) {
            if (this.attendance == other.attendance) {
                if (this.time < other.time) {
                    return -1;
                }
                return 1;
            }
            return other.attendance - this.attendance;
        }

        @Override
        public boolean equals(Object obj) {
            return this == obj;
        }

        public int getAttendance() {
            return attendance;
        }

        public long getTime() {
            return time;
        }

    }
}
