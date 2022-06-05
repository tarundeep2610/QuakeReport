package com.example.quakereport;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Earthquake>> {

//    private static final String SAMPLE_JSON_RESPONSE = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&orderby=time&minmag=5&limit=20";
    private static final String SAMPLE_JSON_RESPONSE="https://earthquake.usgs.gov/fdsnws/event/1/query";
    private CustomAdapter adapter;
    private static final int EARTHQUAKE_LOADER_ID=1;
    private TextView mEmptyTextview;
    private ProgressBar mProgressBar;
//    private boolean isConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mEmptyTextview= findViewById(R.id.textView2);
        mProgressBar= findViewById(R.id.progressBar);

        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        isConnected = activeNetwork != null &&
//                activeNetwork.isConnectedOrConnecting();

        if(activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting()){
            LoaderManager loaderManager= getLoaderManager();
            loaderManager.initLoader(EARTHQUAKE_LOADER_ID,null,this);
        }
        else{
            mProgressBar.setVisibility(View.GONE);
            mEmptyTextview.setText("No Internet Connection!!");
        }
        ListView earthquakeListView = (ListView) findViewById(R.id.listView);

        // Create a new {@link ArrayAdapter} of earthquakes
         adapter= new CustomAdapter(this,new ArrayList<Earthquake>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        earthquakeListView.setAdapter(adapter);
        earthquakeListView.setEmptyView(mEmptyTextview);

        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Earthquake earthquake= adapter.getItem(position);
                String url= earthquake.getMurl();
                Intent intent= new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });

        }

    @Override
    public Loader<List<Earthquake>> onCreateLoader(int id, Bundle args) {
        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        String minMag= sharedPreferences.getString(getString(R.string.settings_min_magnitude_key),getString(R.string.settings_min_magnitude_default));
        String orderBy= sharedPreferences.getString(getString(R.string.settings_order_by_key),getString(R.string.settings_order_by_default));
        String limit= sharedPreferences.getString("limit","20");
        Uri baseUri= Uri.parse(SAMPLE_JSON_RESPONSE);
        Uri.Builder uriBuilder= baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit",limit);
        uriBuilder.appendQueryParameter("minmag", minMag);
        uriBuilder.appendQueryParameter("orderby",orderBy);

        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<Earthquake>> loader, List<Earthquake> earthquakes) {
        adapter.clear();

        mProgressBar.setVisibility(View.GONE);
        mEmptyTextview.setText("No Earthquake found!!");

        if(earthquakes!=null && !earthquakes.isEmpty()){
            adapter.addAll(earthquakes);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Earthquake>> loader) {
        adapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id= item.getItemId();
        if(id== R.id.action_settings){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}