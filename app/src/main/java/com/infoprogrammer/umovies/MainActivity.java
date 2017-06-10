package com.infoprogrammer.umovies;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import Util.Utils;
import models.NowPlayingMovies;

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
import java.util.List;

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

    public class MovieAdapter extends ArrayAdapter {
        public List<NowPlayingMovies> playingMovies;
        private int resource;
        private LayoutInflater layoutInflater;


        public MovieAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<NowPlayingMovies> objects) {
            super(context, resource, objects);
            playingMovies = objects;
            this.resource = resource;
            layoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.custom_movies, null);
            }
            ImageView moviePoster;
            TextView movieTile;
            TextView releaseDate;
            RatingBar rating;
            TextView overview;

            moviePoster = (ImageView)findViewById(R.id.movie_poster);
            movieTile = (TextView)findViewById(R.id.title);
            releaseDate = (TextView)findViewById(R.id.release_date);
            rating = (RatingBar)findViewById(R.id.rating);
            overview = (TextView)findViewById(R.id.overview);

            movieTile.setText("Title: "+playingMovies.get(position).getMovieTitle());
            overview.setText("overview: "+playingMovies.get(position).getOverview());
            releaseDate.setText("Release Date: "+playingMovies.get(position).getReleaseDate());
            rating.setRating(playingMovies.get(position).getRatingBar()/2);




            return convertView;
        }
    }

}
