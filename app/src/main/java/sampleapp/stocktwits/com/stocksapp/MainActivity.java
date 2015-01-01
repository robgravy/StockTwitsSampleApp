package sampleapp.stocktwits.com.stocksapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends ActionBarActivity {

    private EditText mTickerEditText;
    private Button mSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTickerEditText = (EditText) findViewById(R.id.tickerEditText);

        mSearchButton = (Button) findViewById(R.id.searchBtn);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ticker = mTickerEditText.getText().toString();

                if(ticker != null){
                    Intent intent = new Intent(getApplicationContext(), StreamActivity.class);
                    intent.putExtra("ticker",ticker );
                    startActivity(intent);
                }
            }
        });
    }
}
