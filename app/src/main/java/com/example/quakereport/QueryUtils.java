package com.example.quakereport;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    /** Sample JSON response for a USGS query */
   /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException exception) {
            Log.e("MalformedURLException", "Error with creating URL", exception);
            return null;
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String JsonResponse="";

        if(url==null){
            return JsonResponse;
        }

        HttpURLConnection urlConnection= null;
        InputStream inputStream= null;
        try{
            urlConnection= (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.connect();

            if(urlConnection.getResponseCode()==200){
                inputStream= urlConnection.getInputStream();
                JsonResponse= readFromStream(inputStream);
            }
            else{
                Log.e("Url error","Error Response Code: "+urlConnection.getResponseCode());

            }
        } finally {
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
            if(inputStream!=null){
                inputStream.close();
            }
        }
        return JsonResponse;
    }
    private static String readFromStream(InputStream inputStream){
        StringBuilder output= new StringBuilder();
        if(inputStream!=null){
            InputStreamReader inputStreamReader= new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader= new BufferedReader(inputStreamReader);
            String line="";
                try {
                    line+= bufferedReader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            while (line!=null){
                output.append(line);
                try {
                    line= bufferedReader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return  output.toString();
    }

    private static ArrayList<Earthquake> extractFeatureFromJSON(String JsonResponse){
        if(TextUtils.isEmpty(JsonResponse)){
            return null;
        }

        ArrayList<Earthquake> earthquakes= new ArrayList<>();

        try {
            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.

            JSONObject baseJsonResponse= new JSONObject(JsonResponse);
            JSONArray earthquakeArray=  baseJsonResponse.getJSONArray("features");

            for(int i=0;i<earthquakeArray.length();i++){
                JSONObject currentEarthquake= earthquakeArray.getJSONObject(i);
                JSONObject properties= currentEarthquake.getJSONObject("properties");
                double mag= properties.getDouble("mag");
                String place= properties.getString("place");
                String url= properties.getString("url");

                long timeInMiliseconds=  properties.getLong("time");

//                Date dateObject= new Date(timeInMiliseconds);

//                Log.v("third","working till here");
//                SimpleDateFormat dateFormatter= new SimpleDateFormat("MMM DD, yyyy");
//                String toDisplay= dateFormatter.format(dateObject);
//                Log.v("fourth","working till here");
                Earthquake earthquake= new Earthquake(mag,place,timeInMiliseconds,url);
                earthquakes.add(earthquake);
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        return earthquakes;
    }

    public static List<Earthquake> fetchEarthquakeData(String requestUrl) {

//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e("Io error", "Problem making the HTTP request.", e);
        }
        // Extract relevant fields from the JSON response and create a list of {@link Earthquake}s
        List<Earthquake> earthquakes = extractFeatureFromJSON(jsonResponse);

        // Return the list of {@link Earthquake}s
        return earthquakes;
    }
}