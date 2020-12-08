package com.example.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;

import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;

//implements AdapterView.OnItemSelectedListener
public class MainActivity extends AppCompatActivity {
    public static final String SELECTED_COUNTRY = "com.example.currencyconverter.SELECTED_COUNTRY";

    public static boolean active = false;
    private ArrayList<Country> _countryList;
    private CountryAdapter _adapter;
    public String country1 = "";
    public String country2 = "";
    public String defaultCurrency = "";
    public String baseCurrency = "EUR";

    public Country countryToCalc = null;
    public double userAmount;
    public double finalResult;
    public double reverseConvertion;
    public float conversionRate1 = 0;
    public float conversionRate2 = 0;

    String countryCode = "";
    Spinner mySpinner1;
    Spinner mySpinner2;
    EditText userInput;

    LocationManager _locationManager;


    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    public static final String SECOND_COUNTRY = "secondCountry";
    private String serverUrl = "";

    private RequestQueue _queue;

    private String text;

    TextView result_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initList();
        checkCountry();

        _locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

        mySpinner1 = (Spinner) findViewById(R.id.spinner1);
        mySpinner2 = (Spinner) findViewById(R.id.spinner2);

        _queue = Volley.newRequestQueue(this);

        _adapter = new CountryAdapter(this, R.layout.country_spinner_row, _countryList);
        mySpinner1.setAdapter(_adapter);
        mySpinner2.setAdapter(_adapter);

        result_textView = (TextView) findViewById(R.id.result_text);
        final TextView currencyTextView = (TextView) findViewById(R.id.textView_currency_small);

        Button rate_btn = (Button) findViewById(R.id.more_info_btn);

