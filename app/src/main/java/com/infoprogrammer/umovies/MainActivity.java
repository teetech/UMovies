package com.infoprogrammer.umovies;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.infoprogrammer.umovies.Util.Utils;

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

public class MainActivity extends AppCompatActivity {

    private HttpURLConnection connection = null;
    private BufferedReader reader = null;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button hitBt = (Button) findViewById(R.id.go_btn);
        textView = (TextView) findViewById(R.id.result);

        hitBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Task().execute(Utils.NOW_PLAYING_URL);
            }
        });

    }


    public class Task extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null) {
                    stringBuffer.append(line);
                }

                String finalJson = stringBuffer.toString();

                    JSONObject parentObject = new JSONObject(finalJson);
                int page = parentObject.getInt("page");
                    JSONArray array = parentObject.getJSONArray("results");
                    JSONObject movie = array.getJSONObject(0);
                    String title = movie.getString("original_title");
                    String aboutMovie = movie.getString("overview");
                return title + "," + aboutMovie;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
                try {
                    if (reader != null) {
                        reader.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            textView.setText(s);
        }
    }

}
