package com.example.currencyconverter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.nfc.Tag;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static androidx.constraintlayout.motion.widget.Debug.getLocation;

//implements AdapterView.OnItemSelectedListener
public class MainActivity extends AppCompatActivity {
    public static final String SELECTED_COUNTRY = "com.example.currencyconverter.SELECTED_COUNTRY";
    public FusedLocationProviderClient _fusedLocationClient;
    public static boolean active = false;
    private ArrayList<Country> _countryList;
    private CountryAdapter _adapter;
    public String country1 = "";
    public String country2 = "";
    public Country countryToCalc = null;
    public double userAmount;
    public double finalResult;
    Spinner mySpinner1;
    Spinner mySpinner2;
    EditText userInput;
    TextView api_result;
    Button parse_btn;

    public float conversionRate = 0f;
    public float base = 0f;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    public static final String SECOND_COUNTRY = "secondCountry";
    private String serverUrl = "http://data.fixer.io/api/latest?access_key=86bf2e99489d9b269f83fbff6a98db46";

    public ArrayList<Country> country_list_api;

    private RequestQueue _queue;

    private String text;

    TextView result_textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initList();

        mySpinner1 = (Spinner) findViewById(R.id.spinner1);
        mySpinner2 = (Spinner) findViewById(R.id.spinner2);

        parse_btn = (Button) findViewById(R.id.parse_api);
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

        parse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parseApi();

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
                    if (userInput.getText().toString().matches("") || countryToCalc == null) {
                        String hint = "NaN";
                        result_textView.setText(hint);
                    } else {
                        double currencyRate = countryToCalc.getCurrencyRate();
                        userAmount = Integer.parseInt(userInput.getText().toString());
                        finalResult = (userAmount * currencyRate);
                        result_textView.setText((int) finalResult + " " + countryToCalc.get_currencyName());
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
                    currencyTextView.setText(country1);
                } else {
                    country1 = "";
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
                    setCurrencyConstants(country1, country2, clickedCountry);
                    countryToCalc = clickedCountry;

                    if (userInput.getText().toString().matches("")) {
                        String hint = "NaN";
                        result_textView.setText(hint);
                    } else {

                        finalResult = (userAmount * clickedCountry.getCurrencyRate());
                        result_textView.setText((int) finalResult + " " + clickedCountryCurrency);
                    }
                }
            }

            //Auto generated method
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void parseApi(){
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);

        JsonObjectRequest jsonObjectRequest = new
                JsonObjectRequest(Request.Method.GET,serverUrl,
                new JSONObject(),
                new com.android.volley.Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String server_data = response.toString();       //SEK IS HARDCODED BUT IS COUNTRY TO CONVERT TO
                            JSONObject object = new JSONObject(server_data);
                            conversionRate = Float.parseFloat(object.getJSONObject("rates").getString("SEK"));
                            Log.d("main", conversionRate + " ");
                            Log.d("main", response.toString());

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
        _countryList.add(0,new Country("","choose country",0));
        _countryList.add(new Country("Sweden","SEK", R.drawable.sweden1));
        _countryList.add(new Country("Europe","EUR", R.drawable.europe1));
        _countryList.add(new Country("Japan","JPY", R.drawable.japan1));
        _countryList.add(new Country("Great Britain","GBP", R.drawable.england1));
        _countryList.add(new Country("South Korea","KRW", R.drawable.south_korea1));
        _countryList.add(new Country("USA","USD", R.drawable.usa1));
        _countryList.add(new Country("China","CNY", R.drawable.china1));
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
     @Override
     protected void onPause() {
        saveData();
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
         loadData();
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