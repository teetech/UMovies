package com.infoprogrammer.umovies;

import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView movieList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()

                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())

                .defaultDisplayImageOptions(defaultOptions)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .memoryCache(new WeakMemoryCache())
                .build();
        ImageLoader.getInstance().init(config);
        movieList = (ListView) findViewById(R.id.now_playing_movies);

        new Task().execute(Utils.NOW_PLAYING_URL);

    }


    public class Task extends AsyncTask<String, String, List<NowPlayingMovies>> {

        @Override
        protected List<NowPlayingMovies> doInBackground(String... urls) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
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

                List<NowPlayingMovies> movieList = new ArrayList<>();

                for (int i = 0; i < array.length(); i++) {
                    JSONObject finalObject = array.getJSONObject(i);
                    NowPlayingMovies nowPlayingMovies = new NowPlayingMovies();
                    nowPlayingMovies.setMovieTitle(finalObject.getString("original_title"));
                    nowPlayingMovies.setPoster(finalObject.getString("poster_path"));
                    nowPlayingMovies.setReleaseDate(finalObject.getString("release_date"));
                    nowPlayingMovies.setOverview(finalObject.getString("overview"));
                    nowPlayingMovies.setRatingBar((float) finalObject.getDouble("vote_average"));

                    movieList.add(nowPlayingMovies);
                }
                return movieList;
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
        protected void onPostExecute(List<NowPlayingMovies> result) {
            super.onPostExecute(result);
            MovieAdapter movieAdapter = new MovieAdapter(getApplicationContext(), R.layout.custom_movies, result);

            movieList.setAdapter(movieAdapter);
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
                convertView = layoutInflater.inflate(resource, null);
            }
            ImageView moviePoster;
            TextView movieTile;
            TextView releaseDate;
            RatingBar rating;
            TextView overview;

            moviePoster = (ImageView) convertView.findViewById(R.id.movie_poster);
            movieTile = (TextView) convertView.findViewById(R.id.title_id);
            releaseDate = (TextView) convertView.findViewById(R.id.release_date);
            rating = (RatingBar) convertView.findViewById(R.id.rating);
            overview = (TextView) convertView.findViewById(R.id.overview);

            ImageLoader.getInstance().displayImage("https://image.tmdb.org/t/p/w500/" + playingMovies.get(position).getPoster(), moviePoster);

            movieTile.setText("Title: " + playingMovies.get(position).getMovieTitle());
            overview.setText("overview: " + playingMovies.get(position).getOverview());
            releaseDate.setText("Release Date: " + playingMovies.get(position).getReleaseDate());
            rating.setRating(playingMovies.get(position).getRatingBar() / 2);


            return convertView;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbars, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.reload_id) {
            new Task().execute(Utils.NOW_PLAYING_URL);

        }
        return super.onOptionsItemSelected(item);
    }
}
