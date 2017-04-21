package com.example.groupproject.weatherrecs;

/**
 * Created by Elijah Antoine on 4/17/2017.
 */
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Marco on 4/17/2017.
 */

public class DownloadHourlyData extends AsyncTask<String, Void, String> {


    //This connects to the Wunderground API's URL to grab current weather data upon opening the app.
    @Override
    protected String doInBackground(String... urls){

        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;

        try {
            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);

            int data = reader.read();

            while(data != -1){
                char current = (char) data;
                result += current;
                data = reader.read();
            }

            return result;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //This parses the downloaded Wunderground JSON file for certain attributes, like current temperature or UV index, etc.
    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);

        //MainActivity.temperatureTextView.append("hello");

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject weatherData = jsonObject.getJSONObject("current_observation");
            JSONObject cityData = weatherData.getJSONObject("display_location");



            int meanRain = 0;
            //Grab 6 hours worth of POP (percentage of precipitation)
            int givenHourOfRain = 0;


            if (meanRain > 50 || givenHourOfRain > 70){
                //Reccommend Umbrella/Rainboots
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
