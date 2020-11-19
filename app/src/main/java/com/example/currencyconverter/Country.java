package com.example.currencyconverter;

public class Country {
    private double _currencyRate = 1;
    private String _currencyName;
    private String _countryName;
    private int _flagImage;

    public Country(String countryName,String currencyName, int flagImage) {
        _countryName = countryName;
        _currencyName = currencyName;
        _flagImage= flagImage;
    }

    public double getCurrencyRate() {
        return _currencyRate;
    }

    public void setCurrencyRate(double currencyRate) {
        this._currencyRate = currencyRate;
    }

    public String get_currencyName() {
        return _currencyName;
    }

    public String getCountryName() {
        return _countryName;
    }

    public void set_flagImage(int _flagImage) {
        this._flagImage = _flagImage;
    }

    public int get_flagImage() {
        return _flagImage;
    }

}
