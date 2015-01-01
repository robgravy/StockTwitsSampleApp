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
    private Boolean mMoreMentionsToLoad;
    private String mSinceMentionId;
    private Messages[] mRetreivedMentions;
    private ArrayList<Messages> mRetreivedMentionsList= new ArrayList<Messages>();

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
                    .getSymbolStream(mTicker, mGetInitialMentions);
        }
    }

    protected Callback<GetSymbolResponse> mGetInitialMentions = new Callback<GetSymbolResponse>() {
        @Override
        public void success(GetSymbolResponse getSymbolResponse, Response response) {

            mRetreivedMentions = getSymbolResponse.messages;
            int maxLength = mRetreivedMentions.length;

            for(int i=0; i<maxLength; i++){
                mRetreivedMentionsList.add(mRetreivedMentions[i]);
            }

            mStreamListView.setAdapter(
                    new StreamResultsAdapter(mActivity,
                            R.layout.stream_list_item,
                            mRetreivedMentions));

            mMoreMentionsToLoad = getSymbolResponse.cursor.more;
            int length = getSymbolResponse.messages.length;
            mSinceMentionId = getSymbolResponse.cursor.since;
        }

        @Override
        public void failure(RetrofitError error) {
            Toast toast = Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG);
            toast.show();
            mRefreshLayout.setRefreshing(false);
        }
    };

    protected Callback<GetSymbolResponse> mGetMoreMentionsCallback = new Callback<GetSymbolResponse>() {
        @Override
        public void success(GetSymbolResponse getSymbolResponse, Response response) {

            mRetreivedMentions = getSymbolResponse.messages;
            if(mRetreivedMentions != null){
                int maxLength = mRetreivedMentions.length;

                for(int i=0; i<maxLength; i++){
                    mRetreivedMentionsList.add(mRetreivedMentions[i]);
                }

                Messages[] newMentions = new Messages[mRetreivedMentionsList.size()];
                newMentions = mRetreivedMentionsList.toArray(newMentions);

                mStreamListView.setAdapter(
                        new StreamResultsAdapter(mActivity,
                                R.layout.stream_list_item,
                                newMentions));


                mMoreMentionsToLoad = getSymbolResponse.cursor.more;
                int length = getSymbolResponse.messages.length;
                mSinceMentionId = getSymbolResponse.cursor.since;

                int messageCount = getSymbolResponse.messages.length;

                Toast toast = Toast.makeText(getApplicationContext(),
                        "GET More Mentions: "+ String.valueOf(messageCount)+" new mentions.",
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
                .getSymbolStreamFromLastMention(mTicker, mSinceMentionId, mGetMoreMentionsCallback);
    }
}
