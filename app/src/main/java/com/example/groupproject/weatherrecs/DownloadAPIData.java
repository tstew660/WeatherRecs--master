package com.example.groupproject.weatherrecs;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static com.example.groupproject.weatherrecs.MainActivity.iconImageView;

/**
 * Created by Nichole on 4/16/2017.
 */


public class DownloadAPIData extends AsyncTask<String, Void, String> {

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


    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);

        Context applicationContext = MainActivity.getContextOfApplication();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(applicationContext);
        String hotTempPref = prefs.getString("hot_temperature", "80");
        String coldTempPref = prefs.getString("cold_temperature", "45");


        String cityName, weather;
        int temp;
        String uvMsg = "";
        Double uv;
        String wearMsg = "Wear (temp)";

        try {
            JSONObject jsonObject = new JSONObject(result);
            JSONObject weatherData = jsonObject.getJSONObject("current_observation");
            JSONObject cityData = weatherData.getJSONObject("display_location");
            String iconUrl = weatherData.getString("icon_url");

            /*
            manually select which icon set to use
            to use specific icon set change letter at /a/

            String icon = weatherData.getString("icon");
            String iconUrl = "https://icons.wxug.com/i/c/a/" + icon + ".gif";
            */

            //fetches string data from JSON
            cityName = cityData.getString("city");

            weather = weatherData.getString("weather");
            temp = (int) Double.parseDouble(weatherData.getString("feelslike_f"));
            uv = Double.parseDouble(weatherData.getString("UV"));

            //TODO: UV index fluctuates throughout the day. Consider moving this to hourly if we have time for it.

            //generates sunscreen message based on UV index rating

            if (0 <= uv && uv < 3)
                uvMsg = "You might want to wear sunscreen today.";
            else if (3 <= uv && uv < 6)
                uvMsg = "You should wear sunscreen today.";
            else if (6 <= uv)
                uvMsg = "You definitely need to wear sunscreen today.";

            /*
            possible way to do temperature preferences
            shows numbers just for reference. remove later
            message + actual temp + temp preference
            */
            if (temp >= Double.parseDouble(hotTempPref))
                wearMsg = "Wear Shorts " + temp + " " + hotTempPref;
            if (temp <= Double.parseDouble(coldTempPref))
                wearMsg = "Wear pants " + temp + " " + coldTempPref;



            //Applies parsed data from JSON to UI elements
            MainActivity.cityTextView.setText(cityName);
            MainActivity.temperatureTextView.setText(temp + "°F");
            MainActivity.statusTextView.setText(weather);
            MainActivity.uvTextView.setText(uvMsg);
            MainActivity.clothesTextView.setText(wearMsg);

            //Adds icon image
            new DownloadImageTask(iconImageView)
                    .execute(iconUrl);


            /*String tempF = jsonObject.getString("feelslike_f");
            String UV = jsonObject.getString("UV");*/


            //Applies parsed data from JSON to UI elements
            //MainActivity.iconImageView.setImageBitmap(bmp);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
