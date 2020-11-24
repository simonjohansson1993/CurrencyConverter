package com.example.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Objects;

public class SecondActivity extends AppCompatActivity {
    private static final String SELECTED_COUNTRY = "com.example.currencyconverter.SELECTED_COUNTRY" ;
    ListView listView;
    private CountryAdapter _adapter;
    public static boolean active = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();
        String countryCurrency = intent.getStringExtra(MainActivity.SELECTED_COUNTRY);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Currency Rates 1 " + countryCurrency);

        listView = findViewById(R.id.myListView);

        //populate array with data

        ArrayList<Country> countryList = new ArrayList<>();
        countryList.add(new Country("Sweden","SEK", R.drawable.sek));
        countryList.add(new Country("Europe","EUR", R.drawable.eur));
        countryList.add(new Country("Japan","JPY", R.drawable.jpy));
        countryList.add(new Country("Great Britain","GBP", R.drawable.gbp));
        countryList.add(new Country("South Korea","KRW", R.drawable.krw));
        countryList.add(new Country("USA","USD", R.drawable.usd));
        countryList.add(new Country("China","CNY", R.drawable.cny));

        _adapter = new CountryAdapter(this,R.layout.list_row,countryList);
        listView.setAdapter(_adapter);

        if (countryCurrency!=null){
            setCurrencyRates(countryCurrency,countryList);
        }
    }

    private void setCurrencyRates(String countryCurr, ArrayList<Country> countries){
        if(countryCurr.equals("SEK")){
          browseAndSetList(countries.get(0),CurrencyRate.SEK_to_SEK);
            browseAndSetList(countries.get(1),CurrencyRate.SEK_to_EUR);
            browseAndSetList(countries.get(2),CurrencyRate.SEK_to_JPY);
            browseAndSetList(countries.get(3),CurrencyRate.SEK_to_GBP);
            browseAndSetList(countries.get(4),CurrencyRate.SEK_to_KRW);
            browseAndSetList(countries.get(5),CurrencyRate.SEK_to_USA);
            browseAndSetList(countries.get(6),CurrencyRate.SEK_to_CNY);
           //Country country = countries.get(0);
           //country.setCurrencyRate(CurrencyRate.SEK_to_SEK);
        }
        else if(countryCurr.equals("EUR")){

            browseAndSetList(countries.get(0),CurrencyRate.EUR_to_SEK);
            browseAndSetList(countries.get(1),CurrencyRate.EUR_to_EUR);
            browseAndSetList(countries.get(2),CurrencyRate.EUR_to_JPY);
            browseAndSetList(countries.get(3),CurrencyRate.EUR_to_GBP);
            browseAndSetList(countries.get(4),CurrencyRate.EUR_to_KRW);
            browseAndSetList(countries.get(5),CurrencyRate.EUR_to_USA);
            browseAndSetList(countries.get(6),CurrencyRate.EUR_to_CNY);
                //Country country = countries.get(0);
                //country.setCurrencyRate(CurrencyRate.SEK_to_SEK);
            }
        }

    private void browseAndSetList(Country country,double RATE){

        country.setCurrencyRate(RATE);
    }


    @Override
    public void onStart() {
        super.onStart();
        active = true;
    }
    @Override
    public void onStop() {
        super.onStop();
        active = false;
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(SecondActivity.this, MainActivity.class);
        startActivity(i);
        finish();
        //super.onBackPressed();
    }
}