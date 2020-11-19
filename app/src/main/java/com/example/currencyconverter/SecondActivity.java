package com.example.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Objects;

public class SecondActivity extends AppCompatActivity {
    ListView listView;
    private CountryAdapter _adapter;
    public static boolean active = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();
        String countryCurrency = intent.getStringExtra(MainActivity.SELECTED_COUNTRY);

        Objects.requireNonNull(getSupportActionBar()).setTitle("Currency Rates");

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

    }

    private void SetCurrencyRates(String countryCurr, ArrayList<Country> countries){

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