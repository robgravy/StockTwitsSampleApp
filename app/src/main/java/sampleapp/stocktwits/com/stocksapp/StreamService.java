package sampleapp.stocktwits.com.stocksapp;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;
import sampleapp.stocktwits.com.stocksapp.Data.GetSymbolResponse;

public interface StreamService {

    @GET("/symbol/{symbol}.json")
    public void getMentionsBySymbol(
            @Path("symbol") String symbol,
            Callback<GetSymbolResponse> callback
    );

    @GET("/symbol/{symbol}.json")
    public void getMentionsBySymbolAndSinceValue(
            @Path("symbol") String symbol,
            @Query("since") String maxId,
            Callback<GetSymbolResponse> callback
    );
}


