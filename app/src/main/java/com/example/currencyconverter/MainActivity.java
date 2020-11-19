package com.example.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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


import java.util.ArrayList;
 //implements AdapterView.OnItemSelectedListener
public class MainActivity extends AppCompatActivity  {
    public static final String SELECTED_COUNTRY = "com.example.currencyconverter.SELECTED_COUNTRY";

    public static boolean active = false;
    private ArrayList<Country> _countryList;
    private CountryAdapter _adapter;
    public String country1 = "";
    public String country2 = "";
    public Country countryToCalc= null;
    public double userAmount;
    public double finalResult;



     TextView result_textView;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initList();


        Spinner mySpinner1 = (Spinner) findViewById(R.id.spinner1);
        Spinner mySpinner2 = (Spinner) findViewById(R.id.spinner2);


        _adapter = new CountryAdapter(this,R.layout.country_spinner_row,_countryList);
        mySpinner1.setAdapter(_adapter);
        mySpinner2.setAdapter(_adapter);

        result_textView = (TextView) findViewById(R.id.result_text);
        final TextView currencyTextView = (TextView) findViewById(R.id.textView_currency_small);

         Button rate_btn = (Button) findViewById(R.id.more_info_btn);

         rate_btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(!country1.equals("")){
                     Intent i = new Intent(MainActivity.this, SecondActivity.class);
                     i.putExtra(SELECTED_COUNTRY,country1);
                     startActivity(i);
                     finish();
                 }
                 else{
                     String selectCountryText = "select a country to see currency rates";
                     Toast.makeText(MainActivity.this, selectCountryText, Toast.LENGTH_SHORT).show();
                 }

             }
         });

         final EditText userInput = (EditText) findViewById(R.id.editTextNumber);
         userInput.setOnKeyListener(new View.OnKeyListener() {
             public boolean onKey(View v, int keyCode, KeyEvent event) {
                 // If the event is a key-down event on the "enter" button
                 if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                         (keyCode == KeyEvent.KEYCODE_ENTER)) {
                     // Perform action on key press
                     //String test = "test";
                     if (userInput.getText().toString().matches("")) {
                         String hint = "NaN";
                         result_textView.setText(hint);
                     }
                     else{
                         double currencyRate = countryToCalc.getCurrencyRate();
                         userAmount = Integer.parseInt(userInput.getText().toString());
                         finalResult = (userAmount*currencyRate);
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
                if (!clickedCountryCurrency.equals("choose country")){
                    Toast.makeText(MainActivity.this, clickedCountryCurrency + " selected", Toast.LENGTH_SHORT).show();
                    country1 = clickedCountryCurrency ;
                    currencyTextView.setText(country1);
                }
                else{
                    country1= "";
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
                if (!clickedCountryCurrency.equals("choose country")){
                    Toast.makeText(MainActivity.this, clickedCountryCurrency + " selected", Toast.LENGTH_SHORT).show();
                    country2 = clickedCountryCurrency ;
                    setCurrencyConstants(country1,country2,clickedCountry);
                    countryToCalc = clickedCountry;

                    if (userInput.getText().toString().matches("")) {
                        String hint = "NaN";
                        result_textView.setText(hint);
                    }
                    else{

                        finalResult = (userAmount*clickedCountry.getCurrencyRate());
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

         if (countryCurrency.equals("SEK")){
           if (countryToConvert.equals("EUR")){
                country.setCurrencyRate(CurrencyRate.SEK_to_EUR); }
           else if (countryToConvert.equals("USD")){
               country.setCurrencyRate(CurrencyRate.SEK_to_USA);
           }else if (countryToConvert.equals("JPY")){
               country.setCurrencyRate(CurrencyRate.SEK_to_JPY);
           }else if (countryToConvert.equals("GBP")){
               country.setCurrencyRate(CurrencyRate.SEK_to_GBP);
           }else if (countryToConvert.equals("KRW")){
               country.setCurrencyRate(CurrencyRate.SEK_to_KRW);
           }else if (countryToConvert.equals("CNY")){
               country.setCurrencyRate(CurrencyRate.SEK_to_CNY);
           }

           // case "EUR": intent.putExtra("Title", l2); break;
            //case "JPY": intent.putExtra("Title", l3); break;
            //case "GBP": intent.putExtra("Title", l4); break;
            //case "KRW": intent.putExtra("Title", l5); break;
            //case "USD": intent.putExtra("Title", l6); break;
            //case "CNY": intent.putExtra("Title", l7); break;


        }

    }
     @Override
     protected void onPause() {

         super.onPause();
         active = false;
     }
     @Override
     protected void onStop() {

         super.onStop();
         active = false;
     }

     @Override
     protected void onResume() {
         super.onResume();
         initList();
         active = true;
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