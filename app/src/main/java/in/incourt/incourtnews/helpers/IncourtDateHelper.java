package in.incourt.incourtnews.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by bhavan on 2/3/17.
 */

public class IncourtDateHelper {


    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * SECOND_MILLIS;
    private static final int HOUR_MILLIS = 60 * MINUTE_MILLIS;
    private static final int DAY_MILLIS = 24 * HOUR_MILLIS;

    private static final int WEEK = DAY_MILLIS * 7;

    private static final int MONTH = DAY_MILLIS * 30;


    public static String getTimeAgo(long time) {
        if (time < 1000000000000L) {
            // if timestamp given in seconds, convert to millis
            time *= 1000;
        }

        long now = new Date().getTime();

        if (time > now || time <= 0) {
            return null;
        }

        // TODO: localize
        final long diff = now - time;

        if (diff < MINUTE_MILLIS) {
            return "just now";
        } else if (diff < 2 * MINUTE_MILLIS) {
            return "a minute ago";
        } else if (diff < 50 * MINUTE_MILLIS) {
            return diff / MINUTE_MILLIS + " minutes ago";
        }else if (diff < 90 * MINUTE_MILLIS) {
            return "an hour ago";
        }else if (diff < 24 * HOUR_MILLIS) {
            return "Today";
        }else if (diff < 48 * HOUR_MILLIS) {
            return "Yesterday";
        }else if((diff <  WEEK)){
            return diff / DAY_MILLIS + " days ago";
        }else if(diff < (24 * 7) * MONTH){
            if((diff / WEEK) > 1) return (diff / WEEK) + " weeks ago";
            return " week ago";
        }else{
            int count = (int) (diff / ( (24 * 7) * MONTH));
            count--;
            if(count < 1) return "month ago";
            return String.valueOf(count) + " months ago";
        }

    }

    public static long parse(String date) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date).getTime();
    }

    public static long getTime(String date) throws ParseException {
        if(date == null) return 0;
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date).getTime();
    }

    public static String getTimeAgo(String pubDate) {
        try {
            return getTimeAgo(getTime(pubDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
