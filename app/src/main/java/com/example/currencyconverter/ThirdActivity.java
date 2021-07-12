package com.example.currencyconverter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

public class ThirdActivity extends AppCompatActivity  {

    private static final String TAG = "ThirdActivity";
    private LineChart mChart;
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        //recieve intent string from mainActivity
        Intent intent = getIntent();


        mChart = (LineChart) findViewById(R.id.lineChart);

       // mChart.setOnChartGestureListener(ThirdActivity.this);
        // mChart.setOnChartValueSelectedListener(ThirdActivity.this);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(false);

        //set upper limit
        LimitLine upperLimit = new LimitLine(65f,"Danger high");
        upperLimit.setLineWidth(4f);
        upperLimit.enableDashedLine(10f,10f,0);
        upperLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
        upperLimit.setTextSize(15f);

        //set lower limit
        LimitLine lowerLimit = new LimitLine(65f,"Too Low");
        lowerLimit.setLineWidth(4f);
        lowerLimit.enableDashedLine(10f,10f,0);
        lowerLimit.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_BOTTOM);
        lowerLimit.setTextSize(15f);

        //Eliminate right axis
        mChart.getAxisRight().setEnabled(false);
        mChart.getXAxis().setEnabled(false);

        //Need "entry" for line charts
        ArrayList<Entry> yValues = new ArrayList<>();

        //Add data to y value
        //TODO: for loop api values into entries

        yValues.add(new Entry(0,50f));
        yValues.add(new Entry(1,60f));
        yValues.add(new Entry(2,62f));
        yValues.add(new Entry(3,40f));
        yValues.add(new Entry(4,42f));
        yValues.add(new Entry(5,45f));

        //Line dataset from repository
        LineDataSet set1 = new LineDataSet(yValues,"Data Set 1");
        set1.setFillAlpha(110);
        set1.setColor(Color.RED);
        set1.setLineWidth(3f);
        set1.setValueTextSize(10f);


        //Add data to dataset
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        //Local variable hosting the datasets
        LineData data = new LineData(dataSets);

        mChart.setData(data);


    }
}
