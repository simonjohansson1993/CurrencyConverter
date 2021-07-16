package com.example.currencyconverter;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;

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
        mChart.getDescription().setEnabled(false);
        mChart.setTouchEnabled(true);

        mChart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);
        mChart.setHighlightPerDragEnabled(true);

        //Eliminate right axis
        mChart.getAxisRight().setEnabled(false);

        mChart.setDrawGridBackground(false);
        //mChart.getXAxis().setEnabled(false);





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
        yValues.add(new Entry(6,50f));
        yValues.add(new Entry(7,60f));
        yValues.add(new Entry(8,62f));
        yValues.add(new Entry(9,40f));
        yValues.add(new Entry(10,42f));
        yValues.add(new Entry(11,45f));
        yValues.add(new Entry(12,45f));


        //Line dataset from repository
        LineDataSet set1 = new LineDataSet(yValues,"Data Set 1");
        set1.setFillAlpha(110);
        set1.setColor(Color.RED);
        set1.setLineWidth(2f);
        set1.setValueTextSize(0f);


        //Add data to dataset
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);

        //Local variable hosting the datasets
        LineData data = new LineData(dataSets);

        mChart.setData(data);

        String [] months = new String [] {"jan","feb","mar","apr","may","jun","jul","aug","sep","oct","nov","dec",""};

        Objects.requireNonNull(getSupportActionBar()).setTitle("EUR TO SEK - GRAPH");
        XAxis xAxis = mChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP_INSIDE);
        xAxis.setTextSize(14f);
        xAxis.setTextColor(Color.BLACK);
        xAxis.setDrawAxisLine(false);
        xAxis.setDrawGridLines(false);
        //xAxis.setTextColor(Color.rgb(255, 192, 56));
        xAxis.setCenterAxisLabels(true);
        xAxis.setGranularity(1); // one step
        xAxis.setGranularityEnabled(true);
        xAxis.setPosition(XAxis.XAxisPosition.BOTH_SIDED);

        xAxis.setLabelCount(13,true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));


        YAxis yAxis = mChart.getAxisLeft();
        yAxis.setDrawAxisLine(false);
        yAxis.setGranularityEnabled(true);
        //yAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        yAxis.setTextSize(14f);

    }
}
