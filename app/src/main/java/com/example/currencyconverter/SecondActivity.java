package com.example.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;

import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;



public class SecondActivity extends AppCompatActivity {
    private static final String SELECTED_COUNTRY = "com.example.currencyconverter.SELECTED_COUNTRY" ;


    ListView listView;
    public CountryAdapter _adapter;
    public static boolean active = false;
    private ArrayList<Country> countryList;
    private ArrayList<Currency> currencyList;
    public double finalResult;
    public double reverseConvertion;
    public float conversionRate1 = 0;
    public float conversionRate2 = 0;
    String countryToConvertFrom;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent intent = getIntent();
        countryToConvertFrom = intent.getStringExtra(MainActivity.SELECTED_COUNTRY);
        Log.d("second activity: ",countryToConvertFrom+" hey");


        Objects.requireNonNull(getSupportActionBar()).setTitle("Currency Rates 1 " + countryToConvertFrom);

        listView = findViewById(R.id.myListView);


        //populate array with data

        countryList = new ArrayList<>();
        countryList.add(new Country("Sweden","SEK", R.drawable.sek,"SE"));
        countryList.add(new Country("Europe","EUR", R.drawable.eur,""));
        countryList.add(new Country("Japan","JPY", R.drawable.jpy,""));
        countryList.add(new Country("Great Britain","GBP", R.drawable.gbp,""));
        countryList.add(new Country("South Korea","KRW", R.drawable.krw,""));
        countryList.add(new Country("USA","USD", R.drawable.usd,""));
        countryList.add(new Country("China","CNY", R.drawable.cny,""));


        //Populate currency array
        currencyList = new ArrayList<>();
        currencyList.add(new Currency(0,"to_sek"));
        currencyList.add(new Currency(0,"to_eur"));
        currencyList.add(new Currency(0,"to_jpy"));
        currencyList.add(new Currency(0,"to_gbp"));
        currencyList.add(new Currency(0,"to_krw"));
        currencyList.add(new Currency(0,"to_usd"));
        currencyList.add(new Currency(0,"to_cny"));



        if (countryToConvertFrom!=null){
            parseApi(countryList);

        }

        _adapter = new CountryAdapter(this,R.layout.list_row,countryList);
        listView.setAdapter(_adapter);
        _adapter.notifyDataSetChanged();

    }


    private void parseApi(final ArrayList<Country> countries){
        RequestQueue queue = Volley.newRequestQueue(SecondActivity.this);
        String serverUrl = "http://data.fixer.io/api/latest?access_key=86a1652a0960b0a2d628c70d394f6834";
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,serverUrl,
                new JSONObject(),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {


                            int i = 0;
                            String server_data = response.toString();
                            JSONObject object = new JSONObject(server_data);

                            conversionRate1 = Float.parseFloat(object.getJSONObject("rates").getString(countryToConvertFrom));
                            double userAmount = 1;
                            double result = userAmount * conversionRate1;

                            reverseConvertion = userAmount / result;
                            while(i<7){
                                String countryToConvertTo = countries.get(i).get_currencyName();
                                conversionRate2 = Float.parseFloat(object.getJSONObject("rates").getString(countryToConvertTo));
                                //final result after conversions
                                finalResult = conversionRate2 * reverseConvertion;

                                browseAndSetList(countries.get(i),finalResult);
                                i++;
                            }



                          /* conversionRate = Float.parseFloat(object.getJSONObject("rates").getString(countries.get(1).get_currencyName()));
                            browseAndSetList(countries.get(1),conversionRate);

                            conversionRate = Float.parseFloat(object.getJSONObject("rates").getString(countries.get(2).get_currencyName()));
                            browseAndSetList(countries.get(2),conversionRate);

                            conversionRate = Float.parseFloat(object.getJSONObject("rates").getString(countries.get(3).get_currencyName()));
                            browseAndSetList(countries.get(3),conversionRate);
*/
                            _adapter.notifyDataSetChanged();



                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new com.android.volley.Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error8", error.toString());
            }
        });
        queue.add(jsonObjectRequest);

    }

    private void browseAndSetList(Country country,double RATE){
        country.setCurrencyRate(RATE);
        _adapter.notifyDataSetChanged();
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