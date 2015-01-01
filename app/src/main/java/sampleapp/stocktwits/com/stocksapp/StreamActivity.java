package sampleapp.stocktwits.com.stocksapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import sampleapp.stocktwits.com.stocksapp.Data.GetSymbolResponse;
import sampleapp.stocktwits.com.stocksapp.Data.Messages;

public class StreamActivity extends ActionBarActivity {
    private ListView mStreamListView;
    private Activity mActivity;
    private String mTicker;
    private SwipeRefreshLayout mRefreshLayout;
    private Boolean mMoreMessagesToLoad;
    private String mSinceMessageId;
    private String mMaxMessageId;
    private Messages[] mRetreivedMessages;
    private ArrayList<Messages> mRetreivedMessagesList = new ArrayList<Messages>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream);

        mActivity = this;
        mStreamListView = (ListView) findViewById(R.id.streamListView);
        mRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                    refreshStream();
            }
        });

        mTicker = getIntent().getStringExtra("ticker");

        if (mTicker != null) {
            StockTwitsClient
                    .getInstance()
                    .getSymbolStream(mTicker, mGetInitialMessagesCallback);
        }
    }

    protected Callback<GetSymbolResponse> mGetInitialMessagesCallback = new Callback<GetSymbolResponse>() {
        @Override
        public void success(GetSymbolResponse getSymbolResponse, Response response) {

            mRetreivedMessages = getSymbolResponse.messages;
            int maxLength = mRetreivedMessages.length;

            for(int i=0; i<maxLength; i++){
                mRetreivedMessagesList.add(mRetreivedMessages[i]);
            }

            mStreamListView.setAdapter(
                    new StreamResultsAdapter(mActivity,
                            R.layout.stream_list_item,
                            mRetreivedMessages));

            mMoreMessagesToLoad = getSymbolResponse.cursor.more;
            int length = getSymbolResponse.messages.length;
            mSinceMessageId = getSymbolResponse.cursor.since;
        }

        @Override
        public void failure(RetrofitError error) {
            Toast toast = Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG);
            toast.show();
            mRefreshLayout.setRefreshing(false);
        }
    };

    protected Callback<GetSymbolResponse> mGetMoreMessagesCallback = new Callback<GetSymbolResponse>() {
        @Override
        public void success(GetSymbolResponse getSymbolResponse, Response response) {

            mRetreivedMessages = getSymbolResponse.messages;
            if(mRetreivedMessages != null){
                int maxLength = mRetreivedMessages.length;

                for(int i=0; i<maxLength; i++){
                    mRetreivedMessagesList.add(mRetreivedMessages[i]);
                }

                Messages[] newMentions = new Messages[mRetreivedMessagesList.size()];
                newMentions = mRetreivedMessagesList.toArray(newMentions);

                mStreamListView.setAdapter(
                        new StreamResultsAdapter(mActivity,
                                R.layout.stream_list_item,
                                newMentions));

                mMoreMessagesToLoad = getSymbolResponse.cursor.more;
                mMaxMessageId = getSymbolResponse.cursor.max;
                mSinceMessageId = getSymbolResponse.cursor.since;
                int length = getSymbolResponse.messages.length;

                int messageCount = getSymbolResponse.messages.length;

                Toast toast = Toast.makeText(getApplicationContext(),
                        String.valueOf(messageCount)+" new mentions",
                        Toast.LENGTH_LONG);
                toast.show();
                mRefreshLayout.setRefreshing(false);
            }
            mRefreshLayout.setRefreshing(false);
        }

        @Override
        public void failure(RetrofitError error) {
            mRefreshLayout.setRefreshing(false);
            Toast toast = Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG);
            toast.show();
        }
    };

    private void refreshStream() {
        StockTwitsClient
                .getInstance()
                .getLatestSymbolMessages(mTicker, mSinceMessageId, mGetMoreMessagesCallback);
    }
}
