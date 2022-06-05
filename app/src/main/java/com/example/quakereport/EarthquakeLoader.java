package com.example.quakereport;

import android.content.Context;
import androidx.annotation.Nullable;
import android.content.AsyncTaskLoader;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<Earthquake>> {
   private String murl;

    public EarthquakeLoader( Context context,String url) {
        super(context);
        murl= url;
    }

    @Override
    protected void onStartLoading(){
        forceLoad();
    }

    @Nullable
    @Override
    public List<Earthquake> loadInBackground() {
        if(murl==null){
            return null;
        }
        List<Earthquake> earthquakes =  QueryUtils.fetchEarthquakeData(murl);
        return earthquakes;
    }
}
