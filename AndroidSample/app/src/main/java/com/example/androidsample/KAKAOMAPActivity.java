package com.example.androidsample;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;

public class KAKAOMAPActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kakaomap);

        MapView map = new MapView(this);
        ViewGroup group = (ViewGroup)findViewById(R.id.mapll);
        MapPoint mapPoint = MapPoint.mapPointWithGeoCoord(37.501,127.039);
        map.setMapCenterPoint(mapPoint,true);
        group.addView(map);
    }
}
