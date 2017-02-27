package com.example.shir.weather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import it.sephiroth.android.library.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    ImageView imageView;
    EditText editText;
    String sentenceToSearch;
    TextView cityTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.weatherTV);
        imageView = (ImageView) findViewById(R.id.iconIV);
        editText = (EditText) findViewById(R.id.searchET);
        cityTextView = (TextView) findViewById(R.id.cityTV);

        ((Button) findViewById(R.id.goBTN)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Downloader d = new Downloader();

                String wordToSearch = editText.getText().toString();
                String[] words = wordToSearch.split(" ");
                if (words.length == 1) {
                    d.execute("http://api.openweathermap.org/data/2.5/weather?q="+wordToSearch+"&appid=c8392bc62f7a2c6a1001e93c0711f909&units=metric");
                }
                else
                {
                    for (int i=0 ; i<words.length ; i++)
                    {
                        if (i==0)
                        {
                            sentenceToSearch = words[0]+"%20";
                        }else if (i==words.length-1){
                            sentenceToSearch += words[i];
                        }else {
                            sentenceToSearch += words[i]+"%20";
                        }

                    }
                    d.execute("http://api.openweathermap.org/data/2.5/weather?q="+sentenceToSearch+"&appid=c8392bc62f7a2c6a1001e93c0711f909&units=metric");
                }

                cityTextView.setText(wordToSearch);

                editText.setText("");

            }
        });







    }

    public class Downloader extends AsyncTask <String , Void , String>
    {

        @Override
        protected String doInBackground(String... params) {

            StringBuilder response = null;
            try {

                URL website = new URL(params[0]);
                URLConnection connection = website.openConnection();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                connection.getInputStream()));
                response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null)
                    response.append(inputLine);
                in.close();
            } catch (Exception ee) {

            }
            return response.toString();
        }

        @Override
        protected void onPostExecute(String s) {

            Gson gson = new Gson();

            JSonObject jsonObject = gson.fromJson(s , JSonObject.class);
            textView.setText(jsonObject.weather.get(0).description.toString());
            String weatherIconURL = "http://openweathermap.org/img/w/"+jsonObject.weather.get(0).icon+".png";
            Picasso.with(MainActivity.this).load(weatherIconURL).into(imageView);



        }
    }
}
