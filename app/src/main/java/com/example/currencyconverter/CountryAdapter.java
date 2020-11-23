package com.example.currencyconverter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CountryAdapter extends ArrayAdapter<Country> {
    private int _resource;
    private Context _context;
    public CountryAdapter(Context context,int resource,ArrayList<Country> countryList){
        super(context,0,countryList);
        this._resource = resource;
        this._context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(SecondActivity.active){
            LayoutInflater layoutInflater = LayoutInflater.from(_context);

            convertView = layoutInflater.inflate(_resource,parent,false);
            ImageView imageViewFlag = convertView.findViewById(R.id.flag_image_eu);
            TextView textViewCurrency = convertView.findViewById(R.id.currencyRate_number);
            TextView textViewCountryName = convertView.findViewById(R.id.countryName_text);
            Country currentCountry = getItem(position);

            if (MainActivity.active){
                currentCountry = null;
            }

            if (currentCountry!= null ){
                imageViewFlag.setImageResource(currentCountry.get_flagImage());
                textViewCurrency.setText( currentCountry.getCurrencyRate()+ " " + currentCountry.get_currencyName());
                //textViewCurrency.setText(currentCountry.get_currencyName() + " " +CurrencyRate.EUR_to_SEK);
                textViewCountryName.setText(currentCountry.getCountryName());
            }


            return convertView;
        }
        else{
            return initView(position,convertView,parent);
        }

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }
    private View initView(int position, View convertView, ViewGroup parent){
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.country_spinner_row,parent,false);
        }
        ImageView imageViewFlag = convertView.findViewById(R.id.swe_image);
        TextView textViewName = convertView.findViewById(R.id.swe_image_name);

        Country currentCountry = getItem(position);


        if (currentCountry!= null){
            imageViewFlag.setImageResource(currentCountry.get_flagImage());
            textViewName.setText(currentCountry.get_currencyName());
        }


        return convertView;
    }

}
