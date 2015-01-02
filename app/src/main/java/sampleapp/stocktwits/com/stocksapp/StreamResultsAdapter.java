package sampleapp.stocktwits.com.stocksapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import java.util.Locale;
import java.util.TimeZone;

import sampleapp.stocktwits.com.stocksapp.Data.Messages;

public class StreamResultsAdapter extends ArrayAdapter<Messages> {
    private Activity mActivty;
    private Messages[] mSymbolMessages;
    private int mResource;

    public StreamResultsAdapter(Activity activity, int resource, Messages[] messages){
        super(activity,resource,messages);
        mActivty = activity;
        mResource = resource;
        mSymbolMessages = messages;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        View row = null;
        if(mSymbolMessages[position]!=null){
            LayoutInflater inflater = mActivty.getLayoutInflater();
            row = inflater.inflate(mResource, parent, false);
        }

        TextView username = (TextView) row.findViewById(R.id.userName);
        username.setText(mSymbolMessages[position].user.username);

        TextView message = (TextView) row.findViewById(R.id.userMessage);
        message.setText(mSymbolMessages[position].body);

        DateTime date = ISODateTimeFormat.dateTimeParser().parseDateTime(mSymbolMessages[position].created_at);

        DateTimeFormatter fmt = DateTimeFormat.forPattern("MMM d 'at' hh:mm:ss a")
                .withLocale(Locale.US)
                .withZone(DateTimeZone.forTimeZone(TimeZone.getDefault()));
        String formattedDateTime = date.toString(fmt);

        TextView timestamp = (TextView) row.findViewById(R.id.timestamp);
        timestamp.setText(formattedDateTime);

        return row;
    }
}
