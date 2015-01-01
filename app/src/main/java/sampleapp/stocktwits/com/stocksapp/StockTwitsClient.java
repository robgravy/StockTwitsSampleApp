package sampleapp.stocktwits.com.stocksapp;

import retrofit.Callback;
import retrofit.RestAdapter;
import sampleapp.stocktwits.com.stocksapp.Data.GetSymbolResponse;

public class StockTwitsClient {
    private static final String API_URL = "https://api.stocktwits.com/api/2/streams/";
    private static final String ACCESS_TOKEN = "";
    private static final StockTwitsClient INSTANCE = new StockTwitsClient();
    private static StreamService mStreamService;

    private StockTwitsClient(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(API_URL)
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .build();

        mStreamService = restAdapter.create(StreamService.class);
    }

    public static StockTwitsClient getInstance(){ return INSTANCE; }

    public void getSymbolStream(String symbol, Callback<GetSymbolResponse> callback){
        mStreamService.getMessagesBySymbol(symbol, callback);
    }

    public void getLatestSymbolMessages(String symbol, String sinceID, Callback<GetSymbolResponse> callback){
        mStreamService.getMessagesBySymbolAndSinceValue(symbol, sinceID, callback);
    }

    public void getOlderSymbolMessages(String symbol, String maxID, Callback<GetSymbolResponse> callback){
        mStreamService.getMessagesBySymbolAndMaxValue(symbol, maxID, callback);
    }
}