        rate_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!country1.equals("")) {
                    Intent i = new Intent(MainActivity.this, SecondActivity.class);
                    i.putExtra(SELECTED_COUNTRY, country1);
                    startActivity(i);
                    finish();
                } else {
                    String selectCountryText = "select a country to see currency rates";
                    Toast.makeText(MainActivity.this, selectCountryText, Toast.LENGTH_SHORT).show();
                }
            }
        });


        userInput = (EditText) findViewById(R.id.editTextNumber);
        userInput.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    //String test = "test";
                    if (userInput.getText().toString().matches("") ) {
                        String hint = "NaN";
                        result_textView.setText(hint);
                    }
                 /*   else if (!baseCurrency.equals("EUR")){
                        Toast.makeText(MainActivity.this, "Can only convert from EUR", Toast.LENGTH_SHORT).show();
                        String hint = "NaN";
                        result_textView.setText(hint);
                    } */
                        else
                     {
                       // double currencyRate = conversionRate;
                       // userAmount = Integer.parseInt(userInput.getText().toString());
                        //finalResult = (userAmount * currencyRate);
                        parseApi(country2,country1);
                        // result_textView.setText((int) finalResult);
                        // result_textView.setText((int) finalResult + " " + countryToCalc.get_currencyName());
                        //reverseConvertion = (userAmount / finalResult );
                         //Log.d("main", reverseConvertion + "");


                        // Log.d("main", conversionRate + "");

                    }

                    return true;
                }
                return false;
            }
        });

        mySpinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Country clickedCountry = (Country) parent.getItemAtPosition(position);

                //Show user that choose country is empty and cannot be chosen
                String clickedCountryCurrency = clickedCountry.get_currencyName();
                if (!clickedCountryCurrency.equals("choose country")) {
                    Toast.makeText(MainActivity.this, clickedCountryCurrency + " selected", Toast.LENGTH_SHORT).show();
                    country1 = clickedCountryCurrency;
                    baseCurrency = country1;
                    currencyTextView.setText(country1);
                    defaultCurrency = clickedCountryCurrency;
                } else {
                    country1 = "";
                    defaultCurrency = "";
                    baseCurrency = "";
                }
            }

            //Auto generated method
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mySpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Country clickedCountry = (Country) parent.getItemAtPosition(position);

                //Show user that choose country is empty and cannot be chosen
                String clickedCountryCurrency = clickedCountry.get_currencyName();
                if (!clickedCountryCurrency.equals("choose country")) {
                    Toast.makeText(MainActivity.this, clickedCountryCurrency + " selected", Toast.LENGTH_SHORT).show();
                    country2 = clickedCountryCurrency;

                   //Setting currency constants

                    //Using Api

                   // parseApi(country2,country1);

/*
                   // setCurrencyConstants(country1, country2, clickedCountry);
                    countryToCalc = clickedCountry;

                    if (userInput.getText().toString().matches("")) {
                        String hint = "NaN";
                        result_textView.setText(hint);
                    } else {
                        double currencyRate = conversionRate;
                        userAmount = Integer.parseInt(userInput.getText().toString());
                        finalResult = (userAmount * currencyRate);
                        result_textView.setText((int) finalResult + " " + clickedCountryCurrency);

                    } */

                }
            }

            //Auto generated method
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void parseApi(final String convertToCurrency, final String ConvertfromCurrency){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        serverUrl = "http://data.fixer.io/api/latest?access_key=86bf2e99489d9b269f83fbff6a98db46";
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,serverUrl,
                new JSONObject(),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String server_data = response.toString();
                            JSONObject object = new JSONObject(server_data);
                            conversionRate1 = Float.parseFloat(object.getJSONObject("rates").getString(ConvertfromCurrency));
                            userAmount = Integer.parseInt(userInput.getText().toString());

                            double result = userAmount * conversionRate1;
                            reverseConvertion = userAmount / result;

                            //Get the new conversionrate
                            conversionRate2 = Float.parseFloat(object.getJSONObject("rates").getString(convertToCurrency));

                            //final result

                            finalResult = userAmount * conversionRate2 * reverseConvertion;
                            //result_textView.setText((int) finalResult);

                            Log.d("LOOOK HERE!", finalResult + " ");
                            setText(finalResult);
                            //Log.d("main", server_data);

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
    private void setText(double finalResult){
        DecimalFormat numberFormat = new DecimalFormat("#0.0000");
       // result_textView.setText(numberFormat.format(finalResult+ ""));
        result_textView.setText(numberFormat.format(finalResult) + " " + country2);
    }

    private void checkCountry(){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        serverUrl = "http://ipinfo.io/?token=8e7cfa4028878a";
        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,serverUrl,
                new JSONObject(),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String server_data = response.toString();
                            JSONObject object = new JSONObject(server_data);
                            countryCode = object.getString("country");
                            String city = object.getString("city");
                            Log.d("main", countryCode);
                            Log.d("main", server_data);
                            if (countryCode.equals("SE")){
                                mySpinner1.setSelection(1);
                            }


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

    private void initList(){
        _countryList = new ArrayList<>();
        _countryList.add(0,new Country("","choose country",0,""));
        _countryList.add(new Country("Sweden","SEK", R.drawable.sweden1,"SE"));
        _countryList.add(new Country("Europe","EUR", R.drawable.europe1,""));
        _countryList.add(new Country("Japan","JPY", R.drawable.japan1,""));
        _countryList.add(new Country("Great Britain","GBP", R.drawable.england1,""));
        _countryList.add(new Country("South Korea","KRW", R.drawable.south_korea1,""));
        _countryList.add(new Country("USA","USD", R.drawable.usa1,""));
        _countryList.add(new Country("China","CNY", R.drawable.china1,""));
    }


    public void setCurrencyConstants(String countryCurrency,String countryToConvert,Country country){

         if (countryCurrency.equals("SEK")) {
             switch (countryToConvert) {
                 case "EUR":
                     country.setCurrencyRate(CurrencyRate.SEK_to_EUR);
                     break;
                 case "USD":
                     country.setCurrencyRate(CurrencyRate.SEK_to_USA);
                     break;
                 case "JPY":
                     country.setCurrencyRate(CurrencyRate.SEK_to_JPY);
                     break;
                 case "KRW":
                     country.setCurrencyRate(CurrencyRate.SEK_to_KRW);
                     break;
                 case "CNY":
                     country.setCurrencyRate(CurrencyRate.SEK_to_CNY);
                     break;
                 case "GBP":
                     country.setCurrencyRate(CurrencyRate.SEK_to_GBP);
                     break;
             }
         }
         else if (countryCurrency.equals("EUR")){
             switch (countryToConvert) {
                 case "SEK":
                     country.setCurrencyRate(CurrencyRate.EUR_to_SEK);
                     break;
                 case "USD":
                     country.setCurrencyRate(CurrencyRate.EUR_to_USA);
                     break;
                 case "JPY":
                     country.setCurrencyRate(CurrencyRate.EUR_to_JPY);
                     break;
                 case "KRW":
                     country.setCurrencyRate(CurrencyRate.EUR_to_KRW);
                     break;
                 case "CNY":
                     country.setCurrencyRate(CurrencyRate.EUR_to_CNY);
                     break;
                 case "GBP":
                     country.setCurrencyRate(CurrencyRate.EUR_to_GBP);
                     break;
             }
         }
    }
    public void saveData(){
         //private for no other apps usage
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(TEXT, String.valueOf(userInput.getText()));
        int selectedPosition = mySpinner1.getSelectedItemPosition();
        int selectedPosition2 = mySpinner2.getSelectedItemPosition();
        editor.putInt("spinnerSelection", selectedPosition);
        editor.putInt("spinnerSelection2", selectedPosition2);
        editor.apply();

        Toast.makeText(this,"data saved",Toast.LENGTH_SHORT).show();
    }
    public void loadData(){
         SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
         text = sharedPreferences.getString(TEXT,"");
         mySpinner1.setSelection(sharedPreferences.getInt("spinnerSelection",0));
         mySpinner2.setSelection(sharedPreferences.getInt("spinnerSelection2",0));
    }
    public void updateView(){
         userInput.setText(text);
    }
    /*
    public void onLocationChanged(Location location){
        if (location.getLongitude()!=0 && location.getLatitude()!=0){
            longitude = location.getLongitude();
            latitude = location.getLatitude();
        }

        textView.setText("longitude: " + longitude + "\n" + " latitude " + latitude );
    }

    private void location_finder(Location location)  {
        try {
            Geocoder geocoder = new Geocoder(this);
            List<Address> addresses = null;
            addresses = geocoder.getFromLocation(latitude,longitude,1);
            String country = addresses.get(0).getCountryName();
            textView.setText("Country: " + country);
        }
        catch (IOException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Error" + e, Toast.LENGTH_SHORT).show();
        }
    }*/

     @Override
     protected void onPause() {
      //  saveData();
         String TAG = "Hello";
         super.onPause();
         Log.d( TAG, " OnPause");
         active = false;

     }
     @Override
     protected void onStop() {
         String TAG = "Hello";
         Log.d( TAG, " OnStop");
         super.onStop();
         active = false;
        // saveData();

     }


     protected void onResume() {
         String TAG = "Hello";
         super.onResume();
         Log.d( TAG, " OnResume");
         active = true;
        // loadData();
         //updateView();


     }

     @Override
     protected void onDestroy() {
         super.onDestroy();

     }
     @Override
     public void onStart() {
         super.onStart();
         active = true;
     }
 }