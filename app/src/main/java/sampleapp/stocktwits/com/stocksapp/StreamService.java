package sampleapp.stocktwits.com.stocksapp;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import sampleapp.stocktwits.com.stocksapp.Data.GetSymbolResponse;

public interface StreamService {

    @GET("/symbol/{symbol}.json")
    public void getMessagesBySymbol(
            @Path("symbol") String symbol,
            Callback<GetSymbolResponse> callback
    );

    @GET("/symbol/{symbol}.json")
    public void getMessagesBySymbolAndSinceValue(
            @Path("symbol") String symbol,
            @Query("since") String sinceID,
            Callback<GetSymbolResponse> callback
    );

    @GET("/symbol/{symbol}.json")
    public void getMessagesBySymbolAndMaxValue(
            @Path("symbol") String symbol,
            @Query("max") String maxID,
            Callback<GetSymbolResponse> callback
    );
}

